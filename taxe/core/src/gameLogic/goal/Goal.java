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

	//goal cosmetic
	private trainCargo cargo;
	private String cargoString;
	private Random random  = new Random();
	
	public Goal(Station argOrigin, Station argDestination, Station via, int turn, int score) {
		this.origin = argOrigin;
		this.via = via;
		this.destination = argDestination;
		this.turnIssued = turn;
		this.reward = score; //current score
		
		
		int ranNum = random.nextInt(trainCargo.values().length);
		this.cargo = Cargo.getCargo(ranNum);
	}
	
	/**
	 * Add a train constraint to the goal
	 * @param name - pass "train" to add a train constraint
	 * @param value - train to add to goal as constraint.
	 */
	public void addConstraint(String name, String value) {
		if(name.equals("train")) {
			trainName = value;
		} else {
			throw new RuntimeException(name + " is not a valid goal constraint");
		}
	}
	
	/**
	 * Checks if the goal is complete
	 * @param train that may have completed the goal
	 * @return true if goal is completed, else false
	 */
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
	
	/**
	 * Returns a text description of the goal
	 */
	public String toString() {
		String goalString;
		String trainString = "train";
		if(trainName != null) {
			trainString = trainName;
		}

		//goalString = "Send a " + trainString + " carrying a " + cargo.toString().toLowerCase() + " from " + origin.getName() + " to " + destination.getName();

		goalString = "Send a " + trainString + " carrying a " + cargo.toString().toLowerCase() + " from " + origin.getName();
		
		if (via != null){
			goalString += " via " + via.getName();
		}
		goalString +=  " to " + destination.getName();
		
		goalString += " - " + reward + " points";
		
		return goalString;
	}
	
	/** get the string of the filepath for the goal's cargo's image
	 * 
	 * @return
	 */
	public String getImgFile() {
		String imgFilePath = "animal/";
		switch (cargo) {
		case SNAKE:
			imgFilePath += "snake.png";
			break;
		case BEAR:
			imgFilePath += "bear.png";
			break;
		case MONKEY:
			imgFilePath += "monkey.png";
			break;
		case GIRAFFE:
			imgFilePath += "giraffe.png";
			break;
		case PENGUIN:
			imgFilePath += "penguin.png";
			break;
		case SHEEP:
			imgFilePath += "sheep.png";
			break;
		case ELEPHANT:
			imgFilePath += "elephant.png";
			break;
		case OCTOPUS:
			imgFilePath += "octopus.png";
			break;
		case ZEBRA:
			imgFilePath += "zebra.png";
			break;
		case LION:
			imgFilePath += "lion.png";
			break;
		case PIG:
			imgFilePath += "pig.png";
			break;
		case DRAGON:
			imgFilePath += "dragon.png";
			break;
		case YETI:
			imgFilePath += "yeti.png";
			break;
		default:
			imgFilePath += "lion.png";
			break;
		}
		return imgFilePath;
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
	
	public trainCargo getCargo() {
		return cargo;
	}

	public Station getOrigin() {
		return origin;
	}

	public Station getDestination() {
		return destination;
	}
}