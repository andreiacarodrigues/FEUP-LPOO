package org.feup.lcom.utilities;

/**
 * Classe onde estão guardadas várias constantes públicas necessárias ao desenvolvimento do programa
 * @author Ines Gomes e Andreia Rodrigues
 */
public class Constants {

    /* Camera and GameScreen */
    public final static float ASPECT_RATIO = (1920f/1200f);
    public final static float X_MAX = (1920f/1200f);
    public final static float Y_MAX = 1;
    public final static float GRAVITY = -9.81f;
    public final static float PPM = 100;

    /* Update Screen */
    public final static float TIMESTEP = 1/60f;
    public final static Point GAME_INITIAL_SPEED = new Point(-0.8f,0);
    public final static float GAME_INITIAL_SPAWN = 2f;
    public final static float INCREMENT = -0.1f;
    public final static int VELOCITY = 6;
    public final static int POSITION = 2;

    /* Collisions */
    public final static short NOTHING_BIT = 0;
    public final static short BIT_GROUND = 1;
    public final static short BIT_PLAYER = 2;
    public final static short BIT_ENEMY = 4;
    public final static short BIT_ENEMY_HEAD = 8;
    public final static short BIT_PLATFORMS = 16;
    public final static short BIT_COIN = 32;
    public final static short BIT_PLAYER_GROUND = 64;
    public final static short BIT_SPIKES = 128;

    /* Platforms */
    public final static float GROUND_SIZE = 0.109f;

    public final static float PLATFORM_WIDTH = 0.109f*3f;
    public final static float PLATFORM_HEIGHT = 0.109f/2f + 0.008f;
    public final static Point PLATFORM_POS = new Point(Constants.X_MAX +PLATFORM_WIDTH/2 + 0.2f, -Constants.PLATFORM_HEIGHT*4.5f+0.08f);

    public final static float PLATFORMWSPIKES_WIDTH = 0.109f;
    public final static float PLATFORMWSPIKES_HEIGHT = 0.109f*6f -0.01f;
    public final static Point PLATFORMWSPIKES_POS = new Point(Constants.X_MAX + PLATFORMWSPIKES_WIDTH/2 + 0.1f, Constants.Y_MAX-Constants.PLATFORMWSPIKES_HEIGHT + 0.02f);

    /* Player */
    public final static float PLAYER_WIDTH = (145f/1200f);
    public final static float PLAYER_HEIGHT = (190f/1200f);
    public final static Point PLAYER_POS = new Point(-Constants.X_MAX + Constants.GROUND_SIZE + 0.2f, - Constants.Y_MAX + 5 * Constants.GROUND_SIZE);

    /* Enemys */
    public static final float ENEMY_WIDTH = 0.115f;
    public static final float ENEMY_HEIGHT = 0.115f;
    public final static Point SLIME_ENEMY_POS = new Point(Constants.X_MAX + 0.2f,- Constants.Y_MAX + 5 * Constants.GROUND_SIZE-0.04f);
    public final static Point SPINNER_ENEMY_POS = new Point(Constants.X_MAX + 0.2f,- Constants.Y_MAX + 5 * Constants.GROUND_SIZE);
    public final static Point SLIME_TOP_PLATFORM_ENEMY_POS = new Point(Constants.X_MAX +PLATFORM_WIDTH/2 + 0.2f, -Constants.PLATFORM_HEIGHT*4.5f+0.1f+ ENEMY_HEIGHT);
    public final static Point SLIME_BOTTOM_PLATFORM_ENEMY_POS = new Point(Constants.X_MAX +PLATFORM_WIDTH/2 + 0.2f, - Constants.Y_MAX + 5 * Constants.GROUND_SIZE-0.04f);
    public static final Point SPIKES_POS = new Point(Constants.X_MAX + 0.2f,- Constants.Y_MAX + 5 * Constants.GROUND_SIZE-0.06f);
    public static final Point SPIKES_PLATFORM_POS = new Point(Constants.X_MAX +PLATFORM_WIDTH/2 + 0.2f,- Constants.Y_MAX + 5 * Constants.GROUND_SIZE-0.06f);

    /* Coins */
    public static final float COIN_SIZE = 0.07786f;
    public final static Point COIN_POS = new Point(Constants.X_MAX + 0.2f, 0);
    public final static Point COIN2_POS = new Point(Constants.X_MAX + Constants.COIN_SIZE + 0.3f, 0);
    public final static Point COIN3_POS = new Point(Constants.X_MAX + 2*Constants.COIN_SIZE + 0.4f, 0);
    public final static Point COIN_TOP_PLATFORM_POS = new Point(Constants.X_MAX + 0.2f, 0);
    public final static Point COIN2_TOP_PLATFORM_POS = new Point(Constants.X_MAX + Constants.COIN_SIZE + 0.3f,0);
    public final static Point COIN3_TOP_PLATFORM_POS = new Point(Constants.X_MAX + 2*Constants.COIN_SIZE + 0.4f,0);
    public final static Point COIN_BOTTOM_PLATFORM_POS = new Point(Constants.X_MAX + 0.2f, - Constants.Y_MAX + 5 * Constants.GROUND_SIZE);
    public final static Point COIN2_BOTTOM_PLATFORM_POS = new Point(Constants.X_MAX + Constants.COIN_SIZE + 0.3f, - Constants.Y_MAX + 5 * Constants.GROUND_SIZE);
    public final static Point COIN3_BOTTOM_PLATFORM_POS = new Point(Constants.X_MAX + 2*Constants.COIN_SIZE + 0.4f, - Constants.Y_MAX + 5 * Constants.GROUND_SIZE);

    /* Background */
    public static final String BACKGROUND = "background.png";
}
