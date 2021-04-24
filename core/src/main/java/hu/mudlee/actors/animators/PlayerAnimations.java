package hu.mudlee.actors.animators;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static hu.mudlee.Constants.*;

public class PlayerAnimations {
  public final Animation<TextureRegion> idleAnim;
  public final Animation<TextureRegion> walkHorizontalAnim;
  public final Animation<TextureRegion> walkVerticalAnim;
  public final TextureRegion idleFrame;

  public PlayerAnimations(Texture spritesheet) {
    final var regions = TextureRegion.split(
      spritesheet,
      spritesheet.getWidth() / PLAYER_SHEET_COLS,
      spritesheet.getHeight() / PLAYER_SHEET_ROWS
    );

    idleFrame = regions[0][0];
    idleAnim = createIdleAnim(regions);
    walkHorizontalAnim = createWalkHorizontalAnim(regions);
    walkVerticalAnim = createWalkVerticalAnim(regions);
  }

  private Animation<TextureRegion> createWalkHorizontalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[0][0];
    frames[1] = textureRegions[0][1];
    frames[2] = textureRegions[0][2];
    frames[3] = textureRegions[0][3];

    return new Animation<>(PLAYER_WALK_ANIM_SPEED, frames);
  }

  private Animation<TextureRegion> createIdleAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[1][0];
    frames[1] = textureRegions[1][1];
    frames[2] = textureRegions[1][2];
    frames[3] = textureRegions[1][3];

    return new Animation<>(PLAYER_IDLE_ANIM_SPEED, frames);
  }

  private Animation<TextureRegion> createWalkVerticalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[2][0];
    frames[1] = textureRegions[2][1];
    frames[2] = textureRegions[2][2];
    frames[3] = textureRegions[2][3];

    return new Animation<>(PLAYER_WALK_ANIM_SPEED, frames);
  }
}
