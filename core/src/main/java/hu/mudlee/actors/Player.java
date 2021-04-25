package hu.mudlee.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import hu.mudlee.actors.animators.PlayerAnimations;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;
import hu.mudlee.util.Log;

import static hu.mudlee.Constants.*;

public class Player extends Group {
  private final Sprite sprite;
  private final PlayerAnimations animations;
  private final Label msg;
  private final Rectangle TMP_PLAYER_RECT = new Rectangle();
  private final Rectangle TMP_VICTIM_RECT = new Rectangle();
  private final Rectangle TMP_POLICE_RECT = new Rectangle();
  private boolean showMsg;
  private float animStateTime;
  private MoveDirection moveDirection = MoveDirection.IDLE;
  private Array<Citizen> citizens;
  private Array<PoliceCar> policeCars;
  private boolean hypnotizing;
  private Citizen victim;

  public Player(Texture spritesheet) {
    animStateTime = 0f;
    animations = new PlayerAnimations(spritesheet);
    sprite = new Sprite(animations.idleFrame);

    msg = new Label("Close your eyes", Styles.msgBubble(Font.DEFAULT, 8));
    msg.setPosition(getX(), getY());
    addActor(msg);

    TMP_PLAYER_RECT.width = WORLD_UNIT * HYPNOTIZATION_RANGE_UNIT;
    TMP_PLAYER_RECT.height = WORLD_UNIT * HYPNOTIZATION_RANGE_UNIT;
    TMP_VICTIM_RECT.width = WORLD_UNIT * HYPNOTIZATION_RANGE_UNIT;
    TMP_VICTIM_RECT.height = WORLD_UNIT * HYPNOTIZATION_RANGE_UNIT;
    TMP_POLICE_RECT.width = WORLD_UNIT * POLICE_CATCH_RANGE_UNIT;
    TMP_POLICE_RECT.height = WORLD_UNIT * POLICE_CATCH_RANGE_UNIT;
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    animStateTime += delta;
    sprite.setRegion(getActiveFrame());
    msg.setPosition(getX(), getY() + 15);

    if(hypnotizing) {
      actIfAnyPoliceNearby();
    }
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    batch.draw(sprite, getX(), getY(), WORLD_UNIT / 2f, WORLD_UNIT / 2f, WORLD_UNIT, WORLD_UNIT, 1, 1, getRotation());

    if (showMsg) {
      msg.draw(batch, parentAlpha);
    }
  }

  public void setCitizens(Array<Citizen> citizens) {
    this.citizens = citizens;
  }

  public void setPoliceCars(Array<PoliceCar> policeCars) {
    this.policeCars = policeCars;
  }

  public void walkRight(int newX) {
    setX(newX);

    if (moveDirection == MoveDirection.RIGHT) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.RIGHT;
    Log.debug("Moving Right");
  }

  public void walkLeft(int newX) {
    setX(newX);

    if (moveDirection == MoveDirection.LEFT) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.LEFT;
    Log.debug("Moving Left");
  }

  public void walkUp(int newY) {
    setY(newY);

    if (moveDirection == MoveDirection.UPWARDS) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.UPWARDS;
    Log.debug("Moving Up");
  }

  public void walkDown(int newY) {
    setY(newY);

    if (moveDirection == MoveDirection.DOWNWARDS) {
      return;
    }
    animStateTime = 0;
    moveDirection = MoveDirection.DOWNWARDS;
    Log.debug("Moving Down");
  }

  public void startHypnotize() {
    if(hypnotizing) {
      return;
    }

    if(!findNearVictim()) {
      msg.setText("I must go closer");
      showMsg = true;
      return;
    }

    victim.beginHypnotization();
    hypnotizing = true;
    showMsg = true;
    msg.setText(PLAYER_MESSAGES.get(0));

    final var sequence = Actions.sequence();
    for(var i = 1; i<PLAYER_MESSAGES.size(); i++) {
      sequence.addAction(Actions.delay(PLAYER_MESSAGE_FREQUENCY_SEC, getHypnotizeAction(i)));
    }

    sequence.addAction(new Action() {
      @Override
      public boolean act(float delta) {
        victim.hypnotized();
        return true;
      }
    });

    addAction(sequence);
  }

  public void stopHypnotize() {
    showMsg = false;

    if(!hypnotizing) {
      return;
    }

    hypnotizing = false;

    clearActions();
    victim.interruptHypnotization();
  }

  public void stop() {
    animStateTime = 0;
    moveDirection = MoveDirection.IDLE;
  }

  public Sprite getSprite() {
    return sprite;
  }

  private void actIfAnyPoliceNearby() {
    TMP_PLAYER_RECT.setPosition(getX(), getY());
    boolean catched = false;
    for (PoliceCar policeCar : policeCars) {
      if(policeCar.didCatch()) {
        continue;
      }

      TMP_POLICE_RECT.setPosition(policeCar.getX(), policeCar.getY());
      if(TMP_PLAYER_RECT.overlaps(TMP_POLICE_RECT)) {
        Log.debug("Police catches %s".formatted(policeCar.getName()));
        policeCar.catched();
        catched = true;
        break;
      }
    }

    if(catched) {
      for (PoliceCar policeCar : policeCars) {
        if(policeCar.didCatch()) {
          continue;
        }

        policeCar.goToFelonyPlace(policeCar.getX(), policeCar.getY());
      }
    }
  }

  private boolean findNearVictim() {
    TMP_PLAYER_RECT.setPosition(getX(), getY());

    for (Citizen citizen : citizens) {
      if(citizen.isHypnotized()) {
        continue;
      }

      TMP_VICTIM_RECT.setPosition(citizen.getX(), citizen.getY());
      if(TMP_PLAYER_RECT.overlaps(TMP_VICTIM_RECT)) {
        Log.debug("Victim found: %s".formatted(citizen.getName()));
        victim = citizen;
        return true;
      }
    }

    return false;
  }

  private Action getHypnotizeAction(int index) {
    return new Action() {
      @Override
      public boolean act(float delta) {
        msg.setText(PLAYER_MESSAGES.get(index));
        return true;
      }
    };
  }

  private TextureRegion getActiveFrame() {
    final var region = getActiveAnim().getKeyFrame(animStateTime, true);
    if (moveDirection == MoveDirection.LEFT) {
      if (!region.isFlipX()) {
        region.flip(true, false);
      }
    } else {
      if (region.isFlipX()) {
        region.flip(true, false);
      }
    }

    return region;
  }

  private Animation<TextureRegion> getActiveAnim() {
    switch (moveDirection) {
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

    Gdx.app.log("PLAYER", "unhandled state: " + moveDirection);
    return animations.idleAnim;
  }
}
