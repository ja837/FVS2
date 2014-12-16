package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fvs.taxe.dialog.DialogResourceTrain;
import gameLogic.Game;
import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.goal.Goal;
import gameLogic.map.Connection;
import gameLogic.map.IPositionable;
import gameLogic.map.Map;
import gameLogic.map.Station;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class GameScreen extends ScreenAdapter {
    final private TaxeGame game;
    private OrthographicCamera camera;
    private Stage stage; // Stage - Holds all actors
    private Texture mapTexture;
    private Map map;
    private Game gameLogic;
    private Skin skin;
    private Group resourceButtons = new Group();


    public GameScreen(TaxeGame game) {
        this.game = game;

        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false); // Makes the origin to be in the lower left corner
        stage = new Stage();
        mapTexture = new Texture(Gdx.files.internal("gamemap.png"));
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Gdx.input.setInputProcessor(stage);

        // game logic stuff
        map = new Map();
        gameLogic = Game.getInstance();
    }


    //Display all stations
    private void renderStations() {
        for (Station station : map.getStations()) {
            renderStation(station);
        }
    }

    //Display individual station
    private void renderStation(Station station) {
        IPositionable location = station.getLocation();
        MapActor actor = new MapActor(location.getX(), location.getY(), station);
        actor.setTouchable(Touchable.enabled);
        stage.addActor(actor);
    }


    //Display all connections
    private void renderConnections() {
        int lineWidth = 5;
        game.shapeRenderer.begin(ShapeType.Filled);
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.setProjectionMatrix(camera.combined);

        for (Connection connection : map.getConnections()) {
            IPositionable start = connection.getStation1().getLocation();
            IPositionable end = connection.getStation2().getLocation();
            game.shapeRenderer.rectLine(start.getX(), start.getY(), end.getX(), end.getY(), lineWidth);
        }
        game.shapeRenderer.end();
    }



    private void drawResourcesHeader() {
        game.batch.begin();
        game.fontSmall.setColor(Color.BLACK);
        game.fontSmall.draw(game.batch, "Resources:", 10.0f, (float) TaxeGame.HEIGHT - 250.0f);
        game.fontSmall.draw(game.batch, "FPS:" + Gdx.graphics.getFramesPerSecond(), (float) TaxeGame.WIDTH - 100.0f, (float) TaxeGame.HEIGHT - 10.0f);
        game.batch.end();
    }

    private void showCurrentPlayerResources() {

        float top = (float) TaxeGame.HEIGHT;
        float x = 10.0f;
        float y = top - 250.0f;
        y -= 100;

        final Player currentPlayer = gameLogic.getPlayerManager().getCurrentPlayer();

        resourceButtons.remove();
        resourceButtons.clear();

        for (final Resource r : currentPlayer.getResources()) {
            TextButton button = new TextButton(r.toString(), skin);
            button.setPosition(x, y);
            button.addListener(new ClickListener() {
                public void clicked(InputEvent event, float x, float y) {
                    System.out.print("res button clicked");

                    DialogResourceTrain dia = new DialogResourceTrain(r.toString(), skin);
                    dia.dropTrainArgs(currentPlayer, (Train) r);
                    dia.show(stage);
                }
            });

            resourceButtons.addActor(button);

            y -= 30;
        }

        stage.addActor(resourceButtons);
    }

    private List<String> playerGoalStrings() {
        ArrayList<String> strings = new ArrayList<String>();
        PlayerManager pm = gameLogic.getPlayerManager();
        Player currentPlayer = pm.getCurrentPlayer();

        for (Goal g : currentPlayer.getGoals()) {
            strings.add(g.toString());
        }

        return strings;
    }

    private void showCurrentPlayerGoals() {
        game.batch.begin();
        float top = (float) TaxeGame.HEIGHT;
        game.fontSmall.setColor(Color.BLACK);
        float x = 10.0f;
        float y = top - 10.0f;

        String playerGoals = "Current Player (" + gameLogic.getPlayerManager().getCurrentPlayer().toString() + ") Goals:";
        game.fontSmall.draw(game.batch, playerGoals, x, y);

        for (String s : playerGoalStrings()) {
            y -= 30;
            game.fontSmall.draw(game.batch, s, x, y);
        }

        game.batch.end();
    }

    private void renderTrain() {
        Image dummyTrain = new Image(new Texture(Gdx.files.internal("BulletTrain.png")));
        dummyTrain.setPosition(155, 45);
        dummyTrain.setSize(100f, 100f);
        dummyTrain.addAction(sequence(moveTo(340f, 290f, 5f), moveTo(560, 390, 5f), moveTo(245, 510, 5f)));
        stage.addActor(dummyTrain);
    }

    // you can read about the debug keys and their functionality in the GitHub wiki
    private void debugKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.G)) {
            gameLogic.getGoalManager().givePlayerGoal(gameLogic.getPlayerManager().getCurrentPlayer());
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            gameLogic.getPlayerManager().turnOver();
            showCurrentPlayerResources();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            gameLogic.getResourceManager().addRandomResourceToPlayer(gameLogic.getPlayerManager().getCurrentPlayer());
            showCurrentPlayerResources();
        }


    }

    // called every frame
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(mapTexture, 0, 0);
        game.batch.end();

        renderConnections();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();



        // text must be rendered after the stage so the bg image doesn't overlap
        debugKeys();
        showCurrentPlayerGoals();
        drawResourcesHeader();
        // showCurrentPlayerResources();
    }

    @Override
    // Called when GameScreen becomes current screen of the game
    public void show() {
        renderStations();
        renderTrain();
    }


    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        mapTexture.dispose();
        stage.dispose();
    }

}