package com.waddle_ware.heslington_hustle.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.waddle_ware.heslington_hustle.ActivityLocation;
import com.waddle_ware.heslington_hustle.Avatar;
import com.waddle_ware.heslington_hustle.HUD;
import com.waddle_ware.heslington_hustle.HeslingtonHustle;
import com.waddle_ware.heslington_hustle.InteractionPopup;
import com.waddle_ware.heslington_hustle.core.ActivityType;
import com.waddle_ware.heslington_hustle.core.Core;
import com.waddle_ware.heslington_hustle.core.ExitConditions;
import com.waddle_ware.heslington_hustle.core.ResourceExitConditions;

import java.util.Arrays;

/**
 * The PlayScreen class represents the games screen where the gameplay is.
 * It implements the Screen interface and manages rendering and input handling.
 */
public class PlayScreen implements Screen {
    private OrthographicCamera camera;
    private Viewport viewport;
    private TiledMap tile_map;
    private OrthogonalTiledMapRenderer map_renderer;
    private boolean is_fullscreen = false;  // Track fullscreen state
    private int current_map_section = 1;
    private final int map_section_offset = 48*16;

    private int frames_since_int = 0;


    public Avatar player;
    private float world_width;
    private float world_height;
    private HUD hud;
    private final HeslingtonHustle game;

    public static final String ICON_ANIMATION_ASSET = "iconAnimations.png";
    public static final String TILEMAP_ASSET = "MapToCode/map.tmx";

    public Core core;


    // CHANGELOG: ADDED ACTIVITIES TO ARRAY INSTEAD OF USING VARIABLES TO ALLOW FOR EXTRA ACTIVITIES

    // Define activity locations array
    public final ActivityLocation[] activityLocations = {

            // SLEEPING at Goodricke
            new ActivityLocation(1786, 264, 20, "sleep", ActivityType.Sleep),

            // STUDYING at Library
            new ActivityLocation(1136, 258, 20, "study at library", ActivityType.Study),

            // STUDYING at CS building
            new ActivityLocation(1664, 24, 20, "study", ActivityType.Study),

            // RECREATION at Duck pond
            new ActivityLocation(2031, 144, 20, "feed the ducks", ActivityType.Recreation),

            // RECREATION at Sports Centre
            new ActivityLocation(970, 125, 20, "play football", ActivityType.Recreation),

            // RECREATION in town
            new ActivityLocation(136, 280, 20, "go clubbing", ActivityType.Recreation),

            // EATING at Piazza
            new ActivityLocation(2104, 264, 20, "eat", ActivityType.Food),

            // EATING at Courtyard
            new ActivityLocation(1288, 55, 20, "eat", ActivityType.Food),

            // EATING in town
            new ActivityLocation(633, 260, 20, "eat", ActivityType.Food)


    };


    private InteractionPopup interaction_popup; // Add a field for the interaction pop-up
    private float popupX;
    private float popupY;
    private int interacted;
    private String reason;


    //  CHANGELOG: variables for the activity icon animations
    Animation<TextureRegion> sleepIcon;
    Animation<TextureRegion> eatIcon;
    Animation<TextureRegion> studyIcon;
    Animation<TextureRegion> feedDucksIcon;
    Animation<TextureRegion> playFootballIcon;
    Animation<TextureRegion> drinkIcon;
    Texture iconSpriteSheet = new Texture(Gdx.files.internal(ICON_ANIMATION_ASSET));
    float stateTime;


