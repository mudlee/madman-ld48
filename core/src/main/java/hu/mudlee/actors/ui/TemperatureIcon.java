package hu.mudlee.actors.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TemperatureIcon extends Actor {
  private static final float SIZE = 128;
  private static final int FRAME_COLS = 10;
  private static final int FRAME_ROWS = 10;
  private final TextureRegion region;

  public TemperatureIcon(int x, int y, Texture texture) {
    setPosition(x,y);
    setSize(SIZE, SIZE);
    setName("TempIcon");
    setBounds(x*SIZE, y*SIZE, SIZE, SIZE);

    TextureRegion[][] textureRegions = TextureRegion.split(
      texture,
      texture.getWidth() / FRAME_COLS,
      texture.getHeight() / FRAME_ROWS
    );

    region = textureRegions[0][0];
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(region, getX(), getY(),0,0,128,128,1,1,0);
  }
}
