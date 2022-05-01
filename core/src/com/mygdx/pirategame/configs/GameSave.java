package com.mygdx.pirategame.configs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.entities.*;
import com.mygdx.pirategame.fsm.EnemyStateMachine;
import com.mygdx.pirategame.screens.*;
import com.mygdx.pirategame.world.AvailableSpawn;


/**
 * GameSave
 * The class manages the saving and loading phases of the game. To do so, it only get the important information
 * about the main classes in the project and it serializes them (convert into bytes) and writes them onto a json
 * file.
 *
 * @author Davide Bressani
 * @version 2.0
 */

public class GameSave {

    private FileHandle file = Gdx.files.local("saves/save1.json");
    private Json json = new Json();

    private static Difficulty difficultySave;
    private static ObjectMap<Integer, Array<Integer>> invalidSpawnSaveHashMap;

    private PlayerSave playerSave;
    private ArrayList<CollegeSave> collegesSaves = new ArrayList<>();
    private ArrayList<ShipSave> shipsSaves = new ArrayList<>();
    private ArrayList<CoinSave> coinSaves = new ArrayList<>();
    private HudSave hudSave;
    private ArrayList<PowerUpSave> powerUpSaves = new ArrayList<>();
    private ArrayList<TornadoSave> tornadoesSaves = new ArrayList<>();

    private float tempTimeSave;


    private ArrayList<Integer> statesSave = new ArrayList<>();

    /**
     * Saves the game state and writes it to the json file
     * @param game The GameScreen class
     * @param shop The Game's shop
     */

    public void save(GameScreen game, SkillTree shop) {
        difficultySave = GameScreen.getDifficulty();
        invalidSpawnSaveHashMap = AvailableSpawn.getBlockedTiles();
        Array<Integer> keys = new Array<>();
        Array<Array<Integer>> values = new Array<>();
        keys = invalidSpawnSaveHashMap.keys().toArray();
        values = invalidSpawnSaveHashMap.values().toArray();

        playerSave = new PlayerSave(GameScreen.getPlayer());
        for (College college: GameScreen.getColleges()) {
            CollegeSave collegeSave = new CollegeSave(college);
            collegesSaves.add(collegeSave);
            shipsSaves.addAll(collegeSave.fleet);
        }
        for (int i = 20; i > 0; i--){ // Saves the unaligned ships
            shipsSaves.add(new ShipSave(GameScreen.getShips().get(GameScreen.getShips().size() - i)));
        }
        for (Coin coin: GameScreen.getCoins()) {
            coinSaves.add(new CoinSave(coin));
        }
        hudSave = new HudSave(game.getHud());
        for (Powerup powerup: GameScreen.getPowerups()){
            powerUpSaves.add(new PowerUpSave(powerup));
        }

        tempTimeSave = game.getTempTime();

        statesSave = shop.states;

        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<Object> parameters2Save = new ArrayList<>(Arrays.asList(difficultySave, keys, values, playerSave, collegesSaves, shipsSaves, coinSaves, hudSave, powerUpSaves, tornadoesSaves, tempTimeSave, statesSave));
        file.writeString(json.prettyPrint(parameters2Save), false);
    }




    /**
     * PlayerSave
     * Auxiliary class to save data about the player
     */
    public static class PlayerSave{
    	private Vector2 velocity, position;
        private float angle;
        public PlayerSave(Player player) {
            velocity = player.getBody().getLinearVelocity();
            angle = player.getRotation();
            position = player.getBody().getPosition();
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public PlayerSave(){

        }

        /**
         * Modifies the game's Player instance while loading the game
         * @param game
         */
        public Player createPlayer(GameScreen game){
            Player result = new Player(game);
            result.getBody().setLinearVelocity(this.velocity);
            result.setPosition(this.position, this.angle);
            GameScreen.setPlayer(result);
            return result;
        }
    }


    /**
     * CollegeSave
     * Auxiliary class to save data about the college
     */
    public static class CollegeSave{

        private String collegeName;
        private ArrayList<ShipSave> fleet = new ArrayList<>();
        private Vector2 position;
        private int health;
        private boolean destroyed, setToDestroy;

