package com.mygdx.pirategame.configs;

/**
 * The difficulty stores values that change the game based on which difficulty the player chooses
 *
 * @author Davide Bressani, Benjamin Whitnell
 */
public enum Difficulty {

    EASY(0.95f, 10, 15,100, 5, 8, 1, 250.5f, 6),
    MEDIUM(0.93f, 10, 15,80, 3, 10, 1, 225f, 6),
    HARD(0.91f, 15, 15,80, 2, 12, 1, 200f, 6);

    private float speedReduction, maxSpeed, traverseSpeed;
    private int damageReceived, damageDealt, HP, maxGoldXCoin, maxCollegeShips, goldCoinMulti;

    private float prevMaxSpeed, prevTraverseSpeed, prevspeedReduction;
    private int prevDamageReceived, prevDamageDealt, prevGoldCoinMulti;

    private boolean ConeMec = false;

    /**
     * Difficulty enum constructor, used to set and store variables
     *
     * @param speedReduction
     * @param damageReceived
     * @param damageDealt
     * @param HP
     * @param maxGoldXCoin
     * @param maxCollegeShips
     * @param goldCoinMulti
     * @param maxSpeed
     * @param traverseSpeed
     */
    Difficulty(float speedReduction, int damageReceived, int damageDealt, int HP, int maxGoldXCoin, int maxCollegeShips, int goldCoinMulti, float maxSpeed, int traverseSpeed){

        this.speedReduction = speedReduction;
        this.damageReceived = damageReceived;
        this.damageDealt = damageDealt;
        this.HP = HP;
        this.maxGoldXCoin = maxGoldXCoin;
        this.maxCollegeShips = maxCollegeShips;
        this.goldCoinMulti = goldCoinMulti;
        this.maxSpeed = maxSpeed;
        this.traverseSpeed = traverseSpeed;

        savePowerupStats();
        previousPowerupStats();

    }

    /**
     * Increases the maxSpeed variable
     *
     * @param percentage The change percentage
     */
    public void increaseMaxSpeedByPercent(int percentage){

        if (maxSpeed == prevMaxSpeed){ // If this is true, no Speed Powerup has been activated previously
            maxSpeed = maxSpeed * (1+ percentage/100);
        }
        else if (maxSpeed != prevMaxSpeed) { // If this is true, a Speed Powerup has been activated previously
            prevMaxSpeed = prevMaxSpeed * (1+ percentage/100);
            maxSpeed = maxSpeed + (prevMaxSpeed - maxSpeed);
        }
        this.speedReduction = this.speedReduction + 0.01f;

    }

    /**
     * Increases the traverseSpeed variable
     *
     * @param percentage The change percentage
     */
    public void increaseTraverseByPercent(int percentage){this.traverseSpeed = prevTraverseSpeed - percentage;}

    /**
     * Increases the damageDealt variable
     *
     * @param percentage The change percentage
     */
    public void increaseDamageDealtByPercent(int percentage){

        if (damageDealt == prevDamageDealt){ // If this is true, no Damage Powerup has been activated previously
            damageDealt = damageDealt + percentage;
        }
        else if (damageDealt != prevDamageDealt) { // If this is true, a Damage Powerup has been activated previously
            prevDamageDealt = prevDamageDealt + percentage;
            damageDealt = damageDealt + (prevDamageDealt - damageDealt);
        }

    }

    /**
     * Increases the goldCoinMulti variable
     *
     * @param amount The change amount
     */
    public void increaseCoinMulti(int amount){

        if (goldCoinMulti == prevGoldCoinMulti){ // If this is true, no Coin Powerup has been activated previously
            goldCoinMulti += amount;
        } else if (goldCoinMulti != prevGoldCoinMulti){ // If this is true, a Coin Powerup has been activated previously
            prevGoldCoinMulti += amount;
            goldCoinMulti += amount;
        }

    }

    /**
     * Decreases the damageReceived
     *
     * @param amount
     */
    public void decreaseDamageRecieved(int amount){

        if (damageReceived == prevDamageReceived){ // If this is true, no Damage Powerup has been activated previously
            damageReceived = amount;
        } else if (damageReceived != prevDamageReceived){ // If this is true, a Coin Powerup has been activated previously
            prevDamageReceived = amount;
            damageReceived = amount;
        }

    }

    /**
     * Saves the previous variable associated with the Powerups whenever one of them has been picked up
     */
    public void savePowerupStats() {

        prevDamageDealt = damageDealt;
        prevMaxSpeed = maxSpeed;
        prevGoldCoinMulti = goldCoinMulti;
        prevDamageReceived = damageReceived;
        prevTraverseSpeed = traverseSpeed;
        prevspeedReduction = speedReduction;

    }

    /**
     * Resets all the variables to their previous values whenever a Powerup has been deactivated
     */
    public void previousPowerupStats() {

        damageDealt = prevDamageDealt;
        maxSpeed = prevMaxSpeed;
        goldCoinMulti = prevGoldCoinMulti ;
        damageReceived = prevDamageReceived;
        traverseSpeed = prevTraverseSpeed;
        speedReduction = prevspeedReduction;

    }


    public int getDamageReceived() { return damageReceived; }

    public int getDamageDealt() { return damageDealt; }

    public int getHP() { return HP; }

    public int getMaxGoldXCoin() { return maxGoldXCoin; }

    public int getMaxCollegeShips() { return maxCollegeShips; }

    public int getGoldCoinMulti() { return goldCoinMulti; }

    public float getMaxSpeed() { return  maxSpeed; }

    public float getTraverseSpeed() { return traverseSpeed; }

    public boolean getConeMec(){ return ConeMec; }

    public void setGoldCoinMulti(int amount) { this.goldCoinMulti += amount; }

    public void setDamageReceived(int amount) { this.damageReceived = amount; }

    public void setDamageDealt(int amount) { this.damageDealt += amount; }

    public void setConeMec(boolean bool){ ConeMec = bool; }

    public void setMaxSpeed(float percentage) {

        this.maxSpeed = this.maxSpeed * percentage;

        if (this.speedReduction < 0.94f){
            this.speedReduction = this.speedReduction + 0.03f;
        }
        else{
            this.speedReduction = this.speedReduction + 0.01f;
        }

    }

}
