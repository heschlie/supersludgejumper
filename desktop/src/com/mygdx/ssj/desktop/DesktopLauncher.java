package com.mygdx.ssj.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.ssj.MainGame;
import com.mygdx.ssj.SsjMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 1280;
        config.width = 720;
		new LwjglApplication(new MainGame(), config);
	}
}
