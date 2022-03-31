package com.mygdx.pirategame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.pirategame.PirateGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		//Todo Change this, but keep for now
		config.setWindowSizeLimits(Lwjgl3ApplicationConfiguration.getDisplayMode().width-300, Lwjgl3ApplicationConfiguration.getDisplayMode().height-300, Lwjgl3ApplicationConfiguration.getDisplayMode().width-300, Lwjgl3ApplicationConfiguration.getDisplayMode().height-300);

		new Lwjgl3Application(new PirateGame(), config);

	}
}
