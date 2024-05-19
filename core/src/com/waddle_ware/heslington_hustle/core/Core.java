package com.waddle_ware.heslington_hustle.core;
// CHANGELOG: Added access to Activity Location to be used for streak calculation
import com.waddle_ware.heslington_hustle.ActivityLocation;
// CHANGELOG: Added library to help with array processing
import java.util.Arrays;

/**
 * The Core class represents the core functionality of the game, managing game state, interactions, and scoring.
 */
public class Core {
    // The constants below define the values for the impact that the individual components will have on the score.
    public static final int MEAL_SCORE_VALUE = 30;
    public static final int RELAX_SCORE_VALUE = 50;
    public static final int STUDY_SCORE_VALUE = 100;
    public static final int MEAL_SCORE_PENALTY = -100;
    public static final int RELAX_SCORE_PENALTY = -100;
    public static final int STUDY_TOO_MUCH_PENALTY = -50;
    public static final int TOO_MUCH_STUDY_THRESHOLD = 3;

    public static final int MAX_NUMBER_OF_DAYS = 7;

    // Member variables
    public Energy energy;
    private int day;
    public Time time;

    public int[] study_count;
    public int[] relax_count;
    public int[] meal_count;

    /**
     * Constructs a new Core instance.
     * Initialises energy, time, and activity count arrays.
     */
    public Core() {
        this.energy = new Energy(4);
        this.time = new Time(15,3);
        this.study_count = new int[7];
        this.relax_count = new int[7];
        this.meal_count = new int[7];
    }

    /**
     * This method should be called in the main update loop in order
     * to keep the game state up to date. Currently, the only thing that
     * needs updating is time (to see if enough irl time has passed
     * to decrease the remaining time).
     */
    public void update() {
        this.time.update();
    }

    /**
     * This function is the main way for the GUI to interact and manipulate
     * the game state.
     * It should be called whenever a person tries to interact with a building.
     * It will check if the interaction is legal (there is enough energy and time).
     *
     * @param type The type of the interaction.
     * @return Resource exit condition. If it is successful, returns (Null, IsOk),
     * else it returns the ResourceType and the type of fault (too high / low).
     */
    public ResourceExitConditions interactedWith(ActivityType type) {
        if(type == ActivityType.Sleep) {
            incrementDay();
            return new ResourceExitConditions(null, ExitConditions.IsOk);
        }
        //check if we can do activity
        final ResourceExitConditions energy = this.energy.tryActivityType(type);
        final ResourceExitConditions time = this.time.tryActivityType(type);
        if(energy.getConditions() != ExitConditions.IsOk) return energy;
        if(time.getConditions() != ExitConditions.IsOk) return time;

        //do activity
        this.time.doActivity(type);
        this.energy.doActivity(type);

        //update activity tracking data
        switch (type) {
            case Study:
                this.study_count[this.day] += 1;
                break;
            case Recreation:
                this.relax_count[this.day] += 1;
                break;
            case Food:
                this.meal_count[this.day] += 1;
                break;
            default:
                break;
        }
        return new ResourceExitConditions(null, ExitConditions.IsOk);
    }

    /**
     * Checks whether it's the last day of the game.
     *
     * @return true if it's the last day, false otherwise.
     */
    public boolean isLastDay() {
        return this.day >= (MAX_NUMBER_OF_DAYS - 1);
    }

    /**
     * Checks whether the game has ended. (Intended to be used to indicate a need to change from the play screen).
     *
     * @return true if the game has ended, false otherwise.
     */
    public boolean hasEnded() {
         return isLastDay() && this.time.getMinutesRemaining() == 0;
    }

    /**
     * Changes to the next day.
     * Throws an exception if the maximum number of days is exceeded.
     */
    public void incrementDay() {
        //makes sure that we can't go over the max number
        //of days
        if(this.day > (MAX_NUMBER_OF_DAYS - 1))
            throw new RuntimeException("Attempted to increment day past its max");
        ++this.day;
        this.time.reset();
        this.energy.reset();
    }

    /**
     * Checks whether the player has hit a fail condition
     * Intended to be used after game has ended to determine
     * whether to display a fail or win screen.
     * This should only be called once the game has ended.
     * It will throw an exception if called before.
     *
     * @return true/false based on whether the play has failed
     */
    public boolean hasPlayerFailed() {
        if(!isLastDay())
            throw new RuntimeException("hasPlayerFailed has been called before the game has ended");

        // fail conditions are missed studying for 2 days
        for (final int i : this.meal_count) {
            if(i == 0) return true;
        }
        for (final int i : this.relax_count) {
            if(i == 0) return true;
        }
        int studied_zero_times_per_day_count = 0;
        for (final int i : this.study_count) {
            if(i == 0) ++studied_zero_times_per_day_count;
        }
        if (studied_zero_times_per_day_count >= 2) return true;
        return false;
    }

