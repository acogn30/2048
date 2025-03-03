package io.github.Simple2048.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.github.Simple2048.MainGame;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // This handles macOS support and helps on Windows.
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new MainGame(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("2048 Game");
        configuration.setWindowedMode(480, 640); // 設定寬度 480，高度 640
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowIcon(new String[]{"libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png"});
        
        return configuration;
    }
}