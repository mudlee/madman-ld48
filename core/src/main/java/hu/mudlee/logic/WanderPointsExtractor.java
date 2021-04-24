package hu.mudlee.logic;

import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.Constants;

public class WanderPointsExtractor {
  public static Array<Vector2> extract(MapLayers mapLayers) {
    final var citizenWanderPoints = mapLayers.get("CitizenWanderPoints").getObjects();
    final var wanderPoints = new Array<Vector2>();
    for(var i=0;i<citizenWanderPoints.getCount();i++) {
      final var wanderPoint = citizenWanderPoints.get(i);
      wanderPoints.add(new Vector2(
        wanderPoint.getProperties().get("x", Float.class) / Constants.WORLD_UNIT,
        wanderPoint.getProperties().get("y", Float.class) / Constants.WORLD_UNIT
      ));
    }

    return wanderPoints;
  }
}
