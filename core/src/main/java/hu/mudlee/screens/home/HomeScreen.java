package hu.mudlee.screens.home;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.mudlee.input.CameraController;
import hu.mudlee.input.GlobalInputProcessor;
import hu.mudlee.input.InputManager;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.util.Asset;
import hu.mudlee.layers.UILayer;
import hu.mudlee.screens.AbstractScreen;

public class HomeScreen extends AbstractScreen {
  private final Stage stage;
  private final InputManager inputManager;

  public HomeScreen(GameLayer gameLayer, InputManager inputManager, AssetManager assetManager) {
    this.inputManager = inputManager;
    final var viewport = new ScreenViewport();
    stage = new Stage(viewport);
    UILayer.setUILayout(new HomeUI(gameLayer, assetManager));
  }

  @Override
  public void show() {
    inputManager.addInputProcessor(new GlobalInputProcessor());
    inputManager.addInputProcessor(UILayer.getInputProcessor(), UILayer.class.getName());
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(Color.WHITE);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void hide() {
    dispose();
  }

  @Override
  public void dispose() {
    inputManager.clearInputProcessors();
    stage.dispose();
  }
}
