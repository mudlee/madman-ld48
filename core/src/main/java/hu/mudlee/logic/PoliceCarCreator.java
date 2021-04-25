package hu.mudlee.logic;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.Constants;
import hu.mudlee.actors.PoliceCar;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.util.Asset;

public class PoliceCarCreator {
  private final AssetManager assetManager;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;

  public PoliceCarCreator(
    AssetManager assetManager,
    Array<Vector2> wanderPoints,
    PathFinder pathfinder
  ) {
    this.assetManager = assetManager;
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
  }

  public Array<PoliceCar> create(MapLayers mapLayers) {
    final var sprite = assetManager.get(Asset.POLICE_CAR_ATLAS.getReference(), Texture.class);
    final var warpPoints = mapLayers.get(Constants.POLICE_CAR_WARP_POINTS).getObjects();

    final var policeCars = new Array<PoliceCar>();

    for (var i = 0; i < warpPoints.getCount(); i++) {
      final var warpPoint = warpPoints.get(i);
      final var policeCar = new PoliceCar(sprite, wanderPoints, pathfinder);
      policeCar.setPosition(
        warpPoint.getProperties().get("x", Float.class),
        warpPoint.getProperties().get("y", Float.class)
      );
      policeCars.add(policeCar);
    }

    return policeCars;
  }
}
