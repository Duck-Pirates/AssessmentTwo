package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;

import java.util.ArrayList;
import java.util.Arrays;


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
     */

    protected void save(GameScreen game, SkillTree shop) {

        difficultySave = game.difficulty;
        invalidSpawnSave = game.invalidSpawn;

        playerSave = new PlayerSave(game.player);
        for (College college: game.colleges) {
            CollegeSave collegeSave = new CollegeSave(college);
            collegesSaves.add(collegeSave);
            if(collegeSave.fleet != null){ // Checks if the college has a fleet (alcuin doesn't have any ships)
                shipsSaves.addAll(collegeSave.fleet);
            }
        }
        for (int i = 20; i > 0; i--){ // Saves the unaligned ships
            shipsSaves.add(new ShipSave(game.ships.get(game.ships.size() - i)));
        }
        for (Coin coin: game.coins) {
            coinSaves.add(new CoinSave(coin));
        }
        hudSave = new HudSave(game.hud);
        for (Powerup powerup: game.powerups){
            powerUpSaves.add(new PowerUpSave(powerup));
        }

        tempTimeSave = game.TempTime;

        statesSave = shop.states;

        json.setOutputType(JsonWriter.OutputType.json);
        ArrayList<Object> parameters2Save = new ArrayList<>(Arrays.asList(difficultySave, invalidSpawnSave, playerSave, collegesSaves, shipsSaves, coinSaves, hudSave, powerUpSaves, tempTimeSave, statesSave));
        file.writeString(Base64Coder.encodeString(json.prettyPrint(parameters2Save)), false);
    }




    /**
     * PlayerSave
     * Auxiliary class to save data about the player
     */
    public static class PlayerSave{

        private float velocity, rotation, maxVelocity, maxAngularVelocity;
        private Vector2 position;

        public PlayerSave(Player player) {
            velocity = player.velocity;
            rotation = player.getRotation();
            maxVelocity = player.maxVelocity;
            maxAngularVelocity = player.maxAngularVelocity;
            position = player.b2body.getPosition();
        }

        public PlayerSave(){

        }

        public void createPlayer(GameScreen game){
            Player result = new Player(game, this.position, this.rotation);
            result.velocity = this.velocity;
            result.maxVelocity = this.maxVelocity;
            result.maxAngularVelocity = this.maxAngularVelocity;
            game.player = result;
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
            position = college.b2body.getPosition();
            health = college.health;
            destroyed = college.destroyed;
            setToDestroy = college.setToDestroy;
        }

        public CollegeSave(){

        }

        public College createCollege(GameScreen game){
            College result = new College(game, collegeName, this.position.x, this.position.y, String.format("%s_flag.png", collegeName.toLowerCase()).replace(' ', '_'), String.format("%s_ship.png", collegeName.toLowerCase()).replace(' ', '_'), collegeName.equals("Alcuin") ? 0 : game.difficulty.getMaxCollegeShips(), game.invalidSpawn);
            ArrayList<EnemyShip> newfleet = new ArrayList<>();
            for(int i = 0; i < this.fleet.size(); i++){
                newfleet.add(this.fleet.get(i).createEnemyShip(game));
            }
            result.fleet = newfleet;
            result.b2body.getPosition().set(this.position);
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


        public ShipSave(EnemyShip ship){
            college = ship.college;
            position = ship.b2body.getPosition();
            rotation = ship.getRotation();
            health = ship.health;
            destroyed = ship.destroyed;
            setToDestroy = ship.setToDestroy;



        }

        public ShipSave(){

        }

        public EnemyShip createEnemyShip(GameScreen game){
            EnemyShip result = new EnemyShip(game, this.position.x, this.position.y, String.format("%s_ship.png", this.college.toLowerCase()).replace(' ', '_'), this.college);
            result.b2body.getPosition().set(this.position);
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
            position = coin.b2body.getPosition();
            destroyed = coin.destroyed;
            setToDestroyed = coin.setToDestroyed;
        }

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

        public HudSave(){

        }

        public void createHud(GameScreen game){
            Hud oldHud = game.hud;
            oldHud.timeCount = this.timeCount;
            oldHud.Constant_timeCount = this.constantTimeCount;
            oldHud.PowerupTimer = this.powerUpTimer;
            oldHud.score = this.score;
            oldHud.health = this.health;
            oldHud.coins = this.coins;
            oldHud.coinMulti = game.difficulty.getGoldCoinMulti();
            oldHud.PowerUpType = this.powerUpType;
            oldHud.PowerupTimerBool = this.powerUpTimerBool;
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
            position = powerup.b2body.getPosition();
            type = powerup.powerupType;
            destroyed = powerup.destroyed;
            setToDestroyed = powerup.setToDestroyed;
        }

        public PowerUpSave(){

        }

        public Powerup createPowerUp(GameScreen game){
            Powerup result = new Powerup(game, this.position.x, this.position.y, this.type);
            result.destroyed = this.destroyed;
            result.setToDestroyed = this.setToDestroyed;
            result.b2body.getPosition().set(this.position);
            return result;
        }
    }

    public void load(PirateGame game){
        ArrayList<Object> loaded_data = json.fromJson(ArrayList.class, String.valueOf(Base64Coder.decode(file.readString())));

        GameScreen gameScreen = new GameScreen(game, (Difficulty) loaded_data.get(0));
        SkillTree shop = new SkillTree(game);

        gameScreen.invalidSpawn = (AvailableSpawn) loaded_data.get(1);
        ((PlayerSave) loaded_data.get(2)).createPlayer(gameScreen);
        ArrayList<College> colleges = new ArrayList<>();
        ArrayList<EnemyShip> ships = new ArrayList<>();
        ArrayList<Coin> coins = new ArrayList<>();
        ArrayList<Powerup> powerups = new ArrayList<>();
        for (CollegeSave college:
                (Array<CollegeSave>) loaded_data.get(3)) {
            colleges.add(college.createCollege(gameScreen));
            ships.addAll(colleges.get(colleges.size()-1).fleet);
        }
        for(ShipSave ship: (Array<ShipSave>) loaded_data.get(4)){
            if(!ship.college.equals("Unaligned")){
                continue;
            }
            ships.add(ship.createEnemyShip(gameScreen));
        }
        gameScreen.colleges = colleges;
        gameScreen.ships = ships;
        for (CoinSave coin: (Array<CoinSave>) loaded_data.get(5)){
            Coin newcoin = new Coin(gameScreen, coin.position.x, coin.position.y);
            newcoin.destroyed = coin.destroyed;
            newcoin.setToDestroyed = coin.setToDestroyed;
            newcoin.b2body.getPosition().set(coin.position);
            coins.add(newcoin);
        }
        gameScreen.coins = coins;
        ((HudSave) loaded_data.get(6)).createHud(gameScreen);
        for(PowerUpSave powerUp: (Array<PowerUpSave>) loaded_data.get(7)){
            powerups.add(powerUp.createPowerUp(gameScreen));
        }
        gameScreen.powerups = powerups;
        gameScreen.TempTime = (Float) loaded_data.get(8);
        shop.states = new ArrayList(Arrays.asList(((Array<Integer>) loaded_data.get(9)).toArray()));
        game.gameScreen = gameScreen;
        game.skillTreeScreen = shop;
    }
}
