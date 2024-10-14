package io.github.hbazai.fighter.resources;

import com.badlogic.gdx.graphics.Color;

public class GlobalVariables {
    // Window
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 480;

    // Worls
    public static final float WORLD_WIDTH = 80f;
    public static final float WORLD_HEIGHT=48f;
    public static final float WORLD_SCALE = 0.05f;
    public static final float MIN_WORLD_HEIGHT = 0.85f;

    // Colors
    public static final Color GOLD = new Color(094f, 0.85f, 0.32f, 1f);

    // Game
    public enum Difficulty {
        EASY,
        MEDIUM,
        HARD
    }
}
