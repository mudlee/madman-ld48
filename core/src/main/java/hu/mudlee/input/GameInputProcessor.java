package hu.mudlee.input;

import com.badlogic.gdx.Input;
import hu.mudlee.GameState;
import hu.mudlee.layers.GameLayer;

public class GameInputProcessor extends AbstractInputProcessor {
  private final GameLayer gameLayer;

  public GameInputProcessor(GameLayer gameLayer) {
    this.gameLayer = gameLayer;
  }

  @Override
  public boolean keyDown(int keycode) {
    if(keycode == Input.Keys.ESCAPE){
      if(gameLayer.getState() == GameState.PAUSED) {
        gameLayer.resumeGame();
        return true;
      }

      gameLayer.pauseGame();
      return true;
    }

    return false;
  }

  @Override
  public String getName() {
    return GameInputProcessor.class.getName();
  }
}
