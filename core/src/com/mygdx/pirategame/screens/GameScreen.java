package com.mygdx.pirategame.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.configs.Difficulty;
import com.mygdx.pirategame.entities.Cloud;
import com.mygdx.pirategame.entities.Coin;
import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.entities.Player;
import com.mygdx.pirategame.entities.Powerup;
import com.mygdx.pirategame.entities.SteerableEntity;
import com.mygdx.pirategame.entities.Tornado;
import com.mygdx.pirategame.world.AvailableSpawn;
import com.mygdx.pirategame.world.WorldContactListener;
import com.mygdx.pirategame.world.WorldCreator;

import static com.mygdx.pirategame.configs.Constants.*;


/**
 * Game Screen
 * Class to generate the various screens used to play the game.
 * Instantiates all screen types and displays current screen.
 *
 *@author Ethan Alabaster, Adam Crook, Joe Dickinson, Sam Pearson, Tom Perry, Edward Poulter
 *@version 1.0
 */
public class GameScreen implements Screen {
    private static float maxSpeed = 2.5f;
    private static float accel = 0.05f;
    private float stateTime;

    public static PirateGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private final Stage stage;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    public static Difficulty difficulty;
    private Box2DDebugRenderer b2dr;

    public static Player player;
    public static ArrayList<College> colleges = new ArrayList<>();
    public static ArrayList<SteerableEntity> ships = new ArrayList<>();
    public static ArrayList<Coin> coins = new ArrayList<>();
    public AvailableSpawn invalidSpawn = new AvailableSpawn();
    public HashMap<Integer, ArrayList<Integer>> invalidSpawnClouds = new HashMap<>();
    public Hud hud;
    public static ArrayList<Powerup> powerups = new ArrayList<>();
    public static ArrayList<Cloud> clouds = new ArrayList<>();
    public static ArrayList<Tornado> Tornadoes = new ArrayList<>();

    public static final int GAME_RUNNING = 0;
    public static final int GAME_PAUSED = 1;
    private static int gameStatus;

    private Table pauseTable;
    private Table table;

    public Random rand = new Random();
    public Float TempTime;


