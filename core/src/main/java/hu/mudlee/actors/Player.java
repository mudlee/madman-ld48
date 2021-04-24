package hu.mudlee.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import hu.mudlee.actors.animators.PlayerAnimations;
import hu.mudlee.util.Log;

import static hu.mudlee.Constants.*;

public class Player extends Actor {
  private final Sprite sprite;
  private float animStateTime;
  private final PlayerAnimations animations;
  private MoveDirection moveDirection = MoveDirection.IDLE;

  public Player(Texture spritesheet) {
    animStateTime = 0f;
    animations = new PlayerAnimations(spritesheet);
    sprite = new Sprite(animations.idleFrame);
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

    if(moveDirection == MoveDirection.RIGHT) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.RIGHT;
    Log.debug("Moving Right");
  }

  public void walkLeft(int newX) {
    setX(newX);

    if(moveDirection == MoveDirection.LEFT) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.LEFT;
    Log.debug("Moving Left");
  }

  public void walkUp(int newY) {
    setY(newY);

    if(moveDirection == MoveDirection.UPWARDS) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.UPWARDS;
    Log.debug("Moving Up");
  }

  public void walkDown(int newY) {
    setY(newY);

    if(moveDirection == MoveDirection.DOWNWARDS) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.DOWNWARDS;
    Log.debug("Moving Down");
  }

  public void stop() {
    animStateTime = 0;
    moveDirection = MoveDirection.IDLE;
  }

  public Sprite getSprite() {
    return sprite;
  }

  private TextureRegion getActiveFrame() {
    final var region = getActiveAnim().getKeyFrame(animStateTime,true);
    if(moveDirection == MoveDirection.LEFT) {
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

  private Animation<TextureRegion> getActiveAnim(){
    switch (moveDirection){
      case IDLE -> {
        return animations.idleAnim;
      }
      case RIGHT, LEFT -> {
        return animations.walkHorizontalAnim;
      }
      case UPWARDS, DOWNWARDS -> {
        return animations.walkVerticalAnim;
      }
    }

    Gdx.app.log("PLAYER", "unhandled state: "+ moveDirection);
    return animations.idleAnim;
  }
}
