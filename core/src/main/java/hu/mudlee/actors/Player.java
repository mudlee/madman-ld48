package hu.mudlee.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import hu.mudlee.util.Log;

import static hu.mudlee.Constants.WORLD_UNIT;

public class Player extends Actor {
  private enum State {
    IDLE,
    WALKING_RIGHT,
    WALKING_UP,
    WALKING_DOWN,
    WALKING_LEFT,
  }

  private final Sprite sprite;
  private static final int FRAME_COLS = 4;
  private static final int FRAME_ROWS = 2;
  private Animation<TextureRegion> idleAnim;
  private Animation<TextureRegion> walkHorizontallyAnim;
  private float animStateTime;
  private State state = State.IDLE;

  public Player(Texture spritesheet) {
    final var regions = TextureRegion.split(
      spritesheet,
      spritesheet.getWidth() / FRAME_COLS,
      spritesheet.getHeight() / FRAME_ROWS
    );

    animStateTime = 0f;
    createIdleAnim(regions);
    createWalkHorizontallyAnim(regions);

    sprite = new Sprite(regions[0][0]);
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    animStateTime += delta;
    sprite.setRegion(getActiveFrame());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());
  }

  public void walkRight() {
    animStateTime = 0;
    state = State.WALKING_RIGHT;
    Log.debug("Moving Right");
  }

  public void walkLeft() {
    animStateTime = 0;
    state = State.WALKING_LEFT;
    Log.debug("Moving Left");
  }

  public void stop() {
    animStateTime = 0;
    state = State.IDLE;
  }

  public Sprite getSprite() {
    return sprite;
  }

  private TextureRegion getActiveFrame() {
    final var region = getActiveanum().getKeyFrame(animStateTime,true);
    if(state == State.WALKING_LEFT) {
      if(!region.isFlipX()) {
        region.flip(true, false);
      }
    }
    else {
      if(region.isFlipX()) {
        region.flip(true, false);
      }
    }

    return region;
  }

  private Animation<TextureRegion> getActiveanum(){
    switch (state){
      case IDLE -> {
        return idleAnim;
      }
      case WALKING_RIGHT, WALKING_LEFT -> {
        return walkHorizontallyAnim;
      }
    }

    Gdx.app.log("PLAYER", "unhandled state: "+state);
    return idleAnim;
  }

  private void createWalkHorizontallyAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[0][0];
    frames[1] = textureRegions[0][1];
    frames[2] = textureRegions[0][2];
    frames[3] = textureRegions[0][3];

    walkHorizontallyAnim = new Animation<>(0.1f, frames);
  }

  private void createIdleAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[1][0];
    frames[1] = textureRegions[1][1];
    frames[2] = textureRegions[1][2];
    frames[3] = textureRegions[1][3];

    idleAnim = new Animation<>(0.5f, frames);
  }
}
