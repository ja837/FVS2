package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;


import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import fvs.taxe.GameScreen;
import fvs.taxe.actor.TrainActor;
import fvs.taxe.controller.InfoController;
import gameLogic.Game;
import gameLogic.Player;
import gameLogic.goal.Goal;
import gameLogic.map.CollisionStation;
import gameLogic.map.IPositionable;
import gameLogic.map.Position;
import gameLogic.map.Station;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;

public class TrainMoveController {
    private Context context;
    private Train train;

    public TrainMoveController(Context context, Train train) {
        this.context = context;
        this.train = train;

        addMoveActions();
    }

    /**
     * an action for the train to run before it starts moving across the screen
     * @return
     */
    private RunnableAction beforeAction() {
        return new RunnableAction() {
            public void run() {
                train.getActor().setVisible(true);
                train.setPosition(new Position(-1, -1));
            }
        };
    }


    /**
     * this action will run every time the train reaches a station within a route
     * @param station
     * @return
     */
    private RunnableAction perStationAction(final Station station) {   	   	    	
    	//contains the methods for border control and junction failure
        return new RunnableAction() {
            public void run() {
                train.addHistory(station.getName(), context.getGameLogic().getPlayerManager().getTurnNumber());
                System.out.println("Added to history: passed " + station.getName() + " on turn "
                        + context.getGameLogic().getPlayerManager().getTurnNumber());
                if (station.isPassable() != true){
            		System.out.println("Train was destroyed passing through here");
            		//context.getTopBarController().displayFlashMessage("Train was destroyed passing through broken junction", Color.RED, 3);
            		Dialog dia = new Dialog("Train Crash", context.getSkin());
                    dia.show(context.getStage());
                    TextButton button = new TextButton("Ok", context.getSkin());
                    dia.text("The train was destroyed passing\nthrough the broken junction");
                    dia.setHeight(125);
                    dia.setWidth(250);
                    dia.setPosition(400, 500);
                    dia.button(button);
                    station.setPassable(false);
            		train.getActor().remove();
                    train.getPlayer().removeResource(train);         
            	}
                
                if (station.isControlled() == true){
            		System.out.println("Passing through a border control zone of " + station.getName());
            		Random rn = new Random();
            		if(rn.nextInt(100) < 20){
            			System.out.println("Illegal animal found on train!");
            			context.getInfoController().displayFlashMessage("A diseased animal was found on your train. It had to be destroyed.", Color.RED, 3);
            			train.getActor().remove();
                        train.getPlayer().removeResource(train);
            		}
            		else{
            			System.out.println("Got through safely");
            			context.getInfoController().displayFlashMessage("Passed through \nborder control safely.", Color.GREEN, 3);
            		}            		
            	}  
                
                for (Goal g : train.getPlayer().getGoals()){
                	if (g.getOrigin().equals(station)){
                		train.addCargo(g.getCargo());
                    	System.out.println(g.getCargo() + " added to " + train.toString()); 
                	}
                }
                
                
                if (station.getSpeedModifier() == 1.4f && train.getName().equals("Nuclear Train")){
                	Game.getInstance().getSoundManager().playHighSpeed();
                }
                
                collisions(station);
            }
        };
    }

    /**
     * an action for the train to run after it has moved the whole route
     * @return
     */
    private RunnableAction afterAction() {
        return new RunnableAction() {
            public void run() {
                ArrayList<String> completedGoals = context.getGameLogic().getGoalManager().trainArrived(train, train.getPlayer());
                for(String message : completedGoals) {
                    context.getInfoController().displayFlashMessage(message, Color.WHITE, 2);
                }
                System.out.println(train.getFinalDestination().getLocation().getX() + "," + train.getFinalDestination().getLocation().getY());
                train.setPosition(train.getFinalDestination().getLocation());
                train.getActor().setVisible(false);
                train.setFinalDestination(null);
            }
        };
    }

    public void addMoveActions() {
        SequenceAction action = Actions.sequence();
        IPositionable current = train.getPosition();
        action.addAction(beforeAction());

        for (final Station station : train.getRoute()) {
        	     	           
        	IPositionable next = station.getLocation();
         
            float duration = getDistance(current, next) / train.getSpeed();
            action.addAction(moveTo(next.getX() - TrainActor.width / 2, next.getY() - TrainActor.height / 2, duration));
            action.addAction(perStationAction(station));
            
            //Adds speed modifier to train here.     
            System.out.println("At station " + station + " train speed = "+ train.getSpeed());		//Debug info for train speeds. 
            if(station.getSpeedModifier() != 0){
            	int newSpeed = (int) (train.getSpeed() * station.getSpeedModifier());
                System.out.println("Train '" + train.toString() + "' speed modified. Old speed = " + train.getSpeed() + ". New speed = " + newSpeed);
                train.setSpeed(newSpeed);
                
            }
            
            current = next;
        }

        action.addAction(afterAction());

        // remove previous actions to be cautious
        train.getActor().clearActions();
        train.getActor().addAction(action);
    }

    private float getDistance(IPositionable a, IPositionable b) {
        return Vector2.dst(a.getX(), a.getY(), b.getX(), b.getY());
    }

    private void collisions(Station station) {
        //test for train collisions at Junction point
        if(!(station instanceof CollisionStation)) {
            return;
        }
        
        
        List<Train> trainsToDestroy = trainsToDestroy();

        if(trainsToDestroy.size() > 0) {
            for(Train trainToDestroy : trainsToDestroy) {
                trainToDestroy.getActor().remove();
                trainToDestroy.getPlayer().removeResource(trainToDestroy);
            }

            Dialog dia = new Dialog("Junction Failure", context.getSkin());
            dia.show(context.getStage());
            TextButton button = new TextButton("Ok", context.getSkin());
            dia.text("Two trains collided at a Junction\nThey were both destroyed\nThe junction failed");
            dia.setHeight(125);
            dia.setWidth(250);
            dia.setPosition(400, 500);
            dia.button(button);
            station.setPassable(false);
        }
    }

    private List<Train> trainsToDestroy() {
        List<Train> trainsToDestroy = new ArrayList<Train>();

        for(Player player : context.getGameLogic().getPlayerManager().getAllPlayers()) {
            for(Resource resource : player.getResources()) {
                if(resource instanceof Train) {
                    Train otherTrain = (Train) resource;
                    if(otherTrain.getActor() == null) continue;
                    if(otherTrain == train) continue;

                    if(train.getActor().getBounds().overlaps(otherTrain.getActor().getBounds())) {
                        //destroy trains that have crashed and burned
                        trainsToDestroy.add(train);
                        trainsToDestroy.add(otherTrain);
                    }

                }
            }
        }

        return trainsToDestroy;
    }
}
