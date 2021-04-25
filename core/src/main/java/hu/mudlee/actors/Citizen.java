package hu.mudlee.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import hu.mudlee.actors.animators.CitizenAnimations;
import hu.mudlee.pathfinding.ManhattanDistanceHeuristic;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.pathfinding.TestNode;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;
import hu.mudlee.util.Log;

import java.util.Random;

import static hu.mudlee.Constants.*;

public class Citizen extends Group {
  private static final Random random = new Random();
  private final Sprite sprite;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;
  private final DefaultGraphPath<TestNode> path;
  private final String name;
  private final ManhattanDistanceHeuristic heuristic = new ManhattanDistanceHeuristic();
  private final CitizenAnimations animations;
  private final Label msg;
  private boolean showMsg;
  private long showMsgAfter;
  private Vector2 nextTarget;
  private long showMsgTill;
  private int currentPathIndex;
  private float animStateTime;
  private MoveDirection moveDirection = MoveDirection.IDLE;
  private boolean hypnotized;
  private boolean beingHypnotized;

  public Citizen(int index, Texture spritesheet, Array<Vector2> wanderPoints, PathFinder pathfinder) {
    this.name = "Citizen-%d".formatted(index);
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
    path = new DefaultGraphPath<>();

    animations = new CitizenAnimations(spritesheet);
    sprite = new Sprite(animations.idleFrame);

    msg = new Label("4A$#@5!?", Styles.msgBubble(Font.DEFAULT, 8));
    msg.setPosition(getX(), getY());
    addActor(msg);

    initiateNextMsg();
  }

  public void beginHypnotization() {
    Log.debug("%s is being hypnotized".formatted(name));
    stopMoving();
    showMsg = true;
    beingHypnotized = true;
    msg.setText("I'm sorry?");
  }

  public void interruptHypnotization(){
    if(hypnotized) {
      return;
    }

    if(!beingHypnotized) {
      return;
    }

    Log.debug("%s hypnotize interrupted".formatted(name));
    showMsg = false;
    beingHypnotized = false;
  }

  public void hypnotized() {
    showMsg = false;
    hypnotized = true;
  }

  public boolean isHypnotized() {
    return hypnotized;
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    animStateTime += delta;
    sprite.setRegion(getActiveFrame());

    if (beingHypnotized || hypnotized) {
      return;
    }

    if (nextTarget == null) {
      findPathToNextWanderPoint();
      moveTowardsWanderPoint();
    }

    msg.setPosition(getX(), getY() + 15);

    if (!showMsg && TimeUtils.millis() > showMsgAfter && TimeUtils.millis() < showMsgTill) {
      msg.setText(CITIZEN_MESSAGES.get(random.nextInt(CITIZEN_MESSAGES.size())));
      showMsg = true;
    }

    if (TimeUtils.millis() > showMsgTill) {
      showMsg = false;
      initiateNextMsg();
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());

    if (showMsg) {
      msg.draw(batch, parentAlpha);
    }
  }

  private void stopMoving() {
    clearActions();
    path.clear();
    currentPathIndex = 0;
    nextTarget = null;
    moveDirection = MoveDirection.IDLE;
  }

  private void moveTowardsWanderPoint() {
    final var moveFinished = new Action() {
      @Override
      public boolean act(float delta) {
        currentPathIndex++;

        if (currentPathIndex >= path.getCount()) {
          stopMoving();
          return true;
        }

        moveTowardsWanderPoint();
        return true;
      }
    };

    nextTarget = new Vector2(
      path.nodes.get(currentPathIndex).mX * WORLD_UNIT,
      path.nodes.get(currentPathIndex).mY * WORLD_UNIT
    );

    moveDirection = calculateDirection(nextTarget);

    addAction(Actions.sequence(
      Actions.moveTo(nextTarget.x, nextTarget.y, CITIZEN_WALK_SPEED),
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

    return region;
  }

  private Animation<TextureRegion> getActiveAnim() {
    if (hypnotized) {
      return animations.hypnotizedAnim;
    }

    switch (moveDirection) {
      case IDLE -> {
        return animations.idleAnim;
      }
      case RIGHT, LEFT -> {
        return animations.walkHorizontalAnim;
      }
      case UPWARDS, DOWNWARDS -> {
        return animations.walkVerticalAnim;
      }
    }

    Gdx.app.log("PLAYER", "unhandled state: " + moveDirection);
    return animations.idleAnim;
  }

  private void findPathToNextWanderPoint() {
    currentPathIndex = 0;
    path.clear();

    final var wanderPoint = wanderPoints.get(random.nextInt(wanderPoints.size));
    final var startNode = pathfinder.getNodes()[(int) getX() / WORLD_UNIT][(int) getY() / WORLD_UNIT];
    final var endNode = pathfinder.getNodes()[(int) wanderPoint.x][(int) wanderPoint.y];
    pathfinder.searchNodePath(startNode, endNode, heuristic, path);
  }

  private void initiateNextMsg() {
    showMsgAfter = TimeUtils.millis() + random.nextInt(1000 * CITIZEN_MSG_FREQUENCE_SEC);
    showMsgTill = showMsgAfter + random.nextInt(1000 * CITIZEN_MSG_FREQUENCE_SEC);
  }
}
