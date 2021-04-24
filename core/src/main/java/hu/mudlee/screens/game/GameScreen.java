package hu.mudlee.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import hu.mudlee.Constants;
import hu.mudlee.actors.Citizen;
import hu.mudlee.logic.CitizenCreator;
import hu.mudlee.actors.Player;
import hu.mudlee.input.*;
import hu.mudlee.layers.GameLayer;
import hu.mudlee.layers.UILayer;
import hu.mudlee.logic.WanderPointsExtractor;
import hu.mudlee.pathfinding.*;
import hu.mudlee.screens.AbstractScreen;
import hu.mudlee.util.Asset;

public class GameScreen extends AbstractScreen {
  private final Stage stage;
  private final InputManager inputManager;
  private final GameInputProcessor gameInputProcessor;
  private final PlayerController playerController;
  private final TiledMap map;
  private final TiledMapRenderer renderer;
  private final Player player;
  private final OrthographicCamera camera;
  private final Array<Citizen> citizens = new Array<>();

  ShapeRenderer mShapeRenderer;

  public GameScreen(GameLayer gameLayer, InputManager inputManager, AssetManager assetManager) {
    this.inputManager = inputManager;
    this.gameInputProcessor = new GameInputProcessor(gameLayer);
    camera = new OrthographicCamera();
    camera.zoom = 0.2f;
    final var viewport = new ScreenViewport(camera);

    stage = new Stage(viewport);

    UILayer.setUILayout(new GameUI(gameLayer, assetManager));

    map = new TmxMapLoader().load("map.tmx");
    renderer = new OrthogonalTiledMapRenderer(map);

    final var playerSprite = assetManager.get(Asset.PLAYER_ATLAS.getReference(), Texture.class);

    player = new Player(playerSprite);
    playerController = new PlayerController(player, map);

    final var mapLayers = map.getLayers();
    int mapWidth = ((TiledMapTileLayer) mapLayers.get(0)).getWidth();
    int mapHeight = ((TiledMapTileLayer) mapLayers.get(0)).getHeight();

    final var sidewalk= ((TiledMapTileLayer)map.getLayers().get("Sidewalk"));

    // Warping citizens
    mShapeRenderer = new ShapeRenderer();

    final var pathfinder = new PathFinder(mapWidth, mapHeight, sidewalk);

    final var wanderPoints = WanderPointsExtractor.extract(mapLayers);
    final var citizCreator = new CitizenCreator(assetManager, wanderPoints, pathfinder);
    citizens.addAll(citizCreator.create(mapLayers));
  }

  @Override
  public void show() {
    inputManager.addInputProcessor(new GlobalInputProcessor());
    inputManager.addInputProcessor(UILayer.getInputProcessor(), UILayer.class.getName());
    inputManager.addInputProcessor(stage, GameScreen.class.getName());
    inputManager.addInputProcessor(gameInputProcessor);
    //ambient.play();

    final var layer = map.getLayers().get("PlayerWarpPoint");
    float x = layer.getObjects().get("Player").getProperties().get("x", Float.class);
    float y = layer.getObjects().get("Player").getProperties().get("y", Float.class);
    player.setPosition(x,y);

    //Player player = actorCreator.createPlayer(10, 10);
    //inputManager.addInputProcessor(new PlayerController(player));
    stage.addActor(player);
    citizens.forEach(stage::addActor);
  }

  @Override
  public void render(float delta) {
    ScreenUtils.clear(Color.WHITE);

    renderer.setView((OrthographicCamera) stage.getCamera());
    renderer.render();

    boolean moving = false;

    if(Gdx.input.isKeyPressed(Input.Keys.D)) {
      playerController.moveRight(delta);
      moving = true;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.A)) {
      playerController.moveLeft(delta);
      moving = true;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.W)) {
      playerController.moveUp(delta);
      moving = true;
    }
    if(Gdx.input.isKeyPressed(Input.Keys.S)) {
      playerController.moveDown(delta);
      moving = true;
    }
    if(!moving) {
      playerController.stop();
    }

    stage.act(delta);
    stage.draw();

    camera.position.x = Math.round(player.getX());
    camera.position.y = Math.round(player.getY());
    camera.update();

    //cameraController.update(delta);
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width,height, true);
  }

  @Override
  public void hide() {
    dispose();
  }

  @Override
  public void dispose() {
    inputManager.clearInputProcessors();
    //ambient.stop();
    //ambient.dispose();
    stage.dispose();
    map.dispose();
  }
}
