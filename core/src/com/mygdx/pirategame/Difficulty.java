package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;

public enum Difficulty {
    EASY(0.95f, 5, 15,100, 5, 8, 1, 12f, 40),
    MEDIUM(0.9f, 5, 15,80, 3, 10, 1, 8f, 30),
    HARD(0.8f, 10, 15,80, 2, 12, 1, 4f, 20);

    private float speedReduction, maxSpeed, traverseSpeed;
    private int damageReceived, damageDealt, HP, maxGoldXCoin, maxCollegeShips, goldCoinMulti;

    private float prevMaxSpeed, prevTraverseSpeed, prevspeedReduction;
    private int prevDamageReceived, prevDamageDealt, prevGoldCoinMulti;

    //TODO Add skill cost variable
    //TODO Add AI accuracy
    //TODO Add base radius for colleges

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
        SavePowerupStats();
        PreviousPowerupStats();
    }

    public void SetGoldCoinMulti(int num) {
        this.goldCoinMulti += num;
    }

    //public void SetSpeedReduction(float num) { this.speedReduction = num; }

    public void SetDamageReceived(int num) { this.damageReceived = num; }  // DAMAGE RECEIVED

    public void SetDamageDealt(int num) { this.damageDealt += num; }  // DAMAGE Dealt

    //public void IncreaseHP() {this.HP += 50; }

    public void SetMaxSpeed(float num) {
        this.maxSpeed = this.maxSpeed * num;
        this.speedReduction = this.speedReduction * 1.01f;
    }



    // Save to previous if powerup is in effect
    public void IncreaseMaxSpeedPercent(int num){
        Gdx.app.log("max Speed", Float.toString(maxSpeed));
        if (maxSpeed == prevMaxSpeed){ // No powerup
            maxSpeed = maxSpeed * (1+ num/100);
        } else if (maxSpeed != prevMaxSpeed) { // Powerup
            prevMaxSpeed = prevMaxSpeed * (1+ num/100); // Set prev max speed
            maxSpeed = maxSpeed + (prevMaxSpeed - maxSpeed); // Add prev max difference to the powerup max speed
        }
        Gdx.app.log("maxSpeed", Float.toString(maxSpeed));
    }
    public void IncreaseTraversePercent(int num){
        this.traverseSpeed = prevTraverseSpeed * (1 + num/100);
    }
    public void IncreaseDamageDealtPercent(int num){
        //this.damageDealt = prevDamageDealt + num;
        Gdx.app.log("damage dealt", Float.toString(damageDealt));
        if (damageDealt == prevDamageDealt){ // No powerup
            damageDealt = damageDealt + num;
        } else if (damageDealt != prevDamageDealt) { // Powerup
            prevDamageDealt = prevDamageDealt + num; // Set prev damage dealt
            damageDealt = damageDealt + (prevDamageDealt - damageDealt); // Add prev max difference to the powerup max speed
        }
        Gdx.app.log("damage dealt", Float.toString(damageDealt));
    }
    public void IncreaseCoinMulti(int num){
        //this.goldCoinMulti = num;

        Gdx.app.log("gold coin multi", Float.toString(goldCoinMulti));
        if (goldCoinMulti == prevGoldCoinMulti){
            goldCoinMulti += num;
        } else if (goldCoinMulti != prevGoldCoinMulti){
            prevGoldCoinMulti += num;
            goldCoinMulti += num;
        }
        Gdx.app.log("gold coin multi", Float.toString(goldCoinMulti));

    }
    // SetGoldCoinMulti
    public void DecreaseDamageRecievedPercent(int num){
        //this.damageReceived = num;
        Gdx.app.log("damage received", Float.toString(damageReceived));
        if (damageReceived == prevDamageReceived){
            damageReceived = num;
        } else if (damageReceived != prevDamageReceived){
            prevDamageReceived = num;
            damageReceived = num;
        }
        Gdx.app.log("damage received", Float.toString(damageReceived));
    }

    public float getSpeedReduction() { return speedReduction; }

    public int getDamageReceived() {
        return damageReceived;
    }

    public int getDamageDealt() { return damageDealt; }

    public int getHP() {
        return HP;
    }

    public int getMaxGoldXCoin() {
        return maxGoldXCoin;
    }

    public int getMaxCollegeShips() {
        return maxCollegeShips;
    }

    public int getGoldCoinMulti() {
        return goldCoinMulti;
    }

    public float getMaxSpeed() { return  maxSpeed; }

    public float getTraverseSpeed() {return traverseSpeed; }

    // When powerup is activated, save the previous variables
    public void SavePowerupStats () {
        prevDamageDealt = damageDealt;
        prevMaxSpeed = maxSpeed;
        prevGoldCoinMulti = goldCoinMulti;
        prevDamageReceived = damageReceived;
        prevTraverseSpeed = traverseSpeed;
        prevspeedReduction = speedReduction;

    }

    // After powerup is deactivated, revert to previous variables...
    public void PreviousPowerupStats () {
        damageDealt = prevDamageDealt;
        maxSpeed = prevMaxSpeed;
        goldCoinMulti = prevGoldCoinMulti ;
        damageReceived = prevDamageReceived;
        traverseSpeed = prevTraverseSpeed;
        speedReduction = prevspeedReduction;
    }


}
