package hu.mudlee.actors.ui;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import hu.mudlee.util.Asset;

public class ResourcePanel extends VerticalGroup {
  public ResourcePanel(AssetManager assetManager) {
    final Texture spritesheet = assetManager.get(Asset.TEXTURE_ATLAS.getReference());
    final var tempIcon = new TemperatureIcon(0,0, spritesheet);

    addActor(tempIcon);
  }
}
