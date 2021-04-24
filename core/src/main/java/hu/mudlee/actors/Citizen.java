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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.actors.animators.CitizenAnimations;
import hu.mudlee.pathfinding.ManhattanDistanceHeuristic;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.pathfinding.TestNode;
import hu.mudlee.util.Log;

import java.util.Random;

import static hu.mudlee.Constants.*;

public class Citizen extends Actor {
  private static final Random random = new Random();
  private final Sprite sprite;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;
  private final DefaultGraphPath<TestNode> path;
  private final String name;
  private final ManhattanDistanceHeuristic heuristic = new ManhattanDistanceHeuristic();
  private final CitizenAnimations animations;
  private Vector2 nextTarget;
  private int currentPathIndex;
  private float animStateTime;
  // TODO
  private MoveDirection moveDirection = MoveDirection.IDLE;

  public Citizen(int index, Texture spritesheet, Array<Vector2> wanderPoints, PathFinder pathfinder) {
    this.name = "Citizen-%d".formatted(index);
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
    path = new DefaultGraphPath<>();

    animations = new CitizenAnimations(spritesheet);
    sprite = new Sprite(animations.idleFrame);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    animStateTime += delta;
    sprite.setRegion(getActiveFrame());

    if (nextTarget == null) {
      findPathToNextWanderPoint();
      moveTowardsWanderPoint();
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());
  }

  private void moveTowardsWanderPoint() {
    final var moveFinished = new Action() {
      @Override
      public boolean act(float delta) {
        currentPathIndex++;

        if (currentPathIndex >= path.getCount()) {
          path.clear();
          currentPathIndex = 0;
          nextTarget = null;
          return true;
        }

        moveTowardsWanderPoint();
        return true;
      }
    };

    nextTarget = new Vector2(
      path.nodes.get(currentPathIndex).mX * WORLD_UNIT,
      path.nodes.get(currentPathIndex).mY* WORLD_UNIT
    );

    moveDirection = calculateDirection(nextTarget);

    addAction(Actions.sequence(
      Actions.moveTo(nextTarget.x, nextTarget.y, CITIZEN_WALK_SPEED),
      moveFinished
    ));
  }

  private MoveDirection calculateDirection(Vector2 nextTarget) {
    if(nextTarget.x == getX() && nextTarget.y == getY()) {
      return MoveDirection.IDLE;
    }
    else if(nextTarget.x > getX()) {
      return MoveDirection.RIGHT;
    }
    else if(nextTarget.x < getX()) {
      return MoveDirection.LEFT;
    }
    else if(nextTarget.y > getY()) {
      return MoveDirection.UPWARDS;
    }
    else {
      return MoveDirection.DOWNWARDS;
    }
  }

  private TextureRegion getActiveFrame() {
    final var region = getActiveAnim().getKeyFrame(animStateTime,true);
    if(moveDirection == MoveDirection.LEFT) {
      if(!region.isFlipX()) {
        region.flip(true, false);
      }
    }
    else {
      if(region.isFlipX()) {
        region.flip(true, false);
      }
    }

    return region;
  }

  private Animation<TextureRegion> getActiveAnim(){
    switch (moveDirection){
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

    Gdx.app.log("PLAYER", "unhandled state: "+ moveDirection);
    return animations.idleAnim;
  }

  private void findPathToNextWanderPoint() {
    currentPathIndex = 0;
    path.clear();

    final var wanderPoint = wanderPoints.get(random.nextInt(wanderPoints.size));
    final var startNode = pathfinder.getNodes()[(int) getX() / WORLD_UNIT][(int) getY() / WORLD_UNIT];
    final var endNode = pathfinder.getNodes()[(int) wanderPoint.x][(int) wanderPoint.y];
    pathfinder.searchNodePath(startNode, endNode, heuristic, path);
    Log.debug("%s - Next wander point: x: %d, y: %d".formatted(name, endNode.mX, endNode.mY));
  }
}
