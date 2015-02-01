package gameLogic.goal;

import java.util.Random;

import Util.Tuple;
import gameLogic.map.Station;
import gameLogic.resource.Cargo;
import gameLogic.resource.Train;
import gameLogic.resource.Cargo.trainCargo;

public class Goal {
	private Station origin;
	private Station via;
	private Station destination;
	private int turnIssued;
	private boolean complete = false;
	private int reward;
	
	//constraints
	private String trainName = null;
	private String cargo;
	private Random random  = new Random();
	
	public Goal(Station argOrigin, Station argDestination, Station via, int turn, int score) {
		this.origin = argOrigin;
		this.via = via;
		this.destination = argDestination;
		this.turnIssued = turn;
		this.reward = score; //current score
		
		int ranNum= random.nextInt(trainCargo.values().length);
		cargo = Cargo.getCargo(ranNum);
	}
	
	public void addConstraint(String name, String value) {
		if(name.equals("train")) {
			trainName = value;
		} else {
			throw new RuntimeException(name + " is not a valid goal constraint");
		}
	}
	

	public boolean isComplete(Train train) {
		boolean passedOrigin = false;
		boolean passedVia = false;
		
		for(Tuple<String, Integer> history : train.getHistory()) {
			if(history.getFirst().equals(origin.getName()) && history.getSecond() >= turnIssued) {
				passedOrigin = true;
			}
		}
		
		if (via != null){
			for (Tuple<String, Integer> history : train.getHistory()){
				if (history.getFirst().equals(via.getName()) && history.getSecond() >= turnIssued){
					passedVia = true;
				}
			}
			if(train.getFinalDestination() == destination && passedOrigin && passedVia) {
				if(trainName == null || trainName.equals(train.getName())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		else {
			if(train.getFinalDestination() == destination && passedOrigin) {
				if(trainName == null || trainName.equals(train.getName())) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
	}
	
	public String toString() {
		String goalString;
		String trainString = "train";
		if(trainName != null) {
			trainString = trainName;
		}
		goalString = "Send a " + trainString + " carrying a " + cargo.toLowerCase() + " from " + origin.getName() + " to " + destination.getName();
		
		
		if (via != null){
			goalString += " via " + via.getName();
		}
			
		return goalString;
	}

	public void setComplete() {
		complete = true;
	}

	public boolean getComplete() {
		return complete;
	}
	
	public int getReward() {
		return reward;
	}
}