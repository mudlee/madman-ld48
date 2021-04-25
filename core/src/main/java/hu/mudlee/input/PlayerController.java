package hu.mudlee.input;

import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.Constants;
import hu.mudlee.actors.Player;

public class PlayerController {
  private final Player player;
  private final Array<RectangleMapObject> colliders;
  private final Rectangle TMP_RECT = new Rectangle();
  private boolean halted;

  public PlayerController(Player player, TiledMap map) {
    this.player = player;

    final var collisionLayer = map.getLayers().get("NotWalkable");
    final var colliderObjects = collisionLayer.getObjects();
    colliders = colliderObjects.getByType(RectangleMapObject.class);

    TMP_RECT.width = Constants.WORLD_UNIT;
    TMP_RECT.height = Constants.WORLD_UNIT;
  }

  public void startHypnotize() {
    player.startHypnotize();
  }

  public void stopHypnotize() {
    player.stopHypnotize();
  }

  public void stop() {
    player.stop();
  }

  public void stopForever() {
    stop();
    halted = true;
  }

  public void moveRight(float delta) {
    if(halted) {
      return;
    }
    final var newX = Math.round(player.getX() + Constants.PLAYER_WALK_SPEED * delta);
    if(wouldCollide(newX, player.getY())) {
      return;
    }
    player.walkRight(newX);
  }

  public void moveLeft(float delta) {
    if(halted) {
      return;
    }
    final var newX = Math.round(player.getX() - Constants.PLAYER_WALK_SPEED * delta);
    if(wouldCollide(newX, player.getY())) {
      return;
    }
    player.walkLeft(newX);
  }

  public void moveUp(float delta) {
    if(halted) {
      return;
    }
    final var newY = Math.round(player.getY() + Constants.PLAYER_WALK_SPEED * delta);
    if(wouldCollide(player.getX(), newY)) {
      return;
    }
    player.walkUp(newY);
  }

  public void moveDown(float delta) {
    if(halted) {
      return;
    }
    final var newY = Math.round(player.getY() - Constants.PLAYER_WALK_SPEED * delta);
    if(wouldCollide(player.getX(), newY)) {
      return;
    }
    player.walkDown(newY);
  }

  private boolean wouldCollide(float x, float y) {
    for (RectangleMapObject collider : colliders) {
      final var rectangle = collider.getRectangle();
      player.getSprite().getBoundingRectangle();

      TMP_RECT.x = x;
      TMP_RECT.y = y;

      if(TMP_RECT.overlaps(rectangle)) {
        return true;
      }
    }

    return false;
  }
}
