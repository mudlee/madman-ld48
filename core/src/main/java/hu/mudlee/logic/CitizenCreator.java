package hu.mudlee.logic;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.actors.Citizen;
import hu.mudlee.pathfinding.PathFinder;
import hu.mudlee.util.Asset;

public class CitizenCreator {
  private final AssetManager assetManager;
  private final Array<Vector2> wanderPoints;
  private final PathFinder pathfinder;

  public CitizenCreator(AssetManager assetManager, Array<Vector2> wanderPoints, PathFinder pathfinder) {
    this.assetManager = assetManager;
    this.wanderPoints = wanderPoints;
    this.pathfinder = pathfinder;
  }

  public Array<Citizen> create(MapLayers mapLayers) {
    final var sprite = assetManager.get(Asset.PLAYER_ATLAS.getReference(), Texture.class);
    final var citizenWarpPoints = mapLayers.get("CitizenWarpPoints").getObjects();

    final var citizens = new Array<Citizen>();

    for(var i=0;i<citizenWarpPoints.getCount();i++) {
      final var warpPoint = citizenWarpPoints.get(i);
      final var citizen = new Citizen(sprite, wanderPoints, pathfinder);
      citizen.setPosition(
        warpPoint.getProperties().get("x", Float.class),
        warpPoint.getProperties().get("y", Float.class)
      );
      citizens.add(citizen);
    }

    return citizens;
  }
}
