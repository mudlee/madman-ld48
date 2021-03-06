package hu.mudlee.pathfinding;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class GraphBuilder {
  public final Graph graph = new Graph(20);
  public final Node[][] nodes;
  private final int mapWidth;
  private final int mapHeight;
  private final TiledMapTileLayer walkableLayer;

  public GraphBuilder(int mapWidth, int mapHeight, TiledMapTileLayer walkableLayer) {
    this.mapWidth = mapWidth;
    this.mapHeight = mapHeight;
    this.walkableLayer = walkableLayer;
    nodes = new Node[mapWidth][mapHeight];
  }

  public void build() {
    int index = 0;
    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        if (walkableLayer.getCell(x,y) != null) {
          nodes[x][y] = new Node(x, y, index++);
          graph.addNode(nodes[x][y]);
        }
      }
    }

    for (int x = 0; x < mapWidth; x++) {
      for (int y = 0; y < mapHeight; y++) {
        if (null != nodes[x][y]) {
          addNodeNeighbour(nodes[x][y], x - 1, y, nodes); // Node to left
          addNodeNeighbour(nodes[x][y], x + 1, y, nodes); // Node to right
          addNodeNeighbour(nodes[x][y], x, y - 1, nodes); // Node below
          addNodeNeighbour(nodes[x][y], x, y + 1, nodes); // Node above
        }
      }
    }
  }

  private void addNodeNeighbour(Node aNode, int aX, int aY, Node[][] allNodes) {
    // Make sure that we are within our array bounds.
    if (aX >= 0 && aX < allNodes.length && aY >=0 && aY < allNodes[0].length) {
      aNode.addNeighbour(allNodes[aX][aY]);
    }
  }
}
