package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import fvs.taxe.StationClickListener;
import fvs.taxe.TaxeGame;
import fvs.taxe.Tooltip;
import fvs.taxe.actor.CollisionStationActor;
import fvs.taxe.actor.GasPumpActor;
import fvs.taxe.actor.StationActor;
import fvs.taxe.actor.StopActor;
import fvs.taxe.actor.StopSignActor;
import fvs.taxe.dialog.DialogStationMultitrain;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.Player;
import gameLogic.map.CollisionStation;
import gameLogic.map.Connection;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class StationController {
    public final static int CONNECTION_LINE_WIDTH = 5;

    private Context context;
    private Tooltip tooltip;
    /*
    have to use CopyOnWriteArrayList because when we iterate through our listeners and execute
    their handler's method, one case unsubscribes from the event removing itself from this list
    and this list implementation supports removing elements whilst iterating through it
    */
    private static List<StationClickListener> stationClickListeners = new CopyOnWriteArrayList<StationClickListener>();
    private List<Actor> brokenStationActors = new ArrayList<Actor>();		//We store broken station actors to remove them easily
    private List<Station> brokenStations = new ArrayList<Station>();
    private List<Station> gasStations = new ArrayList<Station>();

    public StationController(Context context, Tooltip tooltip) {
        this.context = context;
        this.tooltip = tooltip;
    }

    public static void subscribeStationClick(StationClickListener listener) {
        stationClickListeners.add(listener);
    }

    public static void unsubscribeStationClick(StationClickListener listener) {
        stationClickListeners.remove(listener);
    }

    private static void stationClicked(Station station) {
        for (StationClickListener listener : stationClickListeners) {
            listener.clicked(station);
        }
    }
    
    /**
     * renders the station acronym labels
     */
    public void renderStationLbls() {
        TaxeGame game = context.getTaxeGame();
        List<Station> stations = context.getGameLogic().getMap().getStations();

        for (Station s : stations) {
        	// display station acronym
            game.batch.begin();
            game.fontTiny.setColor(Color.MAROON);
            game.fontTiny.draw(game.batch, s.getAcronym(), s.getLocation().getX() - 5, s.getLocation().getY() - 8);
            game.batch.end(); 
            
        }
    }
    
    /**
     * renders the speed boost at the station
     */
    public void renderStationSpeedModifierLbls() {
        TaxeGame game = context.getTaxeGame();
        List<Station> stations = context.getGameLogic().getMap().getStations();

        for (Station s : stations) {
        	// display station speed modifier
        	
        	if (s.getSpeedModifier() != 0){
        		game.batch.begin();
        		if (s.getSpeedModifier() < 1){
        			game.fontSmall.setColor(Color.MAGENTA);
        		}
        		else{
        			game.fontSmall.setColor(Color.MAGENTA);
        		}          
                game.fontSmall.draw(game.batch, s.getSpeedModifier() + "", s.getLocation().getX() - 30, s.getLocation().getY() + 30);
                game.batch.end();
        	}
        }
    }
    
    private void renderStation(final Station station) {
        final StationActor stationActor = new StationActor(station.getLocation());
        stationActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(Game.getInstance().getState() == GameState.NORMAL){
                	DialogStationMultitrain dia = new DialogStationMultitrain(station, context.getSkin(), context);
                	if(dia.getIsTrain()) {
                		dia.show(context.getStage());
                	}
                }
                stationClicked(station);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                tooltip.setPosition(stationActor.getX() + 20, stationActor.getY() + 20);
                tooltip.show(station.getName());
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                tooltip.hide();
            }
        });

        station.setActor(stationActor);

        context.getStage().addActor(stationActor);
    }

    private void renderCollisionStation(final Station collisionStation) {
    	final CollisionStationActor collisionStationActor = new CollisionStationActor(collisionStation.getLocation());

    	collisionStationActor.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stationClicked(collisionStation);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                tooltip.setPosition(collisionStationActor.getX() + 10, collisionStationActor.getY() + 10);
                tooltip.show("Junction");
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                tooltip.hide();
            }
        });

        context.getStage().addActor(collisionStationActor);
    }
    
    public void tryFixing(){
    	
    }
    /*
    public void renderGasPumps (){
    	List<Station> stations = context.getGameLogic().getMap().getStations();    	
    	for (Station station : stations) {
    		if(gasStations.contains(station) == false){
    			if(station.getSpeedModifier() != 0f){        		
			    	final GasPumpActor stopActor = new GasPumpActor(station.getLocation());
			    	context.getStage().addActor(stopActor);
			    	gasStations.add(station);
        		}
        	}
        }
    }*/
    
    public void renderStop (){
    	List<Station> stations = context.getGameLogic().getMap().getStations();    	
    	for (Station station : stations) {
        	if(station.isPassable() == false){
        		if (brokenStations.contains(station) == false){
			    	final StopActor stopActor = new StopActor(station.getLocation());
			    	context.getStage().addActor(stopActor);
			    	stopActor.setName(station.toString());
			    	brokenStationActors.add(stopActor);
			    	brokenStations.add(station);
        		}
        	}
        }
    }
    
    public void renderControlled (){    	
    	List<Station> stations = context.getGameLogic().getMap().getStations();    	
        for (Station station : stations) {
        	if(station.isControlled() == true){
	        	final StopSignActor stopActor = new StopSignActor(station.getLocation());
	        	context.getStage().addActor(stopActor);
        	}
        }
    }

    public void renderStations() {
        List<Station> stations = context.getGameLogic().getMap().getStations();

        for (Station station : stations) {
        	if(station instanceof CollisionStation) {
        		renderCollisionStation(station);
        	} else {
        		renderStation(station);
        	}
        }
    }
    

    
    /** 
     * renders the connections on the map to be displayed
     * @param connections
     * @param color
     */
    public void renderConnections(List<Connection> connections, Color color) {
        TaxeGame game = context.getTaxeGame();
        // draw all connections on map
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(color);

        for (Connection connection : connections) {
            IPositionable start = connection.getStation1().getLocation();
            IPositionable end = connection.getStation2().getLocation();
            game.shapeRenderer.rectLine(start.getX(), start.getY(), end.getX(), end.getY(), CONNECTION_LINE_WIDTH);
        }
        game.shapeRenderer.end();
    }
    
    /**
     * highlight connections which a user's current trains have travelled / will travel along
     * @param connections
     * @param color
     */
    public void renderRoutedConnections(List<Connection> connections, Color color) {
    	TaxeGame game = context.getTaxeGame();
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(color);
        Player player = context.getGameLogic().getPlayerManager().getCurrentPlayer();
        for(Resource resource : player.getResources()) {
        	if(resource instanceof Train) {
        		if(((Train) resource).getActor() != null) {
        			for (Connection connection : ((Train) resource).getRouteConnections()){
        				IPositionable start = connection.getStation1().getLocation();
        				IPositionable end = connection.getStation2().getLocation();
        				game.shapeRenderer.rectLine(start.getX(), start.getY(), end.getX(), end.getY(), CONNECTION_LINE_WIDTH);
        			}
        		}
        	}
        }
        game.shapeRenderer.end();
    }

    public void displayNumberOfTrainsAtStations() {
    	TaxeGame game = context.getTaxeGame();
		game.batch.begin();
		game.fontSmall.setColor(Color.BLACK);

        for(Station station : context.getGameLogic().getMap().getStations()) {
            if(trainsAtStation(station) > 0) {
                game.fontSmall.draw(game.batch, trainsAtStation(station) + "", (float) station.getLocation().getX() - 6, (float) station.getLocation().getY() + 26);
            }
        }

        game.batch.end();
    }

    private int trainsAtStation(Station station) {
        int count = 0;

        for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
            for(Resource resource : player.getResources()) {
                if(resource instanceof Train) {
                    if(((Train) resource).getActor() != null) {
                        if(((Train) resource).getPosition().equals(station.getLocation())) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }
}
