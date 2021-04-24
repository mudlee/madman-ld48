package hu.mudlee.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public enum Asset {
  //TEXTURE_TILE("tile.png", Texture.class),
  //AUDIO_HOME_AMBIENT("audio/music/home/ObservingTheStar.ogg", Music.class),
  //AUDIO_CLICK("audio/sound/click/click.wav", Sound.class),
  TEXTURE_ATLAS("spritesheet.png", Texture.class),
  PLAYER_ATLAS("player.png", Texture.class),
  CITIZEN_ATLAS("citizen.png", Texture.class),
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
