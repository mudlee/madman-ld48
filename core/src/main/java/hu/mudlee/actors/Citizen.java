package hu.mudlee.actors;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.pathfinding.ManhattanDistanceHeuristic;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.pathfinding.TestGraph;
import hu.mudlee.pathfinding.TestNode;
import hu.mudlee.util.Log;

import java.util.Random;

import static hu.mudlee.Constants.WORLD_UNIT;

public class Citizen extends Actor {
  private static final int FRAME_COLS = 4;
  private static final int FRAME_ROWS = 2;
  private static final Random random = new Random();
  private final Sprite sprite;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;
  private Vector2 nextTarget;
  private DefaultGraphPath<TestNode> path;

  public Citizen(Texture spritesheet, Array<Vector2> wanderPoints, PathFinder pathfinder) {
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

  @Override
  public void act(float delta) {
    super.act(delta);

    if(nextTarget == null) {
      path.clear();

      final var startNode = pathfinder.getNodes()[(int)getX() / WORLD_UNIT][(int)getY() / WORLD_UNIT];
      final var endNode = pathfinder.getNodes()[(int)wanderPoints.get(0).x][(int)wanderPoints.get(0).y];
      pathfinder.searchNodePath(startNode, endNode, new ManhattanDistanceHeuristic(), path);

      for (TestNode node : path.nodes) {
        node.select();
      }


      Log.debug("Moving to x: %d yP %d".formatted(path.nodes.get(0).mX, path.nodes.get(0).mY));

      nextTarget = wanderPoints.get(random.nextInt(wanderPoints.size));
      addAction(Actions.moveTo(nextTarget.x * WORLD_UNIT, nextTarget.y * WORLD_UNIT, 5f));
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());
  }
}
