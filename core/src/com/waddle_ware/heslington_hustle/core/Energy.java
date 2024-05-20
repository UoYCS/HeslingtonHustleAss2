/*
 * CHANGELOG:
 * MINIMAL CHANGES REQUIRED:
 *      Testing
 *          - Updated code and added new method to assist in unit testing
 */

package com.waddle_ware.heslington_hustle.core;

/**
 * Represents the energy resource in the game.
 * This class implements the ResourceBase interface for managing energy related operations.
 */
public class Energy implements ResourceBase {
    // Static constants
    // CHANGELOG: Updated variable accessibility for testing
    static final public int ENERGY_PER_STUDY = -2;
    static final public int ENERGY_PER_RECREATIONAL = -1;
    static final public int ENERGY_PER_FOOD = 1;

    // Member variables.
    final private int limit;
    private int current;

    /**
     * Initialises the energy class with the energy limit specified
     *
     * @param limit The upper limit of the energy that the player can have at any given time
     */
    public Energy(int limit) {
        this.limit = limit;
        this.current = limit;
    }

    /**
     * Gets the current amount of energy.
     *
     * @return The current amount of energy.
     */
    public int getCurrentEnergy() {
        return this.current;
    }

    /**
     * Resets the energy to its initial state, setting it to the limit.
     */
    @Override
    public void reset() {
        this.current = this.limit;
    }

    /**
     * Checks if the specified amount of energy is valid.
     *
     * @param amount The amount of energy to check.
     * @return An ExitConditions value indicating whether the energy level is too low, too high, or ok.
     */
    @Override
    public ExitConditions isOk(int amount) {
        if(amount < 0) return ExitConditions.TooLow;
        if(amount > this.limit) return ExitConditions.TooHigh;
        return ExitConditions.IsOk;
    }

    /**
     * Attempts to perform the specified type of activity with the energy resource.
     *
     * @param type The type of activity to attempt.
     * @return A ResourceExitConditions object indicating whether it is possible to perform the activity.
     */
    @Override
    public ResourceExitConditions tryActivityType(ActivityType type) {
        int cost_of_resource;
        switch(type) {
            case Study:
                cost_of_resource = ENERGY_PER_STUDY;
                break;
            case Recreation:
                cost_of_resource = ENERGY_PER_RECREATIONAL;
                break;
            case Food:
                cost_of_resource = ENERGY_PER_FOOD;
                break;
                //This should never happen
            default:
                cost_of_resource = -99999999;
                break;
        }

        final int potential_state = this.current + cost_of_resource; // future energy amount
        final ExitConditions condition = isOk(potential_state); // checks if enough energy
        return new ResourceExitConditions(ResourceTypes.Energy, condition);
        // ResourceExitConditions has an attribute Resource type,
        // type is either Time or Energy
        // this says whether it's referring to time or energy, this refers to energy as set out in parameter 1
        // condition is exitcondition class
        // exitconditions says if the attempt at doing activity is valid or not
        // i.e. yes (isOK), or too low / too high

        // so basically this returns that an activity uses energy? and whether there is enough energy to do a task
        // the resourcetype attribute isn't actually used in core
        // only bit used by core class is whether it's a valid activity use
    }

    /**
     * Performs the specified type of activity with the energy resource.
     *
     * @param type The type of activity to perform.
     */
    @Override
    public void doActivity(ActivityType type) {
        switch(type) {
            case Study:
                this.current += ENERGY_PER_STUDY;
                return;
            case Recreation:
                this.current += ENERGY_PER_RECREATIONAL;
                return;
            case Food:
                this.current += ENERGY_PER_FOOD;
        }
    }

    /**
     * Gets the upper limit of energy that the player can have at any given time.
     *
     * @return The upper limit of energy.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * CHANGELOG: NEW METHOD
     * Added setter method for testing purposes
     * @param newcurrent New testing energy value.
     */
    public void setcurrent(int newcurrent){this.current = newcurrent;}
}
