package com.mygdx.pirategame.configs;

public final class Constants {

	// Pixels per meter
	public static final float PPM = 100;

	//Bits used in collisions
	public static final short DEFAULT_BIT = 1;
	public static final short PLAYER_BIT = 2;
	public static final short COLLEGEFIRE_BIT = 4;
	public static final short POWERUP_BIT = 8;
	public static final short COIN_BIT = 16;
	public static final short CANNON_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short COLLEGE_BIT = 128;
	public static final short COLLEGESENSOR_BIT = 256;
	public static final short TORNADO_BIT = 512;
	public static final short CLOUDS_BIT = 1024;
	public static final short NOSPAWNAREA_BIT = 2048;

	//Constant for swapping between screens
	public static final int MENU = 0;
	public static final int GAME = 1;
	public static final int SKILL = 2;
	public static final int DEATH = 3;
	public static final int HELP = 4;
	public static final int VICTORY = 5;
	public static final int LOADING = 6;
}