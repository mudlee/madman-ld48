package hu.mudlee.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import hu.mudlee.actors.ui.Menu;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.ui.AbstractUILayout;

public class GameUI extends AbstractUILayout {
  final Menu menu;

  public GameUI(GameLayer gameLayer, AssetManager assetManager) {
    setFillParent(true);

    MessageBus.register(Event.GAME_PAUSED, this::showMenu);
    MessageBus.register(Event.GAME_RESUMED, this::hideMenu);

    menu = new Menu(gameLayer);
    menu.setVisible(false);

    // TODO
    //final var resourcePanel = new ResourcePanel(assetManager);

    final var stack = new Stack();
    stack.setFillParent(true);
    //stack.add(resourcePanel);
    stack.add(menu);

    add(stack).grow();
  }

  private void showMenu() {
    // TODO
    //Gdx.app.exit();
    menu.setVisible(true);
  }

  private void hideMenu() {
    setBackground((Drawable) null);
    menu.setVisible(false);
  }
}
