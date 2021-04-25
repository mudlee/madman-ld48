package hu.mudlee.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.actors.animators.PoliceCarAnimations;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.pathfinding.ManhattanDistanceHeuristic;
import hu.mudlee.pathfinding.Node;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.util.Asset;
import hu.mudlee.util.Log;

import java.util.Random;

import static hu.mudlee.Constants.*;

public class PoliceCar extends Group {
  private static final Random random = new Random();
  private final Sprite sprite;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;
  private final DefaultGraphPath<Node> path;
  private final ManhattanDistanceHeuristic heuristic = new ManhattanDistanceHeuristic();
  private final PoliceCarAnimations animations;
  private Vector2 nextTarget;
  private int currentPathIndex;
  private float animStateTime;
  private MoveDirection prevDirection = MoveDirection.IDLE;
  private MoveDirection moveDirection = MoveDirection.IDLE;
  private boolean paused;
  private boolean playerCatched;
  private boolean chasing;
  private boolean goingToTheFelonyPlace;
  private Sound neno;

  public PoliceCar(int index, Texture spritesheet, Array<Vector2> wanderPoints, PathFinder pathfinder, AssetManager assetManager) {
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
    path = new DefaultGraphPath<>();
    setName("Police-%d".formatted(index));

    animations = new PoliceCarAnimations(spritesheet);
    sprite = new Sprite(animations.idleFrame);
    neno = assetManager.get(Asset.NENO.getReference(), Sound.class);
  }

  public void pause() {
    stopMoving();
    paused = true;
  }

  public void resume() {
    paused = false;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (paused) {
      return;
    }

    animStateTime += delta;
    sprite.setRegion(getActiveFrame());

    if (playerCatched) {
      return;
    }

    if (!chasing && nextTarget == null) {
      findPathToNextWanderPoint();
      moveTowardsToNextLocation(POLICE_CAR_MOVE_SPEED);
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT * 2, WORLD_UNIT * 2, 1, 1, getRotation());
  }

  public boolean didCatch() {
    return playerCatched;
  }

  public void catched() {
    Log.debug("%s catched the player at x: %d y: %d".formatted(getName(), (int)getX() / WORLD_UNIT, (int)getY() / WORLD_UNIT));

    final var startAction = new Action() {
      @Override
      public boolean act(float delta) {
        neno.play();
        return true;
      }
    };

    final var stopAction = new Action() {
      @Override
      public boolean act(float delta) {
        neno.stop();
        return true;
      }
    };

    stopMoving();
    addAction(Actions.sequence(
      startAction,
      Actions.delay(4f, stopAction)
    ));
    playerCatched = true;
    MessageBus.broadcast(Event.PLAYER_CATCHED);
    // The best place for this code, I know

    addAction(Actions.delay(3f, new Action() {
      @Override
      public boolean act(float delta) {
        MessageBus.broadcast(Event.GAME_ENDED);
        return true;
      }
    }));
  }

  public void goToFelonyPlace(float x, float y) {
    Log.debug("%s going to the catched player".formatted(getName()));
    stopMoving();
    final var startNode = pathfinder.getNodes()[(int) getX() / WORLD_UNIT][(int) getY() / WORLD_UNIT];
    final var endNode = pathfinder.getNodes()[(int) x / WORLD_UNIT][(int) y / WORLD_UNIT];
    pathfinder.searchNodePath(startNode, endNode, heuristic, path);
    chasing = true;
    goingToTheFelonyPlace = true;
    moveTowardsToNextLocation(POLICE_CAR_MOVE_FAST_SPEED);
  }

  public void stopMoving() {
    clearActions();
    path.clear();
    currentPathIndex = 0;
    nextTarget = null;
    moveDirection = MoveDirection.IDLE;
  }

  private void moveTowardsToNextLocation(float speed) {
    final var moveFinished = new Action() {
      @Override
      public boolean act(float delta) {
        currentPathIndex++;

        if (currentPathIndex >= path.getCount()) {
          stopMoving();
          return true;
        }

        moveTowardsToNextLocation(speed);
        return true;
      }
    };

    nextTarget = new Vector2(
      path.nodes.get(currentPathIndex).mX * WORLD_UNIT,
      path.nodes.get(currentPathIndex).mY * WORLD_UNIT
    );

    prevDirection = moveDirection;
    moveDirection = calculateDirection(nextTarget);

    addAction(Actions.sequence(
      Actions.moveTo(nextTarget.x, nextTarget.y, speed),
      moveFinished
    ));
  }

  private MoveDirection calculateDirection(Vector2 nextTarget) {
    if (nextTarget.x == getX() && nextTarget.y == getY()) {
      return MoveDirection.IDLE;
    } else if (nextTarget.x > getX()) {
      return MoveDirection.RIGHT;
    } else if (nextTarget.x < getX()) {
      return MoveDirection.LEFT;
    } else if (nextTarget.y > getY()) {
      return MoveDirection.UPWARDS;
    } else {
      return MoveDirection.DOWNWARDS;
    }
  }

  private TextureRegion getActiveFrame() {
    final var region = getActiveAnim().getKeyFrame(animStateTime, true);
    if (moveDirection == MoveDirection.LEFT) {
      if (!region.isFlipX()) {
        region.flip(true, false);
      }
    } else {
      if (region.isFlipX()) {
        region.flip(true, false);
      }
    }

    if (moveDirection == MoveDirection.DOWNWARDS) {
      if (!region.isFlipY()) {
        region.flip(false, true);
      }
    } else {
      if (region.isFlipY()) {
        region.flip(false, true);
      }
    }

    return region;
  }

  private Animation<TextureRegion> getActiveAnim() {
    if (playerCatched) {
      if (prevDirection == MoveDirection.LEFT || prevDirection == MoveDirection.RIGHT)
        return animations.driveVerticalCatchAnim;
      else
        return animations.driveHorizontalCatchAnim;
    }

    switch (moveDirection) {
      case IDLE -> {
        if (prevDirection == MoveDirection.LEFT || prevDirection == MoveDirection.RIGHT)
          return animations.driveVerticalAnim;
        else
          return animations.driveHorizontalAnim;
      }
      case RIGHT, LEFT -> {
        if(chasing)
          return animations.driveHorizontalCatchAnim;
        else
          return animations.driveHorizontalAnim;
      }
      case UPWARDS, DOWNWARDS -> {
        if(chasing)
          return animations.driveVerticalCatchAnim;
        else
          return animations.driveVerticalAnim;
      }
    }

    Gdx.app.log("PoliceCar", "unhandled state: " + moveDirection);
    return animations.driveHorizontalAnim;
  }

  private void findPathToNextWanderPoint() {
    currentPathIndex = 0;
    path.clear();

    final var wanderPoint = wanderPoints.get(random.nextInt(wanderPoints.size));
    final var startNode = pathfinder.getNodes()[(int) getX() / WORLD_UNIT][(int) getY() / WORLD_UNIT];
    final var endNode = pathfinder.getNodes()[(int) wanderPoint.x][(int) wanderPoint.y];
    pathfinder.searchNodePath(startNode, endNode, heuristic, path);
  }
}
