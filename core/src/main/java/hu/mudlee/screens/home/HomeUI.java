package hu.mudlee.screens.home;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.ui.FontSize;
import hu.mudlee.ui.AbstractUILayout;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;

public class HomeUI extends AbstractUILayout {
    public HomeUI(GameLayer gameLayer, AssetManager assetManager) {
        final var startButton = new TextButton("Start", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                /*Sound click = assetManager.get(Asset.AUDIO_CLICK.getReference());
                click.play();*/
                startButton.setChecked(false);
                gameLayer.startGame();
            }
        });
        startButton.pad(15);

        final var quitButton = new TextButton("Quit", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
        quitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quitButton.setChecked(false);
                Gdx.app.exit();
            }
        });
        quitButton.pad(15);

        setFillParent(true);
        add(startButton).width(400).pad(15);
        row();
        add(quitButton).width(400).pad(15);
        bottom();
    }
}