    /**
     * Constructs a new PlayScreen.
     *
     * @param game The game instance.
     */
    public PlayScreen(HeslingtonHustle game)
    {
        this.game = game;


        // CHANGELOG: Initialise TextureRegions/Frames for the Activity Icons
        TextureRegion[][] tmp = TextureRegion.split(iconSpriteSheet, iconSpriteSheet.getWidth() / 6, iconSpriteSheet.getHeight()/6);
        TextureRegion[] sleepFrames = new TextureRegion[6];
        TextureRegion[] eatFrames = new TextureRegion[6];
        TextureRegion[] studyFrames = new TextureRegion[6];
        TextureRegion[] duckFrames = new TextureRegion[6];
        TextureRegion[] footballFrames = new TextureRegion[6];
        TextureRegion[] drinkFrames = new TextureRegion[6];

        for (int i = 0; i < 6; i++) {
            sleepFrames[i] = tmp[0][i];
            eatFrames[i] = tmp[1][i];
            studyFrames[i] = tmp[2][i];
            duckFrames[i] = tmp[3][i];
            footballFrames[i] = tmp[4][i];
            drinkFrames[i] = tmp[5][i];
        }
        float frameRate = 0.08f;

        sleepIcon = new Animation<TextureRegion>(frameRate, sleepFrames);
        eatIcon = new Animation<TextureRegion>(frameRate, eatFrames);
        studyIcon = new Animation<TextureRegion>(frameRate, studyFrames);
        feedDucksIcon = new Animation<TextureRegion>(frameRate, duckFrames);
        playFootballIcon = new Animation<TextureRegion>(frameRate, footballFrames);
        drinkIcon = new Animation<TextureRegion>(frameRate, drinkFrames);

        stateTime = 0f;
    }

    /**
     * Called when this screen becomes the current screen.
     * Initialises camera, viewport, tile map, and player avatar.
     */
    @Override
    public void show() {
        // Create camera and viewport
        this.camera = new OrthographicCamera();

        this.core = new Core();
        // Load tile Map
        this.tile_map = new TmxMapLoader().load(TILEMAP_ASSET); // load tile map
        this.map_renderer = new OrthogonalTiledMapRenderer(this.tile_map);

        // Set target aspect ratio for tile map
        float target_aspect_ratio = 16f / 9f;

        // Calculate world dimensions
        final int map_tile_width = this.tile_map.getProperties().get("width", Integer.class);
        final int map_tile_height = this.tile_map.getProperties().get("height", Integer.class);

        final int tile_size = this.tile_map.getProperties().get("tilewidth", Integer.class);

        this.world_width = map_tile_width * tile_size;
        this.world_height = map_tile_height * tile_size;


        
        this.player = new Avatar(0, 0, this.world_height, this.world_width);
        this.player.setPlayerLoc(260+ (2*48*16), 250);

        // Set the viewport to use the whole screen with the desired aspect ratio
        this.viewport = new FitViewport(this.world_width/3, this.world_height, this.camera);
        this.hud = new HUD(this.core);

        // Center the camera on the tile map


        this.camera.position.set((this.world_width / 2f)+map_section_offset, this.world_height / 2f, 0);
        this.camera.update();

        // Adjust the viewport if needed to ensure the tile map fills the entire screen (for tile maps that are not 16:9)
        float aspect_ratio = (float) Gdx.graphics.getHeight() / Gdx.graphics.getWidth();
        if (aspect_ratio > target_aspect_ratio) {
            final float new_world_height = this.world_width * aspect_ratio;
            final float y_offset = (new_world_height - this.world_height) / 2f;
            this.viewport.setWorldSize(this.world_width, new_world_height);
            this.camera.position.add(0, y_offset, 0);
            this.camera.update();
        }



    }

    /**
     * Called when screen should render itself.
     * Handles input, updates the camera, renders the tile map, and renders the player sprite on top.
     *
     * @param delta time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        if(this.core.hasEnded()) {
            // CHANGELOG : EndScreen now takes streakArray parameter
            this.game.setScreen(new EndScreen(this.game, !this.core.hasPlayerFailed(), this.core.generateScore(activityLocations), this.core.checkStreaks(activityLocations)));
        }
        handleInput(); // Call method to handle inputs
        this.player.handleInput();


        current_map_section = getGameArea(this.player.getPlayerX());

        this.camera.position.set(
                (this.world_width / 2f) + (getGameArea(this.player.getPlayerX()) * map_section_offset),
                this.world_height / 2f,
                0);

        // Update camera and viewport
        this.camera.update();
        this.map_renderer.setView(this.camera);
        this.hud.update(this.core, current_map_section);
        this.player.update(this.tile_map);
        this.core.update();

        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        this.map_renderer.render(); // Render tile map


        // Render player sprite
        this.map_renderer.getBatch().begin();
        this.player.render(this.map_renderer);// Draw sprite in updated position with specified dimensions

        // ADDING FLOATING ICONS FOR ACTIVITIES
        drawActivityIcons();

        this.hud.render(this.map_renderer.getBatch());

        this.map_renderer.getBatch().end();


        checkInteractionProximity(); // Check for proximity and update interaction pop-ups


        // Render the pop-up message if it exists
        if (this.interaction_popup != null) {
            this.interaction_popup.render(this.map_renderer.getBatch(), this.popupX, this.popupY); // Adjust popupX and popupY as needed
        }
    }

    /** Called when the window is resized.
     * Updates the viewport and ensures the sprite stays within the new window boundaries.
     * @param width  the new width in pixels.
     * @param height the new height in pixels.
     */
    @Override
    public void resize(int width, int height) {
        // Update viewport when the window is resized
        this.viewport.update(width, height);
    }

