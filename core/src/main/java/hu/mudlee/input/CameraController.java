package hu.mudlee.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;

public class CameraController extends AbstractInputProcessor {
  private static final float ZOOM_THRESHOLD = 0.001f;
  private static final float ZOOM_SMOOTHNESS = 5f;
  private static final float MAX_ZOOM = 2f;
  private static final float MIN_ZOOM = 0.25f;
  private final OrthographicCamera camera;
  private final Vector2 mouseDragStartAt = new Vector2();
  private Float targetZoom;
  private boolean mouseDrag;
  private boolean disabled;

  public CameraController(OrthographicCamera camera) {
    this.camera = camera;
    MessageBus.register(Event.GAME_PAUSED, () -> {
      mouseDrag = false;
      disabled = true;
    });
    MessageBus.register(Event.GAME_RESUMED, () -> disabled = false);

    this.camera.zoom = 0.5f;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    if(disabled) {
      return false;
    }

    if(button == Input.Buttons.LEFT) {
      mouseDrag = true;
      mouseDragStartAt.set(screenX, screenY);
    }

    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if(disabled) {
      return false;
    }

    if(button == Input.Buttons.LEFT) {
      mouseDrag = false;
    }
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    if(disabled) {
      return false;
    }

    if(mouseDrag) {
      camera.translate(
        (mouseDragStartAt.x - (float)screenX) * camera.zoom,
        -(mouseDragStartAt.y - (float)screenY) * camera.zoom,
        0
      );
      mouseDragStartAt.set(screenX, screenY);
    }
    return false;
  }

  @Override
  public boolean scrolled(float amountX, float amountY) {
    if(disabled) {
      return false;
    }

    if(amountY == 0) {
      return false;
    }

    float oldZoom = camera.zoom;
    targetZoom = clampZoom(oldZoom * (amountY > 0f ? 1.4f : 0.6f));

    return true;
  }

  @Override
  public String getName() {
    return CameraController.class.getName();
  }

  public void update(float delta) {
    if(disabled) {
      return;
    }

    if(targetZoom == null) {
      return;
    }

    float difference = camera.zoom - targetZoom;

    if(Math.abs(difference) < ZOOM_THRESHOLD) {
      targetZoom = null;
    }

    camera.zoom -= difference * delta * ZOOM_SMOOTHNESS;
    camera.update();
  }

  private static float clampZoom(float zoom) {
    return Math.max(Math.min(zoom, MAX_ZOOM), MIN_ZOOM);
  }
}
