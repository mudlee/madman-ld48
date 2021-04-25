package hu.mudlee.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public enum Asset {
  AUDIO_AMBIENT("ambient_game.ogg", Music.class),
  MUMBLING_1("sound/mumbling1.ogg", Sound.class),
  MUMBLING_2("sound/mumbling2.ogg", Sound.class),
  MUMBLING_3("sound/mumbling3.ogg", Sound.class),
  MUMBLING_4("sound/mumbling4.ogg", Sound.class),
  MUMBLING_5("sound/mumbling5.ogg", Sound.class),
  MUMBLING_6("sound/mumbling6.ogg", Sound.class),
  MUMBLING_7("sound/mumbling7.ogg", Sound.class),
  MUMBLING_8("sound/mumbling8.ogg", Sound.class),
  MUMBLING_9("sound/mumbling9.ogg", Sound.class),
  MUMBLING_10("sound/mumbling10.ogg", Sound.class),
  TALK_QUESTION("sound/talk_question.ogg", Sound.class),
  HYPNO("sound/hypno.ogg", Sound.class),
  NENO("sound/neno.ogg", Sound.class),
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
