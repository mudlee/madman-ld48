package hu.mudlee;

import com.badlogic.gdx.graphics.Color;

import java.util.Arrays;
import java.util.List;

public class Constants {
  // APP
  public static final int WINDOW_WIDTH = 1440;
  public static final int WINDOW_HEIGHT = 800;

  // GAME
  public static final int WORLD_UNIT = 16;
  public static final int START_DECIBEL = 80;
  public static final int DECREASE_DECIBEL_PER_CITIZEN = 80;
  public static final List<String> CITIZEN_MESSAGES = Arrays.asList("Huh?", "F@ck!", "What?", "Go Away!", "Hey!", "Bro?", "Bro!", "4A$#@5!?","$5$#@%1!","Que?");
  public static final List<String> PLAYER_MESSAGES = Arrays.asList("Stop!", "Calm down.", "Close your eyes", "Good.", "Go deep.", "Deeper.", "And deeeeeper.", "Woodooo!");
  public static final int PLAYER_MESSAGE_FREQUENCY_SEC = 2;
  public static final Color CITIZEN_MSG_BG = new Color(0x1d213cff);
  public static final int CITIZEN_MSG_FREQUENCE_SEC = 5;
  public static final int HYPNOTIZATION_RANGE_UNIT = 3;

  // MOVEMENT
  public static final float PLAYER_WALK_SPEED = 50f;
  public static final float CITIZEN_WALK_SPEED = 0.3f;
  public static final float PLAYER_IDLE_ANIM_SPEED = 0.5f;
  public static final float PLAYER_WALK_ANIM_SPEED = 0.1f;
  public static final float CITIZEN_IDLE_ANIM_SPEED = 0.5f;
  public static final float CITIZEN_WALK_ANIM_SPEED = 0.1f;
  public static final float CITIZEN_HYPNOTIZED_ANIM_SPEED = 0.2f;

  // SPRITES
  public static final int PLAYER_SHEET_ROWS = 3;
  public static final int PLAYER_SHEET_COLS = 4;
  public static final int CITIZEN_SHEET_ROWS = 4;
  public static final int CITIZEN_SHEET_COLS = 4;
  public static final int BIG_WINEND_ROWS = 2;
  public static final int BIG_WINEND_COLS = 2;
  public static final int BIG_HOME_ROWS = 1;
  public static final int BIG_HOME_COLS = 5;
  public static final float BIG_HOME_SWIRL_SPEED = 0.1f;
}
