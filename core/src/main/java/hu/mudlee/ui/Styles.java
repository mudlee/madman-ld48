package hu.mudlee.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import hu.mudlee.Constants;
import hu.mudlee.util.GfxUtil;

import java.util.HashMap;
import java.util.Map;

public class Styles {
  private static Skin skin;
  private static final Map<Font, FreeTypeFontGenerator> generatorCache = new HashMap<>();

  public static Label.LabelStyle msgBubble(Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new Label.LabelStyle();
    style.font = generateMsgBubbleFont(font, fontSize);
    style.fontColor = Color.WHITE;
    style.background = GfxUtil.createBg(1,1, Constants.CITIZEN_MSG_BG);

    return style;
  }

  public static Label.LabelStyle instructions(Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new Label.LabelStyle();
    style.font = generateMsgBubbleFont(font, fontSize);
    style.fontColor = Color.WHITE;
    style.background = GfxUtil.createBg(1,1, Color.BLACK);

    return style;
  }

  public static Label.LabelStyle decibel(Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new Label.LabelStyle();
    style.font = generateMsgBubbleFont(font, fontSize);
    style.fontColor = Color.WHITE;
    style.background = GfxUtil.createBg(1,1, Constants.CITIZEN_MSG_BG);

    return style;
  }

  public static TextButton.TextButtonStyle menuButton(Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new TextButton.TextButtonStyle();
    style.up = defaultSkin().newDrawable("white", new Color(0x1a2239ff));
    style.checked = defaultSkin().newDrawable("white", Color.BLUE);
    style.over = defaultSkin().newDrawable("white", new Color(0x8c2b24ff));
    style.font = generateFont(font, fontSize);

    return style;
  }

  public static void dispose() {
    for (FreeTypeFontGenerator generator : generatorCache.values()) {
      generator.dispose();
    }

    if(skin!=null) {
      skin.dispose();
    }
  }

  private static BitmapFont generateFont(Font font, int fontSize) {
    final var parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = fontSize;
    parameter.genMipMaps = false;
    parameter.minFilter = Texture.TextureFilter.Nearest;
    parameter.magFilter = Texture.TextureFilter.Nearest;
    return generatorCache.get(font).generateFont(parameter);
  }

  private static BitmapFont generateMsgBubbleFont(Font font, int fontSize) {
    final var parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = fontSize;
    parameter.genMipMaps = false;
    parameter.minFilter = Texture.TextureFilter.Nearest;
    parameter.magFilter = Texture.TextureFilter.Nearest;
    parameter.padLeft = 5;
    parameter.padRight = 5;
    return generatorCache.get(font).generateFont(parameter);
  }

  private static void ensureFontGenerator(Font font) {
    if(!generatorCache.containsKey(font)) {
      generatorCache.put(font, new FreeTypeFontGenerator(Gdx.files.internal(font.getFont())));
    }
  }

  private static Skin defaultSkin() {
    if(skin == null) {
      skin = new Skin();
      Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
      pixmap.setColor(Color.WHITE);
      pixmap.fill();
      skin.add("white", new Texture(pixmap));
      skin.add("default", new BitmapFont());
    }

    return skin;
  }
}
