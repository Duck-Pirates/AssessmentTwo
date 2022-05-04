package com.mygdx.pirategame.desktop;

import com.mygdx.pirategame.PirateGame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1000, 700);
		config.setTitle("Duck Pirates");
		//config.setWindowIcon("DuckPiratesLogo.jpg");
		new Lwjgl3Application(new PirateGame(), config);
	}
}