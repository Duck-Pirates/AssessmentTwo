package com.mygdx.pirategame;

import com.badlogic.gdx.Gdx;

public enum Difficulty {
    EASY(3, 5, 15,100, 5, 8, 1, 4f, 20),
    MEDIUM(0.95f, 5, 15,80, 3, 10, 1, 6f, 20),
    HARD(0.90f, 10, 15,80, 2, 12, 1, 4f, 20);

    private float speedReduction, maxSpeed, traverseSpeed;
    private int damageReceived, damageDealt, HP, maxGoldXCoin, maxCollegeShips, goldCoinMulti;

    private float prevMaxSpeed, prevTraverseSpeed;
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
        this.goldCoinMulti *= num;
    }

    public void SetSpeedReduction(float num) { this.speedReduction = num; }

    public void SetDamageReceived(int num) { this.damageReceived = num; }  // DAMAGE RECEIVED

    public void SetDamageDealt(int num) { this.damageDealt = num; }  // DAMAGE Dealt

    public void IncreaseHP() {this.HP += 50; }

    public void SetMaxSpeed(float num) { this.maxSpeed = num; }



    // Save to previous if powerup is in effect
    public void IncreaseMaxSpeedPercent(int num){this.maxSpeed = prevMaxSpeed * (1+ num/100); } // num increase
    public void IncreaseTraversePercent(int num){this.traverseSpeed = prevTraverseSpeed * (1 + num/100); }
    public void IncreaseDamageDealtPercent(int num){this.damageDealt = prevDamageDealt + num; }
    public void IncreaseCoinMulti(int num){this.goldCoinMulti = prevGoldCoinMulti * num; }
    // SetGoldCoinMulti
    public void DecreaseDamageRecievedPercent(int num){this.damageReceived = num;}
    // Cone shot



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


    public void SavePowerupStats () {

        prevDamageDealt = damageDealt;
        prevMaxSpeed = maxSpeed;
        prevGoldCoinMulti = goldCoinMulti;
        prevDamageReceived = damageReceived;
        prevTraverseSpeed = traverseSpeed;

    }

    public void PreviousPowerupStats () {

        damageDealt = prevDamageDealt;
        maxSpeed = prevMaxSpeed;
        goldCoinMulti = prevGoldCoinMulti ;
        damageReceived = prevDamageReceived;
        traverseSpeed = prevTraverseSpeed;
    }


}
