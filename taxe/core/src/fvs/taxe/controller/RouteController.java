package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import fvs.taxe.StationClickListener;
import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.map.CollisionStation;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;

public class RouteController {
    private Context context;
    private Group routingButtons = new Group();
    private List<IPositionable> positions;
    private boolean isRouting = false;
    private Train train;
    private boolean canEndRouting = true;

    public RouteController(Context context) {
        this.context = context;

        StationController.subscribeStationClick(new StationClickListener() {
            @Override
            public void clicked(Station station) {
                if (isRouting) {
                    addStationToRoute(station);
                }
            }
        });
    }
    /**
     * initialise routing: add initial train position to pos list, change game state, display routing controls
     * @param train
     */
    public void begin(Train train) {
        this.train = train;
        isRouting = true;
        positions = new ArrayList<IPositionable>();
        positions.add(train.getPosition());
        context.getGameLogic().setState(GameState.ROUTING);
        addRoutingButtons();

        TrainController trainController = new TrainController(context);
        trainController.setTrainsVisible(train, false);
        train.getActor().setVisible(true);
    }
    /** 
     * gets the most recently selected station, checks it's connected to the last station clicked & adds to pos list if so
     * @param station
     */
    private void addStationToRoute(Station station) {
        // the latest position chosen in the positions so far
        IPositionable lastPosition =  positions.get(positions.size() - 1);
        Station lastStation = context.getGameLogic().getMap().getStationFromPosition(lastPosition);

        boolean hasConnection = context.getGameLogic().getMap().doesConnectionExist(station.getName(), lastStation.getName());

        if(!hasConnection) {
            context.getInfoController().displayFlashMessage("Connection doesn't exist", Color.RED);
        } else {
            positions.add(station.getLocation());
            canEndRouting = !(station instanceof CollisionStation);
        }
    }
    /** 
     * sets GUI's routing buttons
     */
    private void addRoutingButtons() {
        TextButton doneRouting = new TextButton("Route Complete", context.getSkin());
        TextButton cancel = new TextButton("Cancel", context.getSkin());
        doneRouting.setColor(Color.CYAN);
        cancel.setColor(Color.CYAN);

        doneRouting.setPosition(TaxeGame.WIDTH - 225, TaxeGame.HEIGHT - 610);
        cancel.setPosition(TaxeGame.WIDTH - 75, TaxeGame.HEIGHT - 610);
       
        cancel.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                endRouting();
            }
        });

        doneRouting.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!canEndRouting) {
                    context.getInfoController().displayFlashMessage("Route must end at a station", Color.RED);
                    return;
                }

                confirmed();
                endRouting();
            }
        });

        routingButtons.addActor(doneRouting);
        routingButtons.addActor(cancel);

        context.getStage().addActor(routingButtons);
    }

    /** 
     * confirms the route selected by the user and creates it
     */
    private void confirmed() {
        train.setRoute(context.getGameLogic().getMap().createRoute(positions));

        TrainMoveController move = new TrainMoveController(context, train);
    }
    /** 
     * returns game to NORMAL state, removes routing buttons from GUI
     */
    private void endRouting() {
        context.getGameLogic().setState(GameState.NORMAL);
        routingButtons.remove();
        isRouting = false;

        TrainController trainController = new TrainController(context);
        trainController.setTrainsVisible(train, true);
        train.getActor().setVisible(false);
    }
    /**
     * colours the selected parts of the route clicked by the user
     * @param color
     */
    public void drawRoute(Color color) {
        TaxeGame game = context.getTaxeGame();

        IPositionable previousPosition = null;
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(color);

        for(IPositionable position : positions) {
            if(previousPosition != null) {
                game.shapeRenderer.rectLine(previousPosition.getX(), previousPosition.getY(), position.getX(),
                        position.getY(), StationController.CONNECTION_LINE_WIDTH);
            }

            previousPosition = position;
        }

        game.shapeRenderer.end();
    }

}
