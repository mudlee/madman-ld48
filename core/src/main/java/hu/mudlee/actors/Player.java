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
    WALKING_UPWARDS,
    WALKING_DOWNWARDS,
    WALKING_LEFT,
  }

  private final Sprite sprite;
  private static final int FRAME_COLS = 4;
  private static final int FRAME_ROWS = 3;
  private Animation<TextureRegion> idleAnim;
  private Animation<TextureRegion> walkHorizontalAnim;
  private Animation<TextureRegion> walkVerticalAnim;
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
    createWalkHorizontalAnim(regions);
    createWalkVerticalAnim(regions);

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

  public void walkRight(int newX) {
    setX(newX);

    if(state == State.WALKING_RIGHT) {
      return;
    }
    animStateTime = 0;
    state = State.WALKING_RIGHT;
    Log.debug("Moving Right");
  }

  public void walkLeft(int newX) {
    setX(newX);

    if(state == State.WALKING_LEFT) {
      return;
    }
    animStateTime = 0;
    state = State.WALKING_LEFT;
    Log.debug("Moving Left");
  }

  public void walkUp(int newY) {
    setY(newY);

    if(state == State.WALKING_UPWARDS) {
      return;
    }
    animStateTime = 0;
    state = State.WALKING_UPWARDS;
    Log.debug("Moving Up");
  }

  public void walkDown(int newY) {
    setY(newY);

    if(state == State.WALKING_DOWNWARDS) {
      return;
    }
    animStateTime = 0;
    state = State.WALKING_DOWNWARDS;
    Log.debug("Moving Down");
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
        return walkHorizontalAnim;
      }
      case WALKING_UPWARDS, WALKING_DOWNWARDS -> {
        return walkVerticalAnim;
      }
    }

    Gdx.app.log("PLAYER", "unhandled state: "+state);
    return idleAnim;
  }

  private void createWalkHorizontalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[0][0];
    frames[1] = textureRegions[0][1];
    frames[2] = textureRegions[0][2];
    frames[3] = textureRegions[0][3];

    walkHorizontalAnim = new Animation<>(0.1f, frames);
  }

  private void createIdleAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[1][0];
    frames[1] = textureRegions[1][1];
    frames[2] = textureRegions[1][2];
    frames[3] = textureRegions[1][3];

    idleAnim = new Animation<>(0.5f, frames);
  }

  private void createWalkVerticalAnim(TextureRegion[][] textureRegions) {
    TextureRegion[] frames = new TextureRegion[4];
    frames[0] = textureRegions[2][0];
    frames[1] = textureRegions[2][1];
    frames[2] = textureRegions[2][2];
    frames[3] = textureRegions[2][3];

    walkVerticalAnim = new Animation<>(0.1f, frames);
  }
}