        public CollegeSave(College college) {
            collegeName = college.getCollege();
            for(EnemyShip ship: college.getFleet()){
                fleet.add(new ShipSave(ship));
            }
            position = college.getBody().getPosition();
            health = college.getHealth();
            destroyed = college.isDestroyed();
            setToDestroy = college.isSetToDestroy();
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public CollegeSave(){

        }

        /**
         * Create the College instance while loading the game
         * @param game
         * @return College Object
         */
        public College createCollege(GameScreen game){
            College result = new College(game, collegeName, this.position.x, this.position.y, collegeName.equals("alcuin") ? 0 : GameScreen.getDifficulty().getMaxCollegeShips(), game.getInvalidSpawn());
            ArrayList<EnemyShip> newfleet = new ArrayList<>();
            for(int i = 0; i < this.fleet.size(); i++){
                newfleet.add(this.fleet.get(i).createEnemyShip(game));
            }
            result.setFleet(newfleet);
            result.getBody().getPosition().set(this.position);
            result.setHealth(this.health);
            result.setDestroyed(this.destroyed);
            result.setSetToDestroy(this.setToDestroy);
            return result;
        }

    }

    /**
     * ShipSave
     * Auxiliary class to save data about the college's fleets
     */
    public static class ShipSave{

        private String college;
        private Vector2 position;
        private float rotation;
        private int health;
        private boolean destroyed, setToDestroy;
        private SteeringBehavior<Vector2> behavior;
        private SteeringAcceleration<Vector2> steerOutput;


        public ShipSave(SteerableEntity steerableEntity){
            college = steerableEntity.getCollege();
            position = steerableEntity.getBody().getPosition();
            rotation = steerableEntity.getRotation();
            health = steerableEntity.getHealth();
            destroyed = steerableEntity.isDestroyed();
            setToDestroy = steerableEntity.isSetToDestroy();
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public ShipSave(){

        }

        /**
         * Creates the EnemyShip instance while loading the game
         * @param game
         * @return EnemyShip Object
         */
        public EnemyShip createEnemyShip(GameScreen game){
            EnemyShip result = new EnemyShip(game, this.position.x, this.position.y, this.college);
            result.getBody().getPosition().set(this.position);
            result.setRotation(this.rotation);
            result.setHealth(this.health);
            result.setDestroyed(this.destroyed);
            result.setSetToDestroy(this.setToDestroy);
            return result;
        }
    }

    /**
     * CoinSave
     * Auxiliary class to save data about the coins
     */
    public static class CoinSave {

        private Vector2 position;
        private boolean destroyed, setToDestroyed;

        public CoinSave(Coin coin){
            position = coin.getBody().getPosition();
            destroyed = coin.isDestroyed();
            setToDestroyed = coin.isDestroyed();
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public CoinSave(){

        }
    }

    /**
     * HudSave
     * Auxiliary class to save data about the HUD
     */
    public static class HudSave{

        private float timeCount, constantTimeCount, powerUpTimer;
        private Integer score, health, coins, powerUpType;
        private boolean powerUpTimerBool;

        public HudSave(Hud hud){
            timeCount = hud.timeCount;
            constantTimeCount = hud.Constant_timeCount;
            powerUpTimer = hud.PowerupTimer;
            score = hud.score;
            health = hud.health;
            coins = hud.coins;
            powerUpType = hud.PowerUpType;
            powerUpTimerBool = hud.PowerupTimerBool;
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public HudSave(){

        }

        /**
         * Modified the game's HUD instance while loading the game
         * @param game
         */
        public void createHud(GameScreen game){
            Hud oldHud = game.getHud();
            oldHud.timeCount = this.timeCount;
            oldHud.Constant_timeCount = this.constantTimeCount;
            oldHud.PowerupTimer = this.powerUpTimer;
            oldHud.score = this.score;
            oldHud.health = this.health;
            oldHud.coins = this.coins;
            oldHud.coinMulti = GameScreen.getDifficulty().getGoldCoinMulti();
            oldHud.PowerUpType = this.powerUpType;
            oldHud.PowerupTimerBool = this.powerUpTimerBool;
        }
    }

    /**
     * PowerUpSave
     * Auxiliary class to save data about the PowerUps
     */
    public static class PowerUpSave {

        private Vector2 position;
        private Integer type;
        private boolean destroyed, setToDestroyed;
        
