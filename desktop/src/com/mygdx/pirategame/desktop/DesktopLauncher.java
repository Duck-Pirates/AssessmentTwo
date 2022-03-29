package com.mygdx.pirategame.desktop;

import java.awt.*;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.pirategame.PirateGame;

public class DesktopLauncher {
	static Rectangle availableArea = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(availableArea.width-300, availableArea.height-300);
		new Lwjgl3Application(new PirateGame(), config);
	}
}