    /**
     * This function generate the player's score based on their
     * tracked metrics and the value of each metric as specified
     * by their respective constants.
     * This should only be called once the game has ended.
     * It will throw an exception if called before.
     *
     * CHANGELOG : Method now takes array of activityLocations
     * CHANGELOG : Updated all scoring calculations and return score as a percentage from 0-100
     *
     * @return The total score that the player has achieved
     */
    public int generateScore(ActivityLocation[] activityLocations) {
        if(!isLastDay())
            throw new RuntimeException("generateScore has been called before the game has ended");

        /*
         * Studying = 1500 pts
         * Recreation = 1000 pts
         * Eating = 1000 pts
         */
        int max_possible_score = 3500;

        boolean [] streaks = checkStreaks(activityLocations);

        int score = 0;

        /*
         * STUDYING POINTS
         *
         * Studying more than 10 times a week will remove points from the player
         * Bonus for studying every day
         * Bonus for studying across different locations
         *
         * Maximum points:
         * 1000 pts : Studying (up to 10 times)
         * 250 pts : Bonus for studying every day
         * 250 pts : Bonus for studying in different locations
         */

        int times_studied = Arrays.stream(this.study_count).sum();
        int locations_studied = getNumLocationsActivity(activityLocations, ActivityType.Study);
        int days_studied = getNumDaysActivity(this.study_count);

        // POINTS FOR STUDYING
        if (times_studied <= 10){
            score += times_studied * STUDY_SCORE_VALUE;
        } else {
            score += 10 * STUDY_SCORE_VALUE;
            score -= (times_studied-10) * STUDY_SCORE_VALUE/2;
        }

        // BONUS FOR STUDYING EVERYDAY
        if (days_studied == 7){
            score += 250;
        } else if (days_studied == 6){
            if (times_studied > 6){
                score += 250;
            }
        }

        // BONUS FOR STUDYING IN DIFFERENT PLACES
        if (locations_studied > 1){
            score += 250;
        }



        /*
         * RECREATIONAL POINTS.
         *
         * Points rewarded everytime up to 14 times
         * Bonus for doing recreation every day
         * Bonus for doing recreation across different locations
         *
         * Maximum points:
         * 700 pts : Studying (up to 14 times)
         * 150 pts : Bonus for doing recreation every day
         * 150 pts : Bonus for doing recreation in different locations
         */


        int times_rec = Arrays.stream(this.relax_count).sum();
        int days_rec = getNumDaysActivity(this.relax_count);
        int locations_rec = getNumLocationsActivity(activityLocations, ActivityType.Recreation);

        // Points for studying
        score += Math.min(times_rec, 14) * RELAX_SCORE_VALUE;

        // Bonus for everyday
        if (days_rec == 7){
            score += RELAX_SCORE_VALUE * 3;
        }

        // No bonus if recreation at 1 place
        // Bonus of 100 if done at 2 places
        // Bonus of 150 if done at 3 places
        if (locations_rec == 2){
            score += (RELAX_SCORE_VALUE * 2);
        } else if (locations_rec == 3) {
            score += (RELAX_SCORE_VALUE * 3);
        }


        /*
         * EATING POINTS.
         *
         * Points awarded for eating food
         * Bonus for eating 3 times a day
         *
         * Maximum points:
         * 840 pts : Eating 3 times a day = 3*30 + bonus 30
         * 90 pts : Bonus for eating in different locations
         * 70 pts : Bonus for eating everyday
         */


        int times_eaten = Arrays.stream(this.meal_count).sum();
        int locations_eaten = getNumLocationsActivity(activityLocations, ActivityType.Food);
        int days_eaten = getNumDaysActivity(this.meal_count);

        for (int num : this.meal_count){
            switch (num) {
                case 1:
                    score += MEAL_SCORE_VALUE;
                    break;

                case 2:
                    score += MEAL_SCORE_VALUE * 2;
                    break;

                case 3:
                    score += MEAL_SCORE_VALUE * 4;
                    break;

            }
        }

        // No bonus if recreation at 1 place
        // Bonus of 30 if done at 2 places
        // Bonus of 90 if done at 3 places
        if (locations_eaten == 2){
            score += (MEAL_SCORE_VALUE);
        } else if (locations_eaten == 3) {
            score += (MEAL_SCORE_VALUE * 3);
        }

        if (days_eaten == 7){
            score += 70;
        }


//        for (int i = 0; i < 7; ++i) {
//
//            if(this.meal_count[i] == 0)
//                score += MEAL_SCORE_PENALTY;
//            else
//                score += this.meal_count[i]  * MEAL_SCORE_VALUE;
//
//            if(this.relax_count[i] == 0)
//                score += RELAX_SCORE_PENALTY;
//            else
//                score += this.relax_count[i]  * RELAX_SCORE_VALUE;
//
//            if(this.study_count[i] >= TOO_MUCH_STUDY_THRESHOLD)
//                score += STUDY_TOO_MUCH_PENALTY;
//            else
//                score += this.study_count[i]  * STUDY_SCORE_VALUE;
//
//        }

        // CHANGELOG : PREVENT NEGATIVE SCORE
        if (score < 0){
            score = 0;
        }

        // CHANGELOG : MAP SCORE TO A PERCENT OUT OF 100
        int percent_score = (int)(((double)score / max_possible_score) * 100);

        // CHANGELOG : ADD STREAK BONUSES AS EXTRA 10%
        for (boolean streak : streaks){
            if (streak){
                percent_score += 10;
            }
        }

        // CHANGELOG : CAP PERCENTAGE SCORE AT 100
        if (percent_score > 100){
            percent_score = 100;
        }

        return percent_score;
    }

