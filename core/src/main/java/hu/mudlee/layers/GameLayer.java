package hu.mudlee.layers;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import hu.mudlee.GameState;
import hu.mudlee.input.InputManager;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.screens.game.GameScreen;
import hu.mudlee.screens.home.HomeScreen;
import hu.mudlee.screens.loading.LoadingScreen;
import hu.mudlee.util.Asset;
import hu.mudlee.util.Log;

import java.util.Arrays;

public class GameLayer extends Game implements Layer {
  private final AssetManager assetManager;
  private final InputManager inputManager;
  private boolean assetsLoaded;
  private float gameLoadingProgress;
  private GameState state = GameState.NOT_RUNNING;

  public GameLayer(AssetManager assetManager, InputManager inputManager) {
    this.assetManager = assetManager;
    this.inputManager = inputManager;
  }

  @Override
  public void create() {
    Gdx.input.setInputProcessor(inputManager.getMultiplexer());
  }

  @Override
  public void render() {
    super.render();

    if(!assetsLoaded) {
      final var loadingScreen = (LoadingScreen)screen;
      if(assetManager.update() && !loadingScreen.isAnimating()) {
        assetsLoaded = true;
        //setScreen(new HomeScreen(this, inputManager, assetManager));
        // TODO
        setScreen(new GameScreen(this, inputManager, assetManager));
      }

      gameLoadingProgress = assetManager.getProgress();
    }
  }

  @Override
  public void setScreen(Screen screen) {
    System.gc();
    Log.debug("Switching screen to "+screen.getClass().getName());
    super.setScreen(screen);
  }

  @Override
  public void dispose() {
    super.dispose();
    assetManager.dispose();
  }

  public void pauseGame() {
    Log.debug("Game paused");
    state = GameState.PAUSED;
    MessageBus.broadcast(Event.GAME_PAUSED);
  }

  public void resumeGame() {
    Log.debug("Game resumed");
    state = GameState.RUNNING;
    MessageBus.broadcast(Event.GAME_RESUMED);
  }

  public void startGame() {
    Log.debug("Game started");
    state = GameState.RUNNING;
    setScreen(new GameScreen(this, inputManager, assetManager));
  }

  public void restartGame() {
    Log.debug("Game restarted");
    state = GameState.RUNNING;
    setScreen(new GameScreen(this, inputManager, assetManager));
  }

  public void quitGame() {
    Log.debug("Game quit");
    state = GameState.NOT_RUNNING;
    setScreen(new HomeScreen(this, inputManager, assetManager));
  }

  public void loadGame() {
    setScreen(new LoadingScreen(this));
    Arrays.stream(Asset.values()).forEach(asset -> assetManager.load(asset.getReference(), asset.getType()));
  }

  public float getGameLoadingProgress() {
    return gameLoadingProgress;
  }

  public GameState getState() {
    return state;
  }
}
