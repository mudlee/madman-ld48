package hu.mudlee.actors.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.FontSize;
import hu.mudlee.ui.Styles;
import hu.mudlee.util.GfxUtil;

public class Menu extends Table {
  public Menu(GameLayer gameLayer) {
    setFillParent(true);
    setBackground(GfxUtil.createBg(1,1, new Color(0,0,0,0.7f)));

    final var middle = new Table();

    final var quitBtn = new TextButton("Quit Game", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
    quitBtn.addListener(new ClickListener(){
      @Override
      public void clicked(InputEvent event, float x, float y) {
        quitBtn.setChecked(false);
        gameLayer.quitGame();
      }
    });
    quitBtn.pad(15);

    final var restartBtn = new TextButton("Restart Game", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
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
