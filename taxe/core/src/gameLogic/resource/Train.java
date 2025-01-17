package gameLogic.resource;

import Util.Tuple;
import fvs.taxe.actor.TrainActor;
import gameLogic.map.Connection;
import gameLogic.map.IPositionable;
import gameLogic.map.Station;
import gameLogic.resource.Cargo;
import gameLogic.resource.Cargo.trainCargo;

import java.util.ArrayList;
import java.util.List;
/**
 * @author Chris
 */
public class Train extends Resource {
    private String leftImage;
    private String rightImage;
    private IPositionable position;
    private TrainActor actor;
    private int speed;
    // Final destination should be set to null after firing the arrival event
    private Station finalDestination;

    // Should NOT contain current position!
    private List<Station> route;

    //Station name and turn number
    private List<Tuple<String, Integer>> history;
    
    private ArrayList<trainCargo> cargo;


    public Train(String name, String leftImage, String rightImage, int speed) {
        this.name = name;
        this.leftImage = leftImage;
        this.rightImage = rightImage;
        this.speed = speed;
        history = new ArrayList<Tuple<String, Integer>>();
        route =  new ArrayList<Station>();
        cargo = new ArrayList<trainCargo>();
    }
    
    /**
     * Adds cargo to the train
     * @param a The animal to add to the train.
     */
    public void addCargo(trainCargo c){
    	cargo.add(c);
    }
    
    /**
     * Returns a list of all of the cargo on the train.
     * @return 
     */
    public ArrayList<trainCargo> getCargo(){
    	return cargo;
    }
    
    public String getName() {
    	return name;
    }

    public String getLeftImage() {
        return "trains/" + leftImage;
    }

    public void setSpeed(int speed) {
		this.speed = speed;
	}

	public String getRightImage() {
        return "trains/" + rightImage;
    }

    public String getCursorImage() {
        return "trains/cursor/" + leftImage;
    }

    public void setPosition(IPositionable position) {
        this.position = position;
        changed();
    }

    public IPositionable getPosition() {
        return position;
    }

    public void setActor(TrainActor actor) {
        this.actor = actor;
    }

    public TrainActor getActor() {
        return actor;
    }

    public void setRoute(List<Station> route) {
        // Final destination should be set to null after firing the arrival event
        if (route != null && route.size() > 0) finalDestination = route.get(route.size() - 1);

        this.route = route;
    }

    public boolean isMoving() {
        return finalDestination != null;
    }

    public List<Station> getRoute() {
        return route;
    }
    
    /**
     * gives the list of individual connections which make up the train's route
     * @return
     */
    public List<Connection> getRouteConnections() {
    	ArrayList<Connection> lstConns = new ArrayList<Connection>();
    	for (int i = 1; i < route.size(); i++) {
        	Station station1 = route.get(i-1);
        	Station station2 = route.get(i);
        	Connection conn = new Connection(station1, station2);
	        lstConns.add(conn);
        }
    	return lstConns;
    }

    public Station getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(Station station) {
        finalDestination = station;
    }
    
    public int getSpeed() {
        return speed;
    }

    /**
     * Station name and turn number
     * @return Station name and what turn the train arrived at it
     */
    public List<Tuple<String, Integer>> getHistory() {
        return history;
    }

    /**
     * Station name and turn number
     * @param stationName The name of the station just passed
     * @param turn The turn the train passed the station
     */
    public void addHistory(String stationName, int turn) {
        history.add(new Tuple<String, Integer>(stationName, turn));
    }


    @Override
    public void dispose() {
        if (actor != null) {
            actor.remove();
        }
    }
}
