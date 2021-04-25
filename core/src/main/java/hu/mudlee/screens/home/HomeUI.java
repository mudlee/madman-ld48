package hu.mudlee.screens.home;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import hu.mudlee.actors.animators.HomeImageAnimations;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.ui.FontSize;
import hu.mudlee.ui.AbstractUILayout;
import hu.mudlee.ui.Font;
import hu.mudlee.ui.Styles;
import hu.mudlee.util.Asset;
import hu.mudlee.util.GfxUtil;

public class HomeUI extends AbstractUILayout {
    private float animStateTime;
    private final HomeImageAnimations animations;
    private final Image image;

    public HomeUI(GameLayer gameLayer, AssetManager assetManager) {
        final var texture = assetManager.get(Asset.BIG_HOME_ATLAS.getReference(), Texture.class);
        animations = new HomeImageAnimations(texture);

        final var startButton = new TextButton("Start", Styles.menuButton(Font.DEFAULT, FontSize.BTN_LARGE));
        startButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

        image = new Image(animations.startFrame);

        setFillParent(true);
        setBackground(GfxUtil.createBg(1,1, Color.BLACK));
        add(image).growY().width(400).height(400).center();
        row();
        add(startButton).width(400).pad(15);
        row();
        add(quitButton).width(400).pad(15);
        bottom();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        animStateTime += delta;
        image.setDrawable(new TextureRegionDrawable(animations.anim.getKeyFrame(animStateTime, true)));
    }
}
