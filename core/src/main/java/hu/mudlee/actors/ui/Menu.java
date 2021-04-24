package hu.mudlee.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.FontSize;
import hu.mudlee.ui.UIComponentCreator;
import hu.mudlee.util.GfxUtil;

public class Menu extends Table {
  public Menu(GameLayer gameLayer) {
    final var uiComponentCreator = new UIComponentCreator();

    setFillParent(true);
    setBackground(GfxUtil.createBg(1,1, new Color(0,0,0,0.7f)));

    final var middle = new Table();

    final var quitBtn = uiComponentCreator.textButton("Quit Game", Font.DEFAULT, FontSize.BTN);
    quitBtn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        quitBtn.setChecked(false);
        gameLayer.quitGame();
      }
    });
    quitBtn.pad(15);

    final var restartBtn = uiComponentCreator.textButton("Restart Game",Font.DEFAULT, FontSize.BTN);
    restartBtn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        restartBtn.setChecked(false);
        gameLayer.restartGame();
      }
    });
    restartBtn.pad(15);

    middle.setBackground(GfxUtil.createBg(1, 1, new Color(0,0,0,0.5f)));
    middle.add(restartBtn).growX();
    middle.row();
    middle.add(quitBtn).growX().space(15);
    middle.pad(15);

    add(middle).width(Gdx.graphics.getWidth()/4f).growY();
  }
}
