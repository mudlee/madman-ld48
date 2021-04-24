package hu.mudlee.pathfinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class PathFinder {
  private final IndexedAStarPathFinder<TestNode> pathFinder;
  private final TestNode[][] nodes;
  private final int mapWidth;
  private final int mapHeight;

  public PathFinder(int mapWidth, int mapHeight, TiledMapTileLayer walkableLayer) {
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    final var builder = new GraphBuilder(mapWidth, mapHeight, walkableLayer);
    builder.build();

    pathFinder = new IndexedAStarPathFinder<>(builder.graph, true);
    nodes = builder.nodes;
  }

  public void searchNodePath(TestNode startNode, TestNode endNode, Heuristic<TestNode> heuristic, GraphPath<TestNode> path) {
    pathFinder.searchNodePath(startNode, endNode, heuristic, path);
  }

  public TestNode[][] getNodes() {
    return nodes;
  }

  public int getMapWidth() {
    return mapWidth;
  }

  public int getMapHeight() {
    return mapHeight;
  }
}
