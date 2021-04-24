package hu.mudlee.layers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import hu.mudlee.input.InputManager;
import hu.mudlee.ui.AbstractUILayout;

public class UILayer implements Layer {
    private static AbstractUILayout activeUI;
    private static Stage stage;

    public UILayer(Stage stage) {
        UILayer.stage = stage;
    }

    public static void setUILayout(AbstractUILayout layout) {
        final var prev = activeUI;
        activeUI = layout;
        if(prev!=null) {
            stage.clear();
            prev.dispose();
        }

        System.gc();
        stage.addActor(layout);
    }

    @Override
    public void render() {
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if(activeUI!=null) {
            activeUI.dispose();
        }
    }

    public static InputProcessor getInputProcessor() {
        return stage;
    }
}