    /**
     * CHANGELOG : NEW METHOD
     *
     * Takes in an array of activities and an Acitivty type and returns then number of different
     * locations that type of activity was done in
     *
     * @param activities Array of ActivityLocations
     * @param type Type of activity to check
     * @return Number of different locations the player did that type of activity
     */
    public int getNumLocationsActivity(ActivityLocation[] activities, ActivityType type){
        int locations = 0;

        for (ActivityLocation activity : activities) {
            if (activity.getType() == type){
                if (Arrays.stream(activity.getInteractions()).sum() > 0){
                    locations ++;}
            }
        }

        return locations;
    }


    /**
     * CHANGELOG : NEW METHOD
     *
     * Takes in the activity counter arrays and returns the number of days it was done on
     *
     * @param list Array of ActivityLocations
     * @return Number of different days the player did that type of activity
     */
    public int getNumDaysActivity(int[] list){
        int days_done = 0;

        for (int num : list){
            if (num > 0){
                days_done ++;}
        }

        return days_done;
    }


    /**
     * CHANGELOG: NEW METHOD
     * This method checks if the user has completed the streak achievements:
     * Athlete for playing football, Bookworm for library, and Clubber for clubbing
     *
     * @param activityLocations List of activity location objects
     * @return Boolean array, True/False for each streak completed
     */
    public boolean[] checkStreaks(ActivityLocation[] activityLocations){
        boolean[] streakArray = new boolean[3];

        for (ActivityLocation activity : activityLocations) {
            // For each activity, check if the streak condition has been met.
            // If so, set the relevant array index to true
            switch (activity.getName()){
                case "play football":
                    if (Arrays.stream(activity.getInteractions()).sum() >= 4){
                        streakArray[0] = true;}
                    break;

                case "study at library":
                    if (Arrays.stream(activity.getInteractions()).sum() >= 4){
                        streakArray[1] = true;}
                    break;

                case "go clubbing":
                    if (Arrays.stream(activity.getInteractions()).sum() >= 4){
                        streakArray[2] = true;}
                    break;

                default:
                    break;
            }
        }

        return streakArray;
    }




    /**
     * Returns the day. NOT zero indexed
     *
     * @return current day
     */
    public int getCurrentDay() {
        return this.day + 1;
    }

    /**
     * Returns the energy limit.
     *
     * @return The energy limit.
     */
    public int getEnergyLimit() {
        return this.energy.getLimit();
    }

    /**
     * Returns the current energy.
     *
     * @return The current energy.
     */
    public int getCurrentEnergy() {
        return this.energy.getCurrentEnergy();
    }

    /**
     * Returns the time limit.
     *
     * @return The time limit.
     */
    public int getTimeLimit() {
        return Time.MINUTES_PER_DAY;
    }

    /**
     * Returns the remaining time.
     *
     * @return The remaining time.
     */
    public int getTimeRemaining() {
        return this.time.getMinutesRemaining();
    }

    /**
     * Returns the number of times studied today.
     *
     * @return The number of times studied today.
     */
    public int getTimesStudiedToday() {
        return study_count[this.day];
    }

    /**
     * Returns the number of times eaten today.
     *
     * @return The number of times eaten today.
     */
    public int getTimesEatenToday() {
        return meal_count[this.day];
    }

    /**
     * Returns the number of recreational activities done today.
     *
     * @return The number of recreational activities done today.
     */
    public int getTimesRelaxedToday() {
        return relax_count[this.day];
    }
  

    /**
     * sets meal count. This is only used for testing
     *
     * @Param meals
     *
     */
    public void setMeal_count(int[] meals){
        if(meals.length == 7) {

            this.meal_count = meals;
        }
        else{

            throw new RuntimeException("meal count must have a length of seven denoting seven days");
        }

    }

    /**
     * gets meal count
     *
     * @return meal count array
     */
    public int[] getMealCount(){return meal_count;}

    /**
     * sets study count. This is only used for testing
     *
     * @param studied
     */
    public void setStudy_count(int[] studied){
        if(studied.length == 7) {

            this.study_count = studied;
        }
        else{

            throw new RuntimeException("study count must have a length of seven denoting seven days");
        }

    }
    public int[] getStudyCount(){return study_count;}

    /**
     * sets relax count. This is only used for testing
     *
     * @param relax
     */
    public void setRelax_count(int[] relax){
        if(relax.length == 7) {

            this.relax_count = relax;
        }
        else{

            throw new RuntimeException("relax count must have a length of seven denoting seven days");
        }
    }
    public int[] getRelaxCount(){return relax_count;}
}
