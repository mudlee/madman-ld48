package hu.mudlee.actors.animators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hu.mudlee.Constants.*;

public class PoliceCarAnimations {
  public final Animation<TextureRegion> driveHorizontalAnim;
  public final Animation<TextureRegion> driveVerticalAnim;
  public final Animation<TextureRegion> driveHorizontalCatchAnim;
  public final Animation<TextureRegion> driveVerticalCatchAnim;
  public final TextureRegion idleFrame;

  public PoliceCarAnimations(Texture spritesheet) {
    final var regions = TextureRegion.split(
      spritesheet,
      spritesheet.getWidth() / POLICE_CAR_SHEET_COLS,
      spritesheet.getHeight() / POLICE_CAR_SHEET_ROWS
    );

    idleFrame = regions[0][0];
    driveHorizontalAnim = createDriveHorizontalAnim(regions);
    driveVerticalAnim = createDriveVerticalAnim(regions);
    driveHorizontalCatchAnim = createDriveHorizontalCatchAnim(regions);
    driveVerticalCatchAnim = createDriveVerticalCatchAnim(regions);
  }

  private Animation<TextureRegion> createDriveHorizontalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[1];
    frames[0] = textureRegions[0][0];

    return new Animation<>(POLICE_CAR_ANIM_SPEED, frames);
  }

  private Animation<TextureRegion> createDriveVerticalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[1];
    frames[0] = textureRegions[0][3];

    return new Animation<>(POLICE_CAR_ANIM_SPEED, frames);
  }

  private Animation<TextureRegion> createDriveHorizontalCatchAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[3];
    frames[0] = textureRegions[0][0];
    frames[1] = textureRegions[0][1];
    frames[2] = textureRegions[0][2];

    return new Animation<>(POLICE_CAR_ANIM_SPEED, frames);
  }

  private Animation<TextureRegion> createDriveVerticalCatchAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[3];
    frames[0] = textureRegions[0][3];
    frames[1] = textureRegions[0][4];
    frames[2] = textureRegions[0][5];

    return new Animation<>(POLICE_CAR_ANIM_SPEED, frames);
  }
}
