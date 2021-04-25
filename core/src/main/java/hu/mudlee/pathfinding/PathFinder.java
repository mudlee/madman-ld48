package hu.mudlee.pathfinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class PathFinder {
  private final IndexedAStarPathFinder<Node> pathFinder;
  private final Node[][] nodes;

  public PathFinder(int mapWidth, int mapHeight, TiledMapTileLayer walkableLayer) {
    final var builder = new GraphBuilder(mapWidth, mapHeight, walkableLayer);
    builder.build();

    pathFinder = new IndexedAStarPathFinder<>(builder.graph, true);
    nodes = builder.nodes;
  }

  public void searchNodePath(Node startNode, Node endNode, Heuristic<Node> heuristic, GraphPath<Node> path) {
    pathFinder.searchNodePath(startNode, endNode, heuristic, path);
  }

  public Node[][] getNodes() {
    return nodes;
  }
}