        public PowerUpSave(Powerup powerup){
            position = powerup.getBody().getPosition();
            type = powerup.getPowerupType();
            destroyed = powerup.isDestroyed();
            setToDestroyed = powerup.isSetToDestroyed();
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public PowerUpSave() {

        }

        /**
         * Create the PowerUp instance while loading the game
         * @param game
         * @return PowerUp Object
         */
        public Powerup createPowerUp(GameScreen game) {
            Powerup result = new Powerup(game, this.position.x, this.position.y, this.type);
            result.setDestroyed(this.destroyed);
            result.setToDestroy(this.setToDestroyed);
            result.setPosition(this.position.x, this.position.y, 0);
            return result;
        }
    }
    /**
     * TornadoSave
     * Auxiliary class to save data about the Tornadoes
     */
    public static class TornadoSave{

        private float state;
        private int dimension;
        private Vector2 position;
        private boolean inContact;
        private float damage, timeElapsed;

        public TornadoSave(Tornado tornado){
            state = tornado.getState();
            position = tornado.getPosition();
            inContact = tornado.isInContact();
            damage = tornado.getDamage();
            timeElapsed = tornado.getTimeElapsed();
        }

        public TornadoSave(){}

        public Tornado createTornado(GameScreen game){
            Tornado result = new Tornado(game, this.position.x, this.position.y, this.dimension);
            result.setState(this.state);
            result.setPosition(this.position, 0);
            result.setInContact(this.inContact);
            result.setDamage(this.damage);
            result.setTimeElapsed(this.timeElapsed);
            result.getBody().getPosition().set(this.position);
            return result;
        }
    }

    /**
     * Reads the data from a saved game and loads the game and changes the Screens in the Game's main class (PirateGame)
     * @param game
     */
	public void load(PirateGame game){
        ArrayList<Object> loaded_data = json.fromJson(ArrayList.class, file.readString());

        GameScreen gameScreen = new GameScreen(game, (Difficulty) loaded_data.get(0));
        gameScreen.destroyBodies();
        SkillTree shop = new SkillTree(game);

        ObjectMap<Integer, Array<Integer>> newBlockedTiles = new ObjectMap<>();
        Array<Integer> oldKeys = (Array<Integer>)loaded_data.get(1);
        Array<Array<Integer>> oldValues = (Array<Array<Integer>>) loaded_data.get(2);
        for(int i = 0; i < oldKeys.size; i ++){
            newBlockedTiles.put(oldKeys.get(i), oldValues.get(i));
        }
        gameScreen.getInvalidSpawn().setBlockedTiles(newBlockedTiles);

        ArrayList<SteerableEntity> ships = new ArrayList<>();
        ships.add(((PlayerSave) loaded_data.get(3)).createPlayer(gameScreen));

        ArrayList<College> colleges = new ArrayList<>();
        ArrayList<Coin> coins = new ArrayList<>();
        ArrayList<Powerup> powerups = new ArrayList<>();
        ArrayList<Tornado> tornadoes = new ArrayList<>();

        for (CollegeSave college: (Array<CollegeSave>) loaded_data.get(4)) {
            colleges.add(college.createCollege(gameScreen));
            ships.addAll(colleges.get(colleges.size()-1).getFleet());
        }
        for(ShipSave ship: (Array<ShipSave>) loaded_data.get(5)){
            if(!ship.college.equals("Unaligned")){
                continue;
            }
            ships.add(ship.createEnemyShip(gameScreen));
        }
        GameScreen.setColleges(colleges);
        GameScreen.setShips(ships);
        for (CoinSave coin: (Array<CoinSave>) loaded_data.get(6)){
            Coin newcoin = new Coin(gameScreen, coin.position.x, coin.position.y);
            newcoin.setDestroyed(coin.destroyed);
            newcoin.setToDestroy(coin.setToDestroyed);
            newcoin.getBody().getPosition().set(coin.position);
            coins.add(newcoin);
        }
        GameScreen.setCoins(coins);
        ((HudSave) loaded_data.get(7)).createHud(gameScreen);
        for(PowerUpSave powerUp: (Array<PowerUpSave>) loaded_data.get(8)){
            powerups.add(powerUp.createPowerUp(gameScreen));
        }
        GameScreen.setPowerups(powerups);
        for (TornadoSave tornado: (Array<TornadoSave>) loaded_data.get(9)) {
            tornadoes.add(tornado.createTornado(gameScreen));
        }
        GameScreen.setTornadoes(tornadoes);
        gameScreen.setTempTime((Float) loaded_data.get(10));
        SkillTree.states = new ArrayList<>(Arrays.asList(((Array<Integer>) loaded_data.get(11)).toArray()));
        
        game.setGameScreen(gameScreen);
        game.setSkillTreeScreen(shop);
    }
}
