package hu.mudlee.screens.game;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.TimeUtils;
import hu.mudlee.actors.ui.DecibelMeter;
import hu.mudlee.actors.ui.Menu;
import hu.mudlee.actors.ui.ElapsedTime;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.ui.AbstractUILayout;

public class GameUI extends AbstractUILayout {
  private final Menu menu;
  private final ElapsedTime elapsedTime;
  private long startTime = TimeUtils.millis();
  private boolean paused;

  public GameUI(GameLayer gameLayer) {
    setFillParent(true);

    MessageBus.register(Event.GAME_PAUSED, () -> {
      paused = true;
      showMenu();
    });
    MessageBus.register(Event.GAME_RESUMED,() -> {
      paused = false;
      startTime = TimeUtils.millis();
      hideMenu();
    });

    menu = new Menu(gameLayer);
    menu.setVisible(false);

    elapsedTime = new ElapsedTime();

    final var stack = new Stack();
    stack.setFillParent(true);
    stack.add(new DecibelMeter());
    stack.add(elapsedTime);
    stack.add(menu);
    add(stack).grow();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    if(!paused && TimeUtils.timeSinceMillis(startTime) > 1000L) {
      startTime = TimeUtils.millis();
      elapsedTime.increase();
    }
  }

  private void showMenu() {
    menu.setVisible(true);
  }

  private void hideMenu() {
    setBackground((Drawable) null);
    menu.setVisible(false);
  }
}
