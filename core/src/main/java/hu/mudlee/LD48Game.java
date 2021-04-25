package hu.mudlee;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.mudlee.input.InputManager;
import hu.mudlee.layers.*;
import hu.mudlee.ui.Styles;

public class LD48Game implements ApplicationListener {
  private final LayerStack layerStack;

  public LD48Game() {
    layerStack = new LayerStack();
  }

  @Override
  public void create() {
    Gdx.app.setLogLevel(Application.LOG_DEBUG);

    final var inputManager = new InputManager();
    final var assetManager = new AssetManager();

    final var uiViewport = new ScreenViewport();
    final var uiStage = new Stage(uiViewport);

    final var gameLayer = new GameLayer(assetManager, inputManager);

    layerStack.addLayer(gameLayer);
    layerStack.addLayer(new UILayer(uiStage));
    //layerStack.addLayer(new DebugInfoLayer());
    layerStack.create();

    gameLayer.loadGame();
  }

  @Override
  public void dispose() {
    layerStack.dispose();
    Styles.dispose();
  }

  @Override
  public void pause() {
    layerStack.pause();
  }

  @Override
  public void resume() {
    layerStack.resume();
  }

  @Override
  public void render() {
    layerStack.render();
  }

  @Override
  public void resize(int width, int height) {
    layerStack.resize(width, height);
  }
}