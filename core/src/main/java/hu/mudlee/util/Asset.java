package hu.mudlee.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public enum Asset {
  AUDIO_AMBIENT("ambient_game.ogg", Music.class),
  //AUDIO_CLICK("audio/sound/click/click.wav", Sound.class),
  TEXTURE_ATLAS("spritesheet.png", Texture.class),
  PLAYER_ATLAS("player.png", Texture.class),
  CITIZEN_ATLAS("citizen.png", Texture.class),
  POLICE_CAR_ATLAS("police_car.png", Texture.class),
  BIG_WIN_END_ATLAS("big_winend_images.png", Texture.class),
  BIG_HOME_ATLAS("big_home.png", Texture.class),
  ;

  private final String reference;
  private final Class<?> type;

  Asset(String reference, Class<?> type) {
    this.reference = reference;
    this.type = type;
  }

  public String getReference() {
    return reference;
  }

  public Class<?> getType() {
    return type;
  }
}
