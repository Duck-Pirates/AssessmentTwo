package com.mygdx.pirategame.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.pirategame.PirateGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
    		//config.width = Lwjgl3ApplicationConfiguration.getDesktopDisplayMode().width-300;
		//config.height = Lwjgl3ApplicationConfiguration.getDesktopDisplayMode().height-300;
		new Lwjgl3Application(new PirateGame(), config);

	}
}
