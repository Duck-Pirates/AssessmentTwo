package com.mygdx.pirategame.configs;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.mygdx.pirategame.PirateGame;
import com.mygdx.pirategame.entities.Coin;
import com.mygdx.pirategame.entities.College;
import com.mygdx.pirategame.entities.EnemyShip;
import com.mygdx.pirategame.entities.Player;
import com.mygdx.pirategame.entities.Powerup;
import com.mygdx.pirategame.entities.SteerableEntity;
import com.mygdx.pirategame.screens.GameScreen;
import com.mygdx.pirategame.screens.Hud;
import com.mygdx.pirategame.screens.SkillTree;
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
    private static AvailableSpawn invalidSpawnSave;

    private PlayerSave playerSave;
    private ArrayList<CollegeSave> collegesSaves = new ArrayList<>();
    private ArrayList<ShipSave> shipsSaves = new ArrayList<>();
    private ArrayList<CoinSave> coinSaves = new ArrayList<>();
    private HudSave hudSave;
    private ArrayList<PowerUpSave> powerUpSaves = new ArrayList<>();

    private float tempTimeSave;


    private ArrayList<Integer> statesSave = new ArrayList<>();

    /**
     * Saves the game state and writes it to the json file
     * @param game The GameScreen class
     * @param shop The Game's shop
     */

    public void save(GameScreen game, SkillTree shop) {

        difficultySave = GameScreen.difficulty;
        invalidSpawnSave = game.invalidSpawn;

        playerSave = new PlayerSave(GameScreen.player);
        for (College college: GameScreen.colleges) {
            CollegeSave collegeSave = new CollegeSave(college);
            collegesSaves.add(collegeSave);
            if(collegeSave.fleet != null){ // Checks if the college has a fleet (alcuin doesn't have any ships)
                shipsSaves.addAll(collegeSave.fleet);
            }
        }
        for (int i = 20; i > 0; i--){ // Saves the unaligned ships
            shipsSaves.add(new ShipSave(GameScreen.ships.get(GameScreen.ships.size() - i)));
        }
        for (Coin coin: GameScreen.coins) {
            coinSaves.add(new CoinSave(coin));
        }
        hudSave = new HudSave(game.hud);
        for (Powerup powerup: GameScreen.powerups){
            powerUpSaves.add(new PowerUpSave(powerup));
        }

        tempTimeSave = game.TempTime;

        statesSave = SkillTree.states;

        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<Object> parameters2Save = new ArrayList<>(Arrays.asList(difficultySave, invalidSpawnSave, playerSave, collegesSaves, shipsSaves, coinSaves, hudSave, powerUpSaves, tempTimeSave, statesSave));
        file.writeString(Base64Coder.encodeString(json.prettyPrint(parameters2Save)), false);
    }




    /**
     * PlayerSave
     * Auxiliary class to save data about the player
     */
    public static class PlayerSave{

        private float velocity, maxVelocity, maxAngularVelocity;
        public PlayerSave(Player player) {
            velocity = player.velocity;
            player.getRotation();
            maxVelocity = player.getVelocity();
            maxAngularVelocity = player.getAngularVelocity();
            player.body.getPosition();
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
        public void createPlayer(GameScreen game){
            Player result = new Player(game);
            result.velocity = this.velocity;
            result.maxVelocity = this.maxVelocity;
            result.maxAngularVelocity = this.maxAngularVelocity;
            GameScreen.player = result;
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
            collegeName = college.getCurrentCollegeName();
            for(EnemyShip ship: college.fleet){
                fleet.add(new ShipSave(ship));
            }
            position = college.body.getPosition();
            health = college.health;
            destroyed = college.destroyed;
            setToDestroy = college.setToDestroy;
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
            College result = new College(game, collegeName, this.position.x, this.position.y, String.format("%s_flag.png", collegeName.toLowerCase()).replace(' ', '_'), String.format("%s_ship.png", collegeName.toLowerCase()).replace(' ', '_'), collegeName.equals("Alcuin") ? 0 : GameScreen.difficulty.getMaxCollegeShips(), game.invalidSpawn);
            ArrayList<EnemyShip> newfleet = new ArrayList<>();
            for(int i = 0; i < this.fleet.size(); i++){
                newfleet.add(this.fleet.get(i).createEnemyShip(game));
            }
            result.fleet = newfleet;
            result.body.getPosition().set(this.position);
            result.health = this.health;
            result.destroyed = this.destroyed;
            result.setToDestroy = this.setToDestroy;
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


        public ShipSave(SteerableEntity steerableEntity){
            college = steerableEntity.college;
            position = steerableEntity.body.getPosition();
            rotation = steerableEntity.getRotation();
            health = steerableEntity.health;
            destroyed = steerableEntity.destroyed;
            setToDestroy = steerableEntity.setToDestroy;



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
            EnemyShip result = new EnemyShip(game, this.position.x, this.position.y, String.format("%s_ship.png", this.college.toLowerCase()).replace(' ', '_'), this.college);
            result.body.getPosition().set(this.position);
            result.setRotation(this.rotation);
            result.health = this.health;
            result.destroyed = this.destroyed;
            result.setToDestroy = this.setToDestroy;
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
            position = coin.body.getPosition();
            destroyed = coin.destroyed;
            setToDestroyed = coin.setToDestroyed;
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
            powerUpTimer = Hud.PowerupTimer;
            score = Hud.score;
            health = Hud.health;
            coins = Hud.coins;
            powerUpType = Hud.PowerUpType;
            powerUpTimerBool = Hud.PowerupTimerBool;
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
            Hud oldHud = game.hud;
            oldHud.timeCount = this.timeCount;
            oldHud.Constant_timeCount = this.constantTimeCount;
            Hud.PowerupTimer = this.powerUpTimer;
            Hud.score = this.score;
            Hud.health = this.health;
            Hud.coins = this.coins;
            Hud.coinMulti = GameScreen.difficulty.getGoldCoinMulti();
            Hud.PowerUpType = this.powerUpType;
            Hud.PowerupTimerBool = this.powerUpTimerBool;
        }
    }

    /**
     * PowerUpSave
     * Auxiliary class to save data about the PowerUps
     */
    public static class PowerUpSave{

        private Vector2 position;
        private Integer type;
        private boolean destroyed, setToDestroyed;

        public PowerUpSave(Powerup powerup){
            position = powerup.body.getPosition();
            type = powerup.powerupType;
            destroyed = powerup.destroyed;
            setToDestroyed = powerup.setToDestroyed;
        }

        /**
         * Only used by the LibGDX's Json library
         */
        public PowerUpSave(){

        }

        /**
         * Create the PowerUp instance while loading the game
         * @param game
         * @return PowerUp Object
         */
        public Powerup createPowerUp(GameScreen game){
            Powerup result = new Powerup(game, this.position.x, this.position.y, this.type);
            result.destroyed = this.destroyed;
            result.setToDestroyed = this.setToDestroyed;
            result.body.getPosition().set(this.position);
            return result;
        }
    }

    /**
     * Reads the data from a saved game and loads the game and changes the Screens in the Game's main class (PirateGame)
     * @param game
     */
    public void load(PirateGame game){
        ArrayList<Object> loaded_data = json.fromJson(ArrayList.class, String.valueOf(Base64Coder.decode(file.readString())));

        GameScreen gameScreen = new GameScreen(game, (Difficulty) loaded_data.get(0));
        SkillTree shop = new SkillTree(game);

        gameScreen.invalidSpawn = (AvailableSpawn) loaded_data.get(1);
        ((PlayerSave) loaded_data.get(2)).createPlayer(gameScreen);
        ArrayList<College> colleges = new ArrayList<>();
        ArrayList<SteerableEntity> ships = new ArrayList<>();
        ArrayList<Coin> coins = new ArrayList<>();
        ArrayList<Powerup> powerups = new ArrayList<>();
        for (CollegeSave college: (Array<CollegeSave>) loaded_data.get(3)) {
            colleges.add(college.createCollege(gameScreen));
            ships.addAll(colleges.get(colleges.size()-1).fleet);
        }
        for(ShipSave ship: (Array<ShipSave>) loaded_data.get(4)){
            if(!ship.college.equals("Unaligned")){
                continue;
            }
            ships.add(ship.createEnemyShip(gameScreen));
        }
        GameScreen.colleges = colleges;
        GameScreen.ships = ships;
        for (CoinSave coin: (Array<CoinSave>) loaded_data.get(5)){
            Coin newcoin = new Coin(gameScreen, coin.position.x, coin.position.y);
            newcoin.destroyed = coin.destroyed;
            newcoin.setToDestroyed = coin.setToDestroyed;
            newcoin.body.getPosition().set(coin.position);
            coins.add(newcoin);
        }
        GameScreen.coins = coins;
        ((HudSave) loaded_data.get(6)).createHud(gameScreen);
        for(PowerUpSave powerUp: (Array<PowerUpSave>) loaded_data.get(7)){
            powerups.add(powerUp.createPowerUp(gameScreen));
        }
        GameScreen.powerups = powerups;
        gameScreen.TempTime = (Float) loaded_data.get(8);
        SkillTree.states = new ArrayList(Arrays.asList(((Array<Integer>) loaded_data.get(9)).toArray()));
        
        game.setGameScreen(gameScreen);
        game.setSkillTreeScreen(shop);
    }
}
