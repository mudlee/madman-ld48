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
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class UIComponentCreator implements Disposable {
  private Skin skin;
  private final Map<Font, FreeTypeFontGenerator> generatorCache = new HashMap<>();

  public Label label(String text, Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new Label.LabelStyle();
    style.font = generateFont(font, fontSize);
    style.fontColor = new Color(0x7a2b24ff);
    return new Label(text, style);
  }

  public TextButton textButton(String text, Font font, int fontSize) {
    ensureFontGenerator(font);

    final var style = new TextButton.TextButtonStyle();
    style.up = defaultSkin().newDrawable("white", new Color(0x7a2b24ff));
    style.checked = defaultSkin().newDrawable("white", Color.BLUE);
    style.over = defaultSkin().newDrawable("white", new Color(0x8c2b24ff));
    style.font = generateFont(font, fontSize);
    return new TextButton(text, style);
  }

  @Override
  public void dispose() {
    for (FreeTypeFontGenerator generator : generatorCache.values()) {
      generator.dispose();
    }

    if(skin!=null) {
      skin.dispose();
    }
  }

  private Skin defaultSkin() {
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

  private BitmapFont generateFont(Font font, int fontSize) {
    final var parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = fontSize;
    return generatorCache.get(font).generateFont(parameter);
  }

  private void ensureFontGenerator(Font font) {
    if(!generatorCache.containsKey(font)) {
      generatorCache.put(font, new FreeTypeFontGenerator(Gdx.files.internal(font.getFont())));
    }
  }
}
