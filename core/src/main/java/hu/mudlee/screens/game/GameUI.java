package hu.mudlee.screens.game;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hu.mudlee.actors.ui.DecibelMeter;
import hu.mudlee.actors.ui.Menu;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.ui.AbstractUILayout;

public class GameUI extends AbstractUILayout {
  private final Menu menu;

  public GameUI(GameLayer gameLayer) {
    setFillParent(true);

    MessageBus.register(Event.GAME_PAUSED, this::showMenu);
    MessageBus.register(Event.GAME_RESUMED, this::hideMenu);

    menu = new Menu(gameLayer);
    menu.setVisible(false);

    final var stack = new Stack();
    stack.setFillParent(true);
    stack.add(new DecibelMeter());
    stack.add(menu);
    add(stack).grow();
  }

  private void showMenu() {
    menu.setVisible(true);
  }

  private void hideMenu() {
    setBackground((Drawable) null);
    menu.setVisible(false);
  }
}