    /**
     * Initialises the Game Screen,
     * generates the world data and data for entities that exist upon it,
     * @param game passes game data to current class,
     */
    public GameScreen(PirateGame game, Difficulty difficulty){
        gameStatus = GAME_RUNNING;
        GameScreen.game = game;
        // Setting the difficulty, that will be changed based on the player's choice at the start of the game
        GameScreen.difficulty = difficulty;
        // Initialising camera and extendable viewport for viewing game
        camera = new OrthographicCamera();
        camera.zoom = 0.0155f;
        viewport = new ScreenViewport(camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // Initialize a hud
        hud = new Hud(game.getBatch(), this);

        // Initialising box2d physics
        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();
        player = new Player(this);

        // making the Tiled tmx file render as a map
        maploader = new TmxMapLoader();
        map = maploader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        new WorldCreator(this);

        // Setting up contact listener for collisions
        world.setContactListener(new WorldContactListener(this));

        // Spawning enemy ship and coin. x and y is spawn location
        colleges.add(new College(this, "Alcuin", 3872 / PPM, 4230 / PPM,
                "alcuin_flag.png", "alcuin_ship.png", 0, invalidSpawn));
        colleges.add(new College(this, "Anne Lister", 5855 / PPM, 6470 / PPM,
                "anne_lister_flag.png", "anne_lister_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));
        colleges.add(new College(this, "Constantine", 9055 / PPM, 9860 / PPM,
                "constantine_flag.png", "constantine_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));
        colleges.add(new College(this, "Goodricke", 4128 / PPM, 12936 / PPM,
                "goodricke_flag.png", "goodricke_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));
        colleges.add(new College(this, "Halifax", 12768 / PPM, 14408 / PPM,
                "halifax_flag.png", "halifax_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));
        colleges.add(new College(this, "Langwith", 12576 / PPM, 6920 / PPM,
                "langwith_flag.png", "langwith_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));
        colleges.add(new College(this, "Vanbrugh", 12896 / PPM, 3783 / PPM,
                "vanbrugh_flag.png", "vanbrugh_ship.png", difficulty.getMaxCollegeShips(), invalidSpawn));

        ships = new ArrayList<>();
        ships.addAll(colleges.get(0).fleet);
        ships.addAll(colleges.get(1).fleet);
        ships.addAll(colleges.get(2).fleet);
        ships.addAll(colleges.get(3).fleet);
        ships.addAll(colleges.get(4).fleet);
        ships.addAll(colleges.get(5).fleet);
        ships.addAll(colleges.get(6).fleet);

        //Random ships
        Boolean validLoc;
        int a = 0;
        int b = 0;
        for (int i = 0; i < 20; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                //Check if valid
                validLoc = AvailableSpawn.add(a, b);
            }
            //Add a ship at the random coords
            ships.add(new EnemyShip(this, a, b, "unaligned_ship.png", "Unaligned"));
        }
        TempTime = 0f;
        //Random coins
        coins = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;

                //Check if valid
                validLoc = AvailableSpawn.add(a, b);
            }
            //Add a coins at the random coords
            coins.add(new Coin(this, a, b));
        }

        //Spawn powerups
        powerups = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                validLoc = AvailableSpawn.add(a, b);
            }
            powerups.add(new Powerup(this, a, b, i));
        }

        clouds = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(51-30)+30; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                validLoc = checkGenPosClouds(a, b);
            }
            clouds.add(new Cloud(this, a, b));
        }
        Tornadoes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                validLoc = AvailableSpawn.add(a, b);
            }
            //Add a coins at the random coords
            Tornadoes.add(new Tornado(this, a, b));
        }


        //Setting stage
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Makes this the current screen for the game.
     * Generates the buttons to be able to interact with what screen is being displayed.
     * Creates the escape menu and pause button
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        //GAME BUTTONS
        final TextButton pauseButton = new TextButton("Pause",skin);
        final TextButton shopButton = new TextButton("Shop",skin);


        //PAUSE MENU BUTTONS
        final TextButton start = new TextButton("Resume", skin);
        final TextButton save = new TextButton("Save Game", skin);
        final TextButton skill = new TextButton("Shop", skin);
        final TextButton options = new TextButton("Options", skin);
        TextButton exit = new TextButton("Exit", skin);


        //Create main table and pause tables
        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        pauseTable = new Table();
        pauseTable.setFillParent(true);
        stage.addActor(pauseTable);


        //Set the visability of the tables. Particuarly used when coming back from options or skillTree
        if (gameStatus == GAME_PAUSED){
            table.setVisible(false);
            pauseTable.setVisible(true);
        }
        else{
            pauseTable.setVisible(false);
            table.setVisible(true);
        }

        //ADD TO TABLES
        table.add(pauseButton);
        table.row().pad(10, 0, 10, 0);
        table.add(shopButton);
        table.row().pad(10, 0, 10, 0);
        table.left().top();

        pauseTable.add(start).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(save).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(skill).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(options).fillX().uniformX();
        pauseTable.row().pad(20, 0, 10, 0);
        pauseTable.add(exit).fillX().uniformX();
        pauseTable.center();


        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                table.setVisible(false);
                pauseTable.setVisible(true);
                pause();

            }
        });
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                pauseTable.setVisible(false);
                game.changeScreen(SKILL);
            }
        });
        skill.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                pauseTable.setVisible(false);
                game.changeScreen(SKILL);
            }
        });
        start.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(false);
                table.setVisible(true);
                resume();
            }
        });
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.save();
            }
        }
        );
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(false);
                game.setScreen(new Options(game,game.getScreen()));
            }
        }
        );
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(MENU);
            }
        });
    }

    /**
     * Checks for input and performs an action
     * Applies to keys "W" "A" "S" "D" "E" "Esc"
     *
     * Caps player velocity
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void handleInput(float delta) {
        if (gameStatus == GAME_RUNNING) {

            int angularAcceleration = 0;
            int linearAcceleration = 0;

            // Left physics impulse on 'A'
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                angularAcceleration += 3;
            }
            // Right physics impulse on 'D'
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                angularAcceleration -= 3;
            }
            // Up physics impulse on 'W'
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                linearAcceleration += 20;
            }
            // Down physics impulse on 'S'
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                linearAcceleration -= 10;
            }
            // Cannon fire on 'Spacce'
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                player.fire();
            }

            if (!(Gdx.input.isKeyPressed(Input.Keys.W) | Gdx.input.isKeyPressed(Input.Keys.S))){

                if(player.getVelocity() > 0.1f || player.getVelocity() < -0.1f){ // this is a check so the game doesn't just loop for ever trying to lower the speed down
                    player.slowDown(delta);
                } else{
                    player.setVelocity(0f);
                    player.updateVelocity(0, delta);
                }
            }
            else {
                player.updateVelocity(linearAcceleration, delta);
            }
            player.updateRotation(angularAcceleration, delta);
            // Gdx.app.log("vel", String.valueOf(player.velocity));
            // Checking if player at max velocity, and keeping them below max

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if(gameStatus == GAME_PAUSED) {
                resume();
                table.setVisible(true);
                pauseTable.setVisible(false);
            }
            else {
                table.setVisible(false);
                pauseTable.setVisible(true);
                pause();
            }
        }
    }

    /**
     * Updates the state of each object with delta time
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {
    	stateTime += delta;
        TempTime += delta;

        handleInput(delta);
        // Stepping the physics engine by time of 1 frame
        world.step(1 / 60f, 6, 2);
        GdxAI.getTimepiece().update(delta);
        // Update all players and entities
        player.update(delta);
        for(College college: colleges){
            college.update(delta);
        }

        //Update ships
        for (SteerableEntity ship : ships) {
            ship.update(delta);
        }

        //Updates coin
        for (Coin coin: coins) {
            coin.update();
        }

        //Updates powerups
        for (Powerup powerup: powerups) {
            powerup.update();
        }

        //Gdx.app.log("powerup", String.valueOf(ConstantTime));
        //Add new powerup
        //Gdx.app.log("x", String.valueOf(TempTime));

        if (TempTime >= 29f){
            Boolean validLoc;
            int a = 0;
            int b = 0;
            Gdx.app.log("PowerUps", "Spawn More PowerUps");
            for (int i = 0; i < 5; i++) {
                validLoc = false;
                while (!validLoc) {
                    //Get random x and y coords
                    a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                    b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                    validLoc = AvailableSpawn.add(a, b);
                }
                powerups.add(new Powerup(this, a, b, i));
            }
            TempTime = 0f;
        }
        
        //Updates clouds
        for (int i = 0; i < clouds.size(); i++) {
            clouds.get(i).update(delta);
            if ((player.getX() >= clouds.get(i).getX() - 2 && player.getX() <= clouds.get(i).getX() + 2) && (player.getY() >= clouds.get(i).getY() - 2 && player.getY() <= clouds.get(i).getY() + 2)){
                clouds.get(i).changeAlpha();
            }
            else{
                clouds.get(i).resetAlpha();
            }
        }

        for (int i = 0; i < Tornadoes.size(); i++) {
            Tornado tornado = Tornadoes.get(i);
            tornado.update(delta);
            tornado.tornadoImpulse(player, delta);
        }
        //After a delay check if a college is destroyed. If not, if can fire
        if (stateTime > 1) {
            for(College college: colleges){
                if(!college.destroyed && !(college.getCurrentCollegeName().equals("Alcuin"))){
                    college.fire();
                }
            }
        stateTime = 0;
        }

        hud.update(delta);

        // Centre camera on player boat
        camera.position.x = player.getPosition().x;
        camera.position.y = player.getPosition().y;
        camera.update();
        renderer.setView(camera);
    }

    /**
     * Renders the visual data for all objects
     * Changes and renders new visual data for ships
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    @Override
    public void render(float delta) {
        if (gameStatus == GAME_RUNNING) {
            update(delta);
        }
        else{handleInput(delta);}

        Gdx.gl.glClearColor(46/255f, 204/255f, 113/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        // b2dr is the hitbox shapes, can be commented out to hide
        b2dr.render(world, camera.combined);

        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        // Order determines layering

        //Renders coins
        for(Coin coin: coins) {
            coin.draw(game.getBatch());
        }

        //Renders powerups
        for(Powerup powerup: powerups) {
            powerup.draw(game.getBatch());
        }


        //Renders colleges
        player.draw(game.getBatch());

        for (College college: colleges) {
            college.draw(game.getBatch());
        }

        for(int i = 0; i< Tornadoes.size(); i++) {
            Tornadoes.get(i).draw(game.getBatch());
        }

        //Updates all ships
        for (SteerableEntity ship: ships){
            if (!ship.college.equals("Unaligned")) {
                //Flips a colleges allegence if their college is destroyed
                if (getCollege(ship.college).destroyed) {

                    ship.updateTexture("Alcuin", "alcuin_ship.png");
                }
            }
            ship.draw(game.getBatch());
        }

        // Renders all the clouds on top of eerything else
        for(int i = 0; i <clouds.size(); i++){
            clouds.get(i).draw(game.getBatch());
        }

        game.getBatch().end();
        //player.SlowDownBoat();
        Hud.stage.draw();
        stage.act();
        stage.draw();
        //Checks game over conditions
        gameOverCheck();
    }

    /**
     * Changes the camera size, Scales the hud to match the camera
     *
     * @param width the width of the viewable area
     * @param height the height of the viewable area
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
        Hud.resize(width, height);
        camera.update();
        renderer.setView(camera);
    }

    /**
     * Returns the map
     *
     * @return map : returns the world map
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Returns the world (map and objects)
     *
     * @return world : returns the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Returns the college from the colleges hashmap
     *
     * @param collegeName uses a college name as an index
     * @return college : returns the college fetched from colleges
     */
    public static College getCollege(String collegeName) {
    	for (College college : colleges) {
    		if (college.currentCollege == collegeName) {
                Gdx.app.log("wall", collegeName);
    			return college;
    		}
    	}
        //Gdx.app.log("wall", collegeName);
        return colleges.get(0);
    }

    /**
     * Checks if the game is over
     * i.e. goal reached (all colleges bar "Alcuin" are destroyed)
     */
    public void gameOverCheck(){
        //Lose game if ship on 0 health or Alcuin is destroyed
        //Gdx.app.log("enemy", String.valueOf(Hud.getHealth()));
        if (Hud.getHealth() <= 0 || getCollege("Alcuin").destroyed) {
            game.changeScreen(DEATH);
            game.killGame();
        }
        //Win game if all colleges destroyed
        else if (getCollege("Anne Lister").destroyed && getCollege("Constantine").destroyed && getCollege("Goodricke").destroyed){
            game.changeScreen(VICTORY);
            game.killGame();
        }
    }


    //public void IncreaseMaxSpeedPercent(int num){difficulty.IncreaseMaxSpeedPercent(num); } // num increase
    //public void IncreaseTraversePercent(int num){difficulty.IncreaseTraversePercent(num); }
    //public void IncreaseDamageDealtPercent(int num){difficulty.IncreaseDamageDealtPercent(num);}
    // SetGoldCoinMulti
    //public void DecreaseDamageRecievedPercent(int num){difficulty.DecreaseDamageRecievedPercent(num);}




    // TODO delete
    
    public Player getPlayer() {
    	return player;
    }
    
    /**
     * Fetches the player's current position
     *
     * @return position vector : returns the position of the player
     */
    public Vector2 getPlayerPos(){
        return new Vector2(player.getPosition().x, player.getPosition().y);
    }

    /**
     * Updates acceleration by a given percentage. Accessed by skill tree
     *
     * @param percentage percentage increase
     */
    public static void changeAcceleration(Float percentage){
        accel = accel * (1 + (percentage / 100));
    }

    /**
     * Updates max speed by a given percentage. Accessed by skill tree
     *
     * @param percentage percentage increase
     */
    public static void changeMaxSpeed(Float percentage){
        maxSpeed = maxSpeed * (1 +(percentage/100));
    }

    /**
     * Changes the amount of damage done by each hit. Accessed by skill tree
     *
     * @param value damage dealt
     */
    public static void changeDamage(int value){

        for (int i = 0; i < ships.size(); i++){
            ships.get(i).changeDamageReceived(value);
        }
        getCollege("Anne Lister").changeDamageReceived(value);
        getCollege("Constantine").changeDamageReceived(value);
        getCollege("Goodricke").changeDamageReceived(value);
        getCollege("Halifax").changeDamageReceived(value);
        getCollege("Langwith").changeDamageReceived(value);
        getCollege("Vanbrugh").changeDamageReceived(value);

    }

    public void SetGoldCoinMulti(int num){
        GameScreen.difficulty.SetGoldCoinMulti(num);
    }

    // ----------------------------------



    /**
     * Test if a cloud has already been spawned near these coordinates
     * @param x random x value
     * @param y random y value
     */
    private Boolean checkGenPosClouds(int x, int y){
        if (invalidSpawnClouds.containsKey(x)){
            ArrayList<Integer> yTest = invalidSpawnClouds.get(x);
            if (yTest.contains(y)){
                return false;
            }
        }
        for (int i = -1; i < 2; i++){
            if(invalidSpawnClouds.containsKey(x+i)){
                invalidSpawnClouds.get(x+i).addAll(new ArrayList<>(Arrays.asList(y-1, y, y+1)));
            }
            else{
                invalidSpawnClouds.put(x+i, new ArrayList<>(Arrays.asList(y-1, y, y+1)));
            }
        }
        return true;
    }

    /**
     * Pauses game
     */
    @Override
    public void pause() {
        gameStatus = GAME_PAUSED;
    }

    /**
     * Resumes game
     */
    @Override
    public void resume() {
        gameStatus = GAME_RUNNING;
    }

    /**
     * (Not Used)
     * Hides game
     */
    @Override
    public void hide() {

    }

    /**
     * Disposes game data
     */
    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        stage.dispose();
    }
    
    public static ArrayList<SteerableEntity> getShips() {
    	return ships;
    }
    
    public static ArrayList<Coin> getCoins() {
    	return coins;
    }
}
