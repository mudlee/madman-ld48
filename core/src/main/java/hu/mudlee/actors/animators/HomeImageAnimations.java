package hu.mudlee.actors.animators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hu.mudlee.Constants.*;

public class HomeImageAnimations {
  public final Animation<TextureRegion> anim;
  public final TextureRegion startFrame;

  public HomeImageAnimations(Texture spritesheet) {
    final var regions = TextureRegion.split(
      spritesheet,
      spritesheet.getWidth() / BIG_HOME_COLS,
      spritesheet.getHeight() / BIG_HOME_ROWS
    );

    anim = createAnim(regions);
    startFrame = regions[0][0];
  }

  private Animation<TextureRegion> createAnim(TextureRegion[][] regions) {
    TextureRegion[] frames = new TextureRegion[5];
    frames[0] = regions[0][0];
    frames[1] = regions[0][1];
    frames[2] = regions[0][2];
    frames[3] = regions[0][3];
    frames[4] = regions[0][4];

    return new Animation<>(BIG_HOME_SWIRL_SPEED, frames);
  }
}