    /**
     * CHANGELOG: ADDED NEW METHOD.
     * Method used to draw floating activity icons to the map.
     */
    private void drawActivityIcons(){
        // Update the games state time
        stateTime += Gdx.graphics.getDeltaTime();

        for (ActivityLocation activity : activityLocations) {
            // For each activity, check if it is in the current map section
            if (getGameArea(activity.getX()) == current_map_section){

                // If it is, animate its icon depending on the activity type/name

                switch (activity.getType()){
                    case Study:
                        iconAnimate(studyIcon, activity.getX(), activity.getY()+50);
                        break;

                    case Sleep:
                        iconAnimate(sleepIcon, activity.getX(), activity.getY()+30);
                        break;

                    case Recreation:
                        switch (activity.getName()) {
                            case "feed the ducks":
                                iconAnimate(feedDucksIcon, activity.getX(), activity.getY());
                                break;
                            case "play football":
                                iconAnimate(playFootballIcon, activity.getX(), activity.getY());
                                break;
                            case "go clubbing":
                                iconAnimate(drinkIcon, activity.getX(), activity.getY()+50);}
                        break;

                    case Food:
                        iconAnimate(eatIcon, activity.getX(), activity.getY()+40);
                        break;
                }
            }
        }
    }


    /**
     * CHANGELOG: ADDED NEW METHOD.
     * Method used to animate activity icons.
     *
     * @param icon Icon to be animated.
     * @param x x-coordinate of icon animation.
     * @param y y-coordinate of icon animation.
     */
    private void iconAnimate(Animation<TextureRegion> icon, float x, float y){

        // Get the current frame of animation required
        TextureRegion currentFrame = icon.getKeyFrame(stateTime, true);

        // Draw this frame at the activities position
        this.map_renderer.getBatch().draw(currentFrame, x ,y);
    }

    /**
     * CHANGELOG: ADDED NEW METHOD
     * This method takes an x coordinate and returns which location of the map it is in.
     *
     * @param xValue X Coordinate to check
     * @return -1 for left map (town), 0 for centre map (west), 1 for right map (east)
     */
    public int getGameArea(float xValue) {
        if (xValue > 1523) {
            return 1;
        } else if (xValue <= 1523 && xValue > 755) {
            return 0;
        } else {
            return -1;
        }
    }



