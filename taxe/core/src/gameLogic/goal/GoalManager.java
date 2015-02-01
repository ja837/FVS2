package gameLogic.goal;

import gameLogic.Game;
import gameLogic.Player;
import gameLogic.map.CollisionStation;
import gameLogic.map.Map;
import gameLogic.map.Station;
import gameLogic.resource.ResourceManager;
import gameLogic.resource.Train;
import gameLogic.goal.dijkstra.Dijkstra;

import java.util.ArrayList;
import java.util.Random;

public class GoalManager {
	public final static int CONFIG_MAX_PLAYER_GOALS = 3;
	private ResourceManager resourceManager;
	private Random random = new Random();
	
	public GoalManager(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	private Goal generateRandom(int turn, Player player) {
		Map map = Game.getInstance().getMap();
		
		Station origin;
		int score = 0;
		do {
			origin = map.getRandomStation();
		} while (origin instanceof CollisionStation);
		
		Station destination;
		do {
			destination = map.getRandomStation();
			// always true, really?
		} while (destination == origin || destination instanceof CollisionStation);
		
		//there is a 2/5 chance of a goal having a via station
		Station via;
		if (random.nextInt(5) == 0 || random.nextInt(5) == 1){
			do {
				via = map.getRandomStation();
			} while (via == origin || via == destination || via instanceof CollisionStation);
		}
		else {
			via = null;
		}
		
		
		//score is based on the minimum path from a->b (or a->b->c)
		if (via  != null){
			score += genScore(origin, via);
			score += genScore(via, destination);
		}
		else {
			score += genScore(origin, destination);
		}
		
		//via = null;
		//score = genScore(origin, destination);
		System.out.println("Score for going from  " + origin.getName() + " to " + destination.getName() + " is " + score);

		
		Goal goal = new Goal(origin, destination, via, turn, score);

		// Goal with a specific train
		if(random.nextInt(2) == 1) {
			//System.out.println(player.getResources().size());
			goal.addConstraint("train", player.getResources().get(random.nextInt(player.getResources().size())).toString());
		}
		
		//goal with any cargo (purely cosmetic)
			//goal.addCargo(random.nextInt(/*CARGOSIZE*/));		

		return goal;
	}
	
	public void addRandomGoalToPlayer(Player player) {
		player.addGoal(generateRandom(player.getPlayerManager().getTurnNumber(), player));
	}

	public ArrayList<String> trainArrived(Train train, Player player) {
		ArrayList<String> completedString = new ArrayList<String>();
		for(Goal goal:player.getGoals()) {
			if(goal.isComplete(train)) {
				player.completeGoal(goal);
				player.removeResource(train);
				completedString.add("Player " + player.getPlayerNumber() + " completed a goal to " + goal.toString() + "!");
			}
		}
		System.out.println("Train arrived to final destination: " + train.getFinalDestination().getName());
		return completedString;
	}
	
	
	private int genScore (Station argOrigin, Station argTarget){
		//Dijkstra.getShortestPathTo(Dijkstra.lookupNode(argTarget));
		//look up in distances array for value
		return (int) (Dijkstra.allDistances[Dijkstra.lookupNode(argOrigin).getCount()][Dijkstra.lookupNode(argTarget).getCount()]);
			
	}
}
