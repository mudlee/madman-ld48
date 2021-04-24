package hu.mudlee.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import hu.mudlee.Constants;
import hu.mudlee.LD48Game;

public class Lwjgl3Launcher {
	public static void main(String[] args) {
		createApplication();
	}

	private static Lwjgl3Application createApplication() {
		return new Lwjgl3Application(new LD48Game(), getDefaultConfiguration());
	}

	private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
		final var config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("LD48");
		config.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
		config.useVsync(true);
		config.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");
		return config;
	}
}