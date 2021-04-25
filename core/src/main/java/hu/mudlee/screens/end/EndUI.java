package hu.mudlee.screens.end;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.ui.AbstractUILayout;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.FontSize;
import hu.mudlee.ui.Styles;
import hu.mudlee.util.Asset;
import hu.mudlee.util.GfxUtil;

import static hu.mudlee.Constants.BIG_WINEND_COLS;
import static hu.mudlee.Constants.BIG_WINEND_ROWS;

public class EndUI extends AbstractUILayout {
    public EndUI(boolean won, GameLayer gameLayer, AssetManager assetManager) {
        final var texture = assetManager.get(Asset.BIG_WIN_END_ATLAS.getReference(), Texture.class);
        TextureRegion[][] textureRegions = TextureRegion.split(
          texture,
          texture.getWidth() / BIG_WINEND_COLS,
          texture.getHeight() / BIG_WINEND_ROWS
        );

        final var startButton = new TextButton("Start", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startButton.setChecked(false);
                gameLayer.startGame();
            }
        });
        startButton.pad(15);

        final var restartButton = new TextButton("Restart", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restartButton.setChecked(false);
                gameLayer.restartGame();
            }
        });
        restartButton.pad(15);

        setFillParent(true);
        setBackground(GfxUtil.createBg(1,1, Color.BLACK));
        add(new Image(textureRegions[0][won ? 0 : 1])).growY().width(400).height(400).center();
        row();
        add(restartButton).width(400).pad(15);
        bottom();
    }
}
