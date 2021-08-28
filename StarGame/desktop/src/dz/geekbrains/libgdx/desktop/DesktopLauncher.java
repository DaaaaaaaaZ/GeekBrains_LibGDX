package dz.geekbrains.libgdx.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import dz.geekbrains.libgdx.StarGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = 600;
		config.width = 400;
		config.resizable = false;
		config.title = "Lesson 1";
		new LwjglApplication(new StarGame(), config);
	}
}
