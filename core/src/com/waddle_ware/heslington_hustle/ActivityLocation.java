package com.waddle_ware.heslington_hustle;

import com.waddle_ware.heslington_hustle.core.ActivityType;

/**
 * The ActivityLocation class represents a location within the game where activities are.
 * It stores the coordinates of the location, interaction radius, and a name to identify it.
 */

// CHANGELOG: ADDED AN ACTIVITY TYPE VARIABLE FOR USE IN OTHER AREAS TO EASILY ADD MORE ACTIVITIES
public class ActivityLocation {
    private final float x; // The x-coordinate of the activity location
    private final float y; // The y-coordinate of the activity location
    private final float radius; // The radius of the activity location
    private final String name; // The name of the activity location
    private final ActivityType type; // The type of activity (Eat/Sleep/Study/Recreation)

    // CHANGELOG: ADDED THIS VARIABLE TO TRACK ACTIVITIES ON A PER ACTIVITY BASIS
    private int[] interactions = new int[7]; // Number of times this activity was interacted with on each day

    /**
     * Constructs an ActivityLocation object with the specified coordinates, radius, and name.
     *
     * @param x      The x-coordinate of the activities' location.
     * @param y      The y-coordinate of the activities' location.
     * @param radius The interaction radius of the activity.
     * @param name   The name of the activity.
     */
    public ActivityLocation(float x, float y, float radius, String name, ActivityType type) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
        this.type = type;
    }

    /**
     * Retrieves the y-coordinate of the activity location.
     *
     * @return The y-coordinate of the activity location.
     */
    public float getX() {
        return this.x;
    }

    /**
     * Retrieves the y-coordinate of the activity location.
     *
     * @return The y-coordinate of the activity location.
     */
    public float getY() {
        return this.y;
    }

    /**
     * Retrieves the radius of the activity location.
     *
     * @return The radius of the activity location.
     */
    public float getRadius() {
        return this.radius;
    }

    /**
     * Retrieves the name of the activity location.
     *
     * @return The name of the activity location.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the type of the activity location.
     *
     * @return The type of the activity location.
     */
    // CHANGELOG: ADDED GETTER METHOD FOR NEW CLASS VARIABLE
    public ActivityType getType() {
            return type;
    }


    // CHANGELOG: ADDED GETTER/SETTER METHODS FOR NEW VARIABLES

    /**
     * CHANGELOG : ADDED METHOD
     * Retrieves the array of activity interactions
     *
     * @return Array of activity interactions
     */
    public int[] getInteractions(){
        return this.interactions;
    }

    /**
     * CHANGELOG : ADDED METHOD
     * Increments the counter array for a specific day
     * @param day Integer from 0-6 (representing days 1-7)
     */
    public void incrementCounter(int day){
        this.interactions[day]++;
    }
}
