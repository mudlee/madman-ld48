package hu.mudlee.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import hu.mudlee.Constants;

public class GlobalInputProcessor extends AbstractInputProcessor {
  private boolean altDown;

  @Override
  public boolean keyDown(int keycode) {
    if(keycode == Input.Keys.ALT_LEFT || keycode == Input.Keys.ALT_RIGHT) {
      altDown = true;
    }

    if((keycode == Input.Keys.ENTER || keycode == Input.Keys.NUMPAD_ENTER) && altDown) {
      if(Gdx.graphics.isFullscreen()) {
        Gdx.graphics.setWindowedMode(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
      }
      else {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
      return true;
    }

    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    if(keycode == Input.Keys.ALT_LEFT || keycode == Input.Keys.ALT_RIGHT) {
      altDown = false;
    }

    return false;
  }

  @Override
  public String getName() {
    return GlobalInputProcessor.class.getName();
  }
}
