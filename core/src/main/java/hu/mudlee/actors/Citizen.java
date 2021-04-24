package hu.mudlee.actors;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.pathfinding.ManhattanDistanceHeuristic;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.pathfinding.TestNode;
import hu.mudlee.util.Log;

import java.util.Random;

import static hu.mudlee.Constants.CITIZEN_WALK_SPEED;
import static hu.mudlee.Constants.WORLD_UNIT;

public class Citizen extends Actor {
  private static final int FRAME_COLS = 4;
  private static final int FRAME_ROWS = 2;
  private static final Random random = new Random();
  private final Sprite sprite;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;
  private final DefaultGraphPath<TestNode> path;
  private final String name;
  private final ManhattanDistanceHeuristic heuristic = new ManhattanDistanceHeuristic();
  private Vector2 nextTarget;

  public Citizen(int index, Texture spritesheet, Array<Vector2> wanderPoints, PathFinder pathfinder) {
    this.name = "Citizen-%d".formatted(index);
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
    path = new DefaultGraphPath<>();
    final var regions = TextureRegion.split(
      spritesheet,
      spritesheet.getWidth() / FRAME_COLS,
      spritesheet.getHeight() / FRAME_ROWS
    );

    sprite = new Sprite(regions[0][0]);
  }

  private int currentPathIndex;

  @Override
  public void act(float delta) {
    super.act(delta);

    if (nextTarget == null) {
      findPathToNextWanderPoint();
      moveTowardsWanderPoint();
    }
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

    nextTarget = new Vector2(path.nodes.get(currentPathIndex).mX, path.nodes.get(currentPathIndex).mY);
    addAction(Actions.sequence(
      Actions.moveTo(nextTarget.x * WORLD_UNIT, nextTarget.y * WORLD_UNIT, CITIZEN_WALK_SPEED),
      moveFinished
    ));
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

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());
  }
}