    /**
     * Handles user input to move the sprite with wasd keys.
     * Checks boundaries to prevent the sprite from moving outside the game window.
     */
    private void handleInput() {
        //Used for testing
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.S)) {
//            this.core.interactedWith(ActivityType.Study);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.F)) {
//            this.core.interactedWith(ActivityType.Food);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.E)) {
//            this.core.interactedWith(ActivityType.Sleep);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.R)) {
//            this.core.interactedWith(ActivityType.Recreation);
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.W)) {
//            this.game.setScreen(new EndScreen(game, true, 2342));
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.L)) {
//            this.game.setScreen(new EndScreen(game, false, 7613));
//        }

        // Toggle fullscreen when F11 is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            toggleFullscreen();
        }
        // Interact when "E" is pressed
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            handleInteraction();
        }

        // TEMPORARY FOR TESTING
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            System.out.println(this.player.getPlayerX());
            System.out.println(this.player.getPlayerY());
        }

        // TEMPORARY FOR TESTING
        if (Gdx.input.isKeyJustPressed(Input.Keys.Z)) {
            System.out.println("----------");
            for (ActivityLocation activity : activityLocations) {
                System.out.println(Arrays.toString(activity.getInteractions()) + ", " + activity.getName());
            }
            System.out.println("----------");

        }


    }

    /**
     * Handles player interaction with various activity locations based on their proximity to the player's position.
     * Checks if the player is within the interaction area of each activity location
     * If the player is within range, trigger the interaction with the activity and handle any outcome.
     */
    private void handleInteraction() {
        // Get players current position
        final float playerX = this.player.getPlayerX();
        final float playerY = this.player.getPlayerY();
        ResourceExitConditions exit_value;



        // CHANGELOG: CHANGED THIS FUNCTION TO USE A LOOP TO ALLOW FOR EXTRA ACTIVITIES
        // CHANGELOG: ADDED LINE TO INCREMENT COUNTER FOR SPECIFIC ACTIVITY
        // TODO: Change int increment to array format
        for (ActivityLocation activity : activityLocations){

            if (isPlayerWithinInteractionArea(playerX, playerY, activity)){
                frames_since_int = 0;
                if (activity.getType() == ActivityType.Study || activity.getType() == ActivityType.Recreation){
                    exit_value = this.core.interactedWith(activity.getType());
                    if (exit_value.getConditions() == ExitConditions.IsOk){
                        activity.incrementCounter(this.core.getCurrentDay() - 1);
                        interacted = 1;
                        return;
                    }
                    interacted = -1;
                    reason = String.format("%s %s\n", exit_value.getTypes().toString(), exit_value.getConditions().toString().substring(4));
                }

                if (activity.getType() == ActivityType.Food) {
                    exit_value = this.core.interactedWith(ActivityType.Food);
                    if (exit_value.getConditions() == ExitConditions.IsOk){
                        activity.incrementCounter(this.core.getCurrentDay() - 1);
                        interacted = 1;
                    }
                    else{
                        interacted = -1;
                        reason = String.format("%s %s\n", exit_value.getTypes().toString(), exit_value.getConditions().toString().substring(4));
                    }
                    return;
                }

                if (activity.getType() == ActivityType.Sleep && interacted == 0) {
                    if(this.core.isLastDay()) {
                        // CHANGELOG : EndScreen now takes streakArray parameter
                        game.setScreen(new EndScreen(this.game, !this.core.hasPlayerFailed(), this.core.generateScore(activityLocations), this.core.checkStreaks(activityLocations)));
                    }
                    else this.core.interactedWith(ActivityType.Sleep);
                    interacted = 1;
                }



            }
        }


//        // Check for interaction with each activity location
//        if (isPlayerWithinInteractionArea(playerX, playerY, study_location)) {
//            final ResourceExitConditions exit_value = this.core.interactedWith(ActivityType.Study);
//            if(exit_value.getConditions() == ExitConditions.IsOk)
//                return;
//            System.out.printf("%s%s\n",exit_value.getTypes().toString(),exit_value.getConditions().toString());
//        }
//
//        if (isPlayerWithinInteractionArea(playerX, playerY, recreation_location)) {
//            final ResourceExitConditions exit_value = this.core.interactedWith(ActivityType.Recreation);
//            if(exit_value.getConditions() == ExitConditions.IsOk)
//                return;
//            System.out.printf("%s%s\n",exit_value.getTypes().toString(),exit_value.getConditions().toString());
//        }
//

//        if (isPlayerWithinInteractionArea(playerX, playerY, food_location)) { // Food and sleep should not be able to fail, so they can remain unchecked
//            this.core.interactedWith(ActivityType.Food);
//            return;
//        }
//
//
//        if (isPlayerWithinInteractionArea(playerX, playerY, sleep_location)) {
//            if(this.core.isLastDay()) {
//                game.setScreen(new EndScreen(this.game, !this.core.hasPlayerFailed(), this.core.generateScore()));
//            }
//            else this.core.interactedWith(ActivityType.Sleep);
//        }


    }

    /**
     * Checks the proximity of the player to various activity locations and updates the interaction pop-up message accordingly.
     * If the player is within range of an activity location, an interaction pop-up message is displayed.
     * If the player is not within range of any activity location, the interaction pop-up is hidden.
     */
    private void checkInteractionProximity() {
        // Get players current position
        final float playerX = this.player.getPlayerX();
        final float playerY = this.player.getPlayerY();

        boolean near_activity = false;

        // set pop up above players location
        this.popupX = playerX;
        this.popupY = playerY + 50;
        frames_since_int += 1;

        // CHANGELOG: CHANGED THIS FUNCTION TO USE A LOOP TO ALLOW FOR EXTRA ACTIVITIES
        for (ActivityLocation activity : activityLocations){
            if (isPlayerWithinInteractionArea(playerX, playerY, activity)){
                String colour = "white";
                String message = "Press E to "+ activity.getName();
                if(interacted == 1 && frames_since_int < 60){
                    colour = "green";
                }
                else if (interacted == -1 && frames_since_int < 60){
                    colour = "red";
                    if (reason.equals("Time too low\n")){
                        message = "Not enough time";
                    }
                    else {
                        message = reason;
                    }
                }
                else{
                    interacted = 0;
                }

                this.interaction_popup = new InteractionPopup(message, colour);
                near_activity = true;
            }
        }

        if (!near_activity){
            this.interaction_popup = null;
            interacted = 0;
        }




//        // Check if the player is within range of an activity location
//        if (isPlayerWithinInteractionArea(playerX, playerY, this.study_location)) {
//            this.interaction_popup = new InteractionPopup("Press E to "+ this.study_location.getName());
//            // set pop up above players location
//            this.popupX = playerX;
//            this.popupY = playerY + 50;
//        } else if (isPlayerWithinInteractionArea(playerX, playerY, this.recreation_location)) {
//            this.interaction_popup = new InteractionPopup("Press E to "+ this.recreation_location.getName());
//            this.popupX = playerX;
//            this.popupY = playerY + 50;
//        } else if (isPlayerWithinInteractionArea(playerX, playerY, this.food_location)) {
//            this.interaction_popup = new InteractionPopup("Press E to "+ this.food_location.getName());
//            this.popupX = playerX;
//            this.popupY = playerY + 50;
//        } else if (isPlayerWithinInteractionArea(playerX, playerY, this.sleep_location)) {
//            this.interaction_popup = new InteractionPopup("Press E to "+ this.sleep_location.getName());
//            this.popupX = playerX;
//            this.popupY = playerY + 50;
//        } else {
//            // Hide message if the player is out of range
//            this.interaction_popup = null;
//        }
    }

    /**
     * Toggles fullscreen mode of the application.
     * If the application is currently in fullscreen mode, it switches to windowed mode with the dimensions of the game world.
     * If the application is in windowed mode, it switches to fullscreen mode using the current display mode.
     */
    private void toggleFullscreen() {
        this.is_fullscreen = !this.is_fullscreen;

        if (this.is_fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            Gdx.graphics.setWindowedMode((int) this.world_width/3, (int) this.world_height);
        }
    }

    /**
     * Checks if the player is within the interaction area of a given activity location.
     *
     * @param playerX   The x-coordinate of the player's position.
     * @param playerY   The y-coordinate of the player's position.
     * @param location  The activity location to check for interaction area.
     * @return True if the player is within the interaction area of the activity location, false otherwise.
     */
    public boolean isPlayerWithinInteractionArea(float playerX, float playerY, ActivityLocation location) {
        // Calculate the squared distance between the player and the activity location
        float distance_squared = (playerX - location.getX()) * (playerX - location.getX())
                + (playerY - location.getY()) * (playerY - location.getY());
        // Compare squared distance with the square of the interaction radius of the location
        return distance_squared <= location.getRadius() * location.getRadius();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    /** Called when the application is destroyed. Preceded by a call to {@link #pause()}. */
    @Override
    public void dispose() {
        this.tile_map.dispose();
        this.map_renderer.dispose();
        this.player.dispose();
        this.hud.dispose();
        this.interaction_popup.dispose();

        // CHANGELOG: Need to Dispose of activity icon sprites.
        this.iconSpriteSheet.dispose();
    }

    public float getWorldHeight(){return this.world_height;}
    public float getWorldWidth(){return this.world_width;}
}
