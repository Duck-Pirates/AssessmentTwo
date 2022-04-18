package com.mygdx.pirategame.desktop;

import java.awt.*;
import java.nio.IntBuffer;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.pirategame.PirateGame;
import org.lwjgl.glfw.GLFW;

public class DesktopLauncher {
	public static void main (String[] arg) {
		IntBuffer xpos_b, ypos_b, width_b, height_b;
		int value = 1;
		xpos_b = ypos_b = width_b = height_b = IntBuffer.allocate(value);
		GLFW.glfwGetMonitorWorkarea(GLFW.glfwGetPrimaryMonitor(), xpos_b, ypos_b, width_b, height_b);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setWindowedMode(1400, 1400);
		new Lwjgl3Application(new PirateGame(), config);
	}
}
