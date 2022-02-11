package tk.shardsoftware.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import tk.shardsoftware.PirateGame;
import tk.shardsoftware.util.DebugUtil;

public class DesktopLauncher {
	public static void main(String[] args) {
		boolean fullscreenFlag = false;
		for (String arg : args) {
			if (arg.equals("--fullscreen") || arg.equals("-f")) {
				fullscreenFlag = true;
			}
			if (arg.equals("--debug") || arg.equals("-d")) {
				DebugUtil.DEBUG_MODE = true;
			}
		}

		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		if (fullscreenFlag) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else {
			config.setWindowedMode(1280, 720);
		}
		config.setTitle("Team 29 - Shard Software - Pirate Game");
		config.setResizable(false);
		config.setForegroundFPS(60);
		config.useVsync(false);
		new Lwjgl3Application(new PirateGame(), config);
	}
}
