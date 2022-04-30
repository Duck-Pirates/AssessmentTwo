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
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
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

    private static PirateGame game;
    private OrthographicCamera camera;
    private Viewport viewport;
    private final Stage stage;

    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private World world;
    private static Difficulty difficulty;
    private Box2DDebugRenderer b2dr;

    private static ArrayList<College> colleges = new ArrayList<>();
    private static ArrayList<SteerableEntity> ships = new ArrayList<>();
    private static ArrayList<Coin> coins = new ArrayList<>();
    private AvailableSpawn invalidSpawn = new AvailableSpawn();
    private HashMap<Integer, ArrayList<Integer>> invalidSpawnClouds = new HashMap<>();
    private Hud hud;
    private static ArrayList<Powerup> powerups = new ArrayList<>();
    private static ArrayList<Cloud> clouds = new ArrayList<>();
    private static ArrayList<Tornado> Tornadoes = new ArrayList<>();

    private static final int GAME_RUNNING = 0;
    private static final int GAME_PAUSED = 1;
    private static int gameStatus;

    private Table pauseTable;
    private Table table;

    private Random rand = new Random();
    private Float TempTime;


    /**
     * Initialises the Game Screen,
     * generates the world data and data for entities that exist upon it,
     * @param game passes game data to current class,
     */
    public GameScreen(PirateGame game, Difficulty difficulty){
        gameStatus = GAME_RUNNING;
        GameScreen.setGame(game);
        // Setting the difficulty, that will be changed based on the getPlayer()'s choice at the start of the game
        GameScreen.difficulty = difficulty;
        // Initialising camera and extendable viewport for viewing game
        camera = new OrthographicCamera();
        camera.zoom = 0.03f;
        viewport = new ScreenViewport(camera);
        camera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        // Initialize a hud
        hud = new Hud(game.getBatch(), this);

        // Initialising box2d physics
        world = new World(new Vector2(0,0), true);
        b2dr = new Box2DDebugRenderer();

        // making the Tiled tmx file render as a map
        maploader = new TmxMapLoader();
        map = maploader.load("map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
        new WorldCreator(this);

        // Setting up contact listener for collisions
        world.setContactListener(new WorldContactListener(this));

        // Spawning enemy ship and coin. x and y is spawn location
        colleges.add(new College(this, "alcuin", 3872 / PPM, 4230 / PPM, 0, getInvalidSpawn()));
        colleges.add(new College(this, "anne_lister", 5855 / PPM, 6470 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));
        colleges.add(new College(this, "constantine", 9055 / PPM, 9860 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));
        colleges.add(new College(this, "goodricke", 4128 / PPM, 12936 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));
        colleges.add(new College(this, "halifax", 12768 / PPM, 14408 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));
        colleges.add(new College(this, "langwith", 12576 / PPM, 6920 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));
        colleges.add(new College(this, "vanbrugh", 12896 / PPM, 3783 / PPM, difficulty.getMaxCollegeShips(), getInvalidSpawn()));

        ships = new ArrayList<>();
        ships.add(new Player(this));
        ships.addAll(colleges.get(0).getFleet());
        ships.addAll(colleges.get(1).getFleet());
        ships.addAll(colleges.get(2).getFleet());
        ships.addAll(colleges.get(3).getFleet());
        ships.addAll(colleges.get(4).getFleet());
        ships.addAll(colleges.get(5).getFleet());
        ships.addAll(colleges.get(6).getFleet());

        //Random ships
        Boolean validLoc;
        int a = 0;
        int b = 0;
        for (int i = 0; i < 20; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(xCap - xBase) + xBase;
                b = rand.nextInt(yCap - yBase) + yBase;
                //Check if valid
                validLoc = AvailableSpawn.add(a, b);
            }
            //Add a ship at the random coords
            getShips().add(new EnemyShip(this, a, b, "unaligned"));
        }
        setTempTime(0f);
        //Random coins
        coins = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(xCap - xBase) + xBase;
                b = rand.nextInt(yCap - yBase) + yBase;

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
                a = rand.nextInt(xCap - xBase) + xBase;
                b = rand.nextInt(yCap - yBase) + yBase;
                validLoc = AvailableSpawn.add(a, b);
            }
            powerups.add(new Powerup(this, a, b, i));
        }

        clouds = new ArrayList<>();
        for (int i = 0; i < rand.nextInt(51-30)+30; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(xCap - xBase) + xBase;
                b = rand.nextInt(yCap - yBase) + yBase;
                validLoc = checkGenPosClouds(a, b);
            }
            clouds.add(new Cloud(this, a, b));
        }
        Tornadoes = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            validLoc = false;
            while (!validLoc) {
                //Get random x and y coords
                a = rand.nextInt(xCap - xBase) + xBase;
                b = rand.nextInt(yCap - yBase) + yBase;
                validLoc = AvailableSpawn.add(a, b);
            }
            //Add a coins at the random coords
            Tornadoes.add(new Tornado(this, a, b, rand.nextInt(257-128)+128));
        }


        //Setting stage
        stage = new Stage(new ScreenViewport());
    }

    /**
     * Updates the state of each object with delta time
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void update(float delta) {
    	// Stepping the physics engine by time of 1 frame
        world.step(1 / 60f, 6, 2);
        GdxAI.getTimepiece().update(delta);

    	stateTime += delta;
        setTempTime(getTempTime() + delta);

        handleInput();
        for(College college: getColleges()){
        	if(!college.isDestroyed())
        		college.update(delta);
        }

        //Update ships
        for (SteerableEntity ship : getShips()) {
        	if(!ship.isDestroyed())
        		ship.update(delta);
        }

        //Updates coin
        for (Coin coin: getCoins()) {
        	if(!coin.isDestroyed())
        		coin.update();
        }

        //Updates powerups
        for (Powerup powerup: getPowerups()) {
        	if(!powerup.isDestroyed())
        		powerup.update();
        }

        //Gdx.app.log("powerup", String.valueOf(ConstantTime));
        //Add new powerup
        //Gdx.app.log("x", String.valueOf(TempTime));

        if (getTempTime() >= 29f){
            Boolean validLoc;
            int a = 0;
            int b = 0;
            for (int i = 0; i < 5; i++) {
                validLoc = false;
                while (!validLoc) {
                    //Get random x and y coords
                    a = rand.nextInt(AvailableSpawn.xCap - AvailableSpawn.xBase) + AvailableSpawn.xBase;
                    b = rand.nextInt(AvailableSpawn.yCap - AvailableSpawn.yBase) + AvailableSpawn.yBase;
                    validLoc = AvailableSpawn.add(a, b);
                }
                getPowerups().add(new Powerup(this, a, b, i));
            }
            setTempTime(0f);
        }

        //Updates clouds
        for (int i = 0; i < clouds.size(); i++) {
            clouds.get(i).update(delta);
            if ((getPlayer().getX() >= clouds.get(i).getX() - 2 && getPlayer().getX() <= clouds.get(i).getX() + 2) && (getPlayer().getY() >= clouds.get(i).getY() - 2 && getPlayer().getY() <= clouds.get(i).getY() + 2)){
                clouds.get(i).changeAlpha();
            }
            else{
                clouds.get(i).resetAlpha();
            }
        }

        for (int i = 0; i < Tornadoes.size(); i++) {
            Tornado tornado = Tornadoes.get(i);
            tornado.update(delta);
            tornado.tornadoImpulse(getPlayer(), delta);
        }
        //After a delay check if a college is destroyed. If not, it can fire
        if (stateTime > 1) {
            for(College college: getColleges()){
                if(!college.isDestroyed() && !(college.getCollege().equals("alcuin"))){
                    college.fire();
                }
            }
        stateTime = 0;
        }

        getHud().update(delta);

        // Centre camera on player boat
        camera.position.x = getPlayer().getPosition().x;
        camera.position.y = getPlayer().getPosition().y;
        camera.update();
        renderer.setView(camera);
    }

    /**
     * Checks for input and performs an action
     * Applies to keys "W" "A" "S" "D" "E" "Esc"
     *
     * Caps player velocity
     *
     * @param delta Delta time (elapsed time since last game tick)
     */
    public void handleInput() {
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
        else{ handleInput(); }

        Gdx.gl.glClearColor(46/255f, 204/255f, 113/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        // b2dr is the hitbox shapes, can be commented out to hide
        b2dr.render(world, camera.combined);

        getGame().getBatch().setProjectionMatrix(camera.combined);
        getGame().getBatch().begin();
        // Order determines layering

        //Renders coins
        for(Coin coin: getCoins()) {
            coin.draw(getGame().getBatch());
        }

        //Renders powerups
        for(Powerup powerup: getPowerups()) {
            powerup.draw(getGame().getBatch());
        }


        //Renders colleges
        getPlayer().draw(getGame().getBatch());

        for (College college: getColleges()) {
            college.draw(getGame().getBatch());
        }

        for(int i = 0; i< Tornadoes.size(); i++) {
            Tornadoes.get(i).draw(getGame().getBatch());
        }

        //Updates all ships
        for (SteerableEntity ship: getShips()){
            if (!ship.getCollege().equals("unaligned")) {
                //Flips a colleges allegence if their college is destroyed
                if (getCollege(ship.getCollege()).isDestroyed()) {
                    ship.updateTexture("alcuin", "alcuin_ship.png");
                }
            }
            ship.draw(getGame().getBatch());
        }

        // Renders all the clouds on top of eerything else
        for(int i = 0; i <clouds.size(); i++){
            clouds.get(i).draw(getGame().getBatch());
        }

        getGame().getBatch().end();
        Hud.stage.draw();
        stage.act();
        stage.draw();
        //Checks game over conditions
        gameOverCheck();
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
                getGame().changeScreen(SKILL, true);
            }
        });
        skill.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor){
                pauseTable.setVisible(false);
                getGame().changeScreen(SKILL, true);
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
                getGame().save();
            }
        }
        );
        options.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                pauseTable.setVisible(false);
                getGame().setScreen(new Options(getGame(),getGame().getScreen()));
            }
        }
        );
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
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
     * Destroys all the bodies in world
     */
    public void destroyBodies(){
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body:
                bodies) {
            if(!world.isLocked())
                world.destroyBody(body);
        }
    }

    /**
     * Returns the college from the colleges hashmap
     *
     * @param collegeName uses a college name as an index
     * @return college : returns the college fetched from colleges
     */
    public static College getCollege(String collegeName) {
    	for (College college : getColleges()) {
    		if (college.getCollege() == collegeName) {
    			return college;
    		}
    	}
        return getColleges().get(0);
    }

    /**
     * Checks if the game is over
     * i.e. goal reached (all colleges bar "Alcuin" are destroyed)
     */
    public void gameOverCheck(){
        //Lose game if ship on 0 health or Alcuin is destroyed
        if (Hud.getHealth() <= 0 || getColleges().get(0).getHealth() <= 0) {
            getGame().changeScreen(DEATH, true);
            getGame().killGame();
        }
        //Win game if all colleges destroyed
        else if (getColleges().size() == 1 && getColleges().get(0).getCollege() == "alcuin"){
            getGame().changeScreen(VICTORY, true);
            getGame().killGame();
        }
    }


    //public void IncreaseMaxSpeedPercent(int num){difficulty.IncreaseMaxSpeedPercent(num); } // num increase
    //public void IncreaseTraversePercent(int num){difficulty.IncreaseTraversePercent(num); }
    //public void IncreaseDamageDealtPercent(int num){difficulty.IncreaseDamageDealtPercent(num);}
    // SetGoldCoinMulti
    //public void DecreaseDamageRecievedPercent(int num){difficulty.DecreaseDamageRecievedPercent(num);}




    // TODO delete
    
    public static Player getPlayer() {
    	return (Player) getShips().get(0);
    }

    public static void setPlayer(Player player) {
    	getShips().set(0, player);
    }
    
    /**
     * Fetches the player's current position
     *
     * @return position vector : returns the position of the player
     */
    public Vector2 getPlayerPos(){
        return new Vector2(getPlayer().getPosition().x, getPlayer().getPosition().y);
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

        for (int i = 0; i < getShips().size(); i++){
            getShips().get(i).changeDamageReceived(value);
        }
        getCollege("anne_lister").changeDamageReceived(value);
        getCollege("constantine").changeDamageReceived(value);
        getCollege("goodricke").changeDamageReceived(value);
        getCollege("halifax").changeDamageReceived(value);
        getCollege("langwith").changeDamageReceived(value);
        getCollege("vanbrugh").changeDamageReceived(value);
    }

    public void SetGoldCoinMulti(int num){
        GameScreen.getDifficulty().SetGoldCoinMulti(num);
    }

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
        getHud().dispose();
        stage.dispose();
    }
    
    public static ArrayList<SteerableEntity> getShips() {
    	return ships;
    }
    
    public static ArrayList<Coin> getCoins() {
    	return coins;
    }

	public static Difficulty getDifficulty() {
		return difficulty;
	}

	public AvailableSpawn getInvalidSpawn() {
		return invalidSpawn;
	}

	public static ArrayList<College> getColleges() {
		return colleges;
	}

	public static void setColleges(ArrayList<College> colleges) {
		GameScreen.colleges = colleges;
	}

	public static ArrayList<Powerup> getPowerups() {
		return powerups;
	}

	public Hud getHud() {
		return hud;
	}

	public Float getTempTime() {
		return TempTime;
	}

	public static void setShips(ArrayList<SteerableEntity> ships) {
		GameScreen.ships = ships;
	}

	public static void setCoins(ArrayList<Coin> coins) {
		GameScreen.coins = coins;
	}

	public static void setPowerups(ArrayList<Powerup> powerups) {
		GameScreen.powerups = powerups;
	}

	public void setTempTime(Float tempTime) {
		TempTime = tempTime;
	}

	public void setInvalidSpawn(AvailableSpawn invalidSpawn) {
		this.invalidSpawn = invalidSpawn;
	}

	public static PirateGame getGame() {
		return game;
	}

	public static void setGame(PirateGame game) {
		GameScreen.game = game;
	}
}
