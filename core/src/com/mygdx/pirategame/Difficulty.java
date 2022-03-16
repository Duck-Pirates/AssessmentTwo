package com.mygdx.pirategame;

public enum Difficulty {
    EASY(1, 5, 100, 5, 8),
    MEDIUM(0.95f, 5, 80, 3, 10),
    HARD(0.90f, 10, 80, 2, 12);

    private float speedReduction;
    private int damage, HP, maxGoldXCoin, maxCollegeShips;

    //TODO Add skill cost variable
    //TODO Add AI accuracy
    //TODO Add base radius for colleges

    Difficulty(float speedReduction, int damage, int HP, int maxGoldXCoin, int maxCollegeShips){
        this.speedReduction = speedReduction;
        this.damage = damage;
        this.HP = HP;
        this.maxGoldXCoin = maxGoldXCoin;
        this.maxCollegeShips = maxCollegeShips;
    }

    public float getSpeedReduction() {
        return speedReduction;
    }

    public int getDamage() {
        return damage;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxGoldXCoin() {
        return maxGoldXCoin;
    }

    public int getMaxCollegeShips() {
        return maxCollegeShips;
    }
}
