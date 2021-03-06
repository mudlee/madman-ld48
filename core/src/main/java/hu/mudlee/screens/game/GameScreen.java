package hu.mudlee.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.mudlee.GameState;
import hu.mudlee.actors.Citizen;
import hu.mudlee.actors.PoliceCar;
import hu.mudlee.logic.CitizenCreator;
import hu.mudlee.actors.Player;
import hu.mudlee.input.*;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.layers.UILayer;
import hu.mudlee.logic.PoliceCarCreator;
import hu.mudlee.logic.WanderPointsExtractor;
import hu.mudlee.messaging.Event;
import hu.mudlee.messaging.MessageBus;
import hu.mudlee.pathfinding.*;
import hu.mudlee.screens.AbstractScreen;
import hu.mudlee.util.Asset;

import static hu.mudlee.Constants.*;

public class GameScreen extends AbstractScreen {
  private final Stage stage;
  private final GameLayer gameLayer;
  private final InputManager inputManager;
  private final GameInputProcessor gameInputProcessor;
  private final PlayerController playerController;
  private final TiledMap map;
  private final TiledMapRenderer mapRenderer;
  private final Player player;
  private final OrthographicCamera camera;
  private final Array<Citizen> citizens = new Array<>();
  private final Array<PoliceCar> policeCars = new Array<>();
  private final Color clearColor = new Color(0x1a2239ff);

  public GameScreen(GameLayer gameLayer, InputManager inputManager, AssetManager assetManager) {
    this.gameLayer = gameLayer;
    this.inputManager = inputManager;
    this.gameInputProcessor = new GameInputProcessor(gameLayer);
    camera = new OrthographicCamera();
    camera.zoom = 0.5f;
    final var viewport = new ScreenViewport(camera);

    stage = new Stage(viewport);

    UILayer.setUILayout(new GameUI(gameLayer));

    map = new TmxMapLoader().load("map.tmx");
    mapRenderer = new OrthogonalTiledMapRenderer(map);

    final var playerSprite = assetManager.get(Asset.PLAYER_ATLAS.getReference(), Texture.class);

    player = new Player(playerSprite, assetManager);
    playerController = new PlayerController(player, map);

    final var mapLayers = map.getLayers();
    int mapWidth = ((TiledMapTileLayer) mapLayers.get(0)).getWidth();
    int mapHeight = ((TiledMapTileLayer) mapLayers.get(0)).getHeight();

    final var sidewalk = ((TiledMapTileLayer) map.getLayers().get("Sidewalk"));
    final var road = ((TiledMapTileLayer) map.getLayers().get("Road"));

    // Warping citizens
    final var citizenPathFinder = new PathFinder(mapWidth, mapHeight, sidewalk);
    final var citizenWanderPoints = WanderPointsExtractor.extract(mapLayers, CITIZEN_WANDER_POINTS);
    final var citizenCreator = new CitizenCreator(assetManager, citizenWanderPoints, citizenPathFinder);
    citizens.addAll(citizenCreator.create(mapLayers));

    // Warping police
    final var policePathFinder = new PathFinder(mapWidth, mapHeight, road);
    final var policeWanderPoints = WanderPointsExtractor.extract(mapLayers, POLICE_CAR_WANDER_POINTS);
    final var policeCarCreator = new PoliceCarCreator(assetManager, policeWanderPoints, policePathFinder);
    policeCars.addAll(policeCarCreator.create(mapLayers));

    MessageBus.register(Event.GAME_PAUSED, () -> {
      playerController.stop();
      citizens.forEach(Citizen::pause);
      policeCars.forEach(PoliceCar::pause);
    });
    MessageBus.register(Event.GAME_RESUMED, () -> {
      citizens.forEach(Citizen::resume);
      policeCars.forEach(PoliceCar::resume);
    });
    MessageBus.register(Event.PLAYER_CATCHED, () -> {
      playerController.stopForever();
      citizens.forEach(Citizen::pause);
    });
    MessageBus.register(Event.DECIBEL_GOAL_REACHED, gameLayer::winGame);
    MessageBus.register(Event.GAME_ENDED, gameLayer::loseGame);
  }

  @Override
  public void show() {
    inputManager.addInputProcessor(new GlobalInputProcessor());
    inputManager.addInputProcessor(UILayer.getInputProcessor(), UILayer.class.getName());
    inputManager.addInputProcessor(stage, GameScreen.class.getName());
    inputManager.addInputProcessor(gameInputProcessor);

    final var playerWarp = map.getLayers().get(PLAYER_WARP_POINT);
    float playerWarpX = playerWarp.getObjects().get("Player").getProperties().get("x", Float.class);
    float playerWarpY = playerWarp.getObjects().get("Player").getProperties().get("y", Float.class);
    player.setPosition(playerWarpX, playerWarpY);

    citizens.forEach(stage::addActor);
    policeCars.forEach(stage::addActor);
    stage.addActor(player);
    player.setCitizens(citizens);
    player.setPoliceCars(policeCars);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(clearColor);

    mapRenderer.setView((OrthographicCamera) stage.getCamera());
    mapRenderer.render();

    handlePlayerMovement(delta);

    stage.act(delta);
    stage.draw();

    camera.position.x = Math.round(player.getX());
    camera.position.y = Math.round(player.getY());
    camera.update();
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
    map.dispose();
  }

  private void handlePlayerMovement(float delta) {
    if(gameLayer.getState() == GameState.PAUSED) {
      return;
    }

    boolean moving = false;

    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      playerController.moveRight(delta);
      moving = true;
    } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      playerController.moveLeft(delta);
      moving = true;
    } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
      playerController.moveUp(delta);
      moving = true;
    } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
      playerController.moveDown(delta);
      moving = true;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
      playerController.startHypnotize();
    } else {
      playerController.stopHypnotize();
    }

    if (!moving) {
      playerController.stop();
    }
  }
}
