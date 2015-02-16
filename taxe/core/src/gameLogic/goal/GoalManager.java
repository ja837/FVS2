package gameLogic.goal;

import fvs.taxe.controller.Context;
import gameLogic.Game;
import gameLogic.Player;
import gameLogic.map.CollisionStation;
import gameLogic.map.Map;
import gameLogic.map.Station;
import gameLogic.resource.ResourceManager;
import gameLogic.resource.SoundManager;
import gameLogic.resource.Train;
import gameLogic.goal.dijkstra.Dijkstra;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class GoalManager {
	public final static int CONFIG_MAX_PLAYER_GOALS = 3;
	private ResourceManager resourceManager;
	private SoundManager soundManager;
	private Random random = new Random();
	private final int scoreLimit = 1600;
	
	public GoalManager(Game game) {
		this.resourceManager = game.getResourceManager();
		this.soundManager = game.getSoundManager();
	}

	/**
	 * generates a random goal
	 * @param turn number, is used to tell goal what turn it was issued.
	 * @param player that goal is to be added to. This is used to generate fair goals
	 * @return A random goal
	 */
	private Goal generateRandom(int turn, Player player) {
		Map map = Game.getInstance().getMap();
		int score = 0;
		int score1 = 0;
		Station origin = null;
		Station destination = null;
		Station via = null;
		do {
			origin = map.getRandomStation();
		} while (origin instanceof CollisionStation);
		do {
			destination = map.getRandomStation();
			// always true, really?
		} while (destination == origin || destination instanceof CollisionStation);

		//there is a 2/5 chance of a goal having a via station
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
			score = genScore(origin, via);
			if (score == 0){
				score = genScore(via, origin);
			}
			score1 = genScore(via, destination);
			if (score1 == 0){
				score1 = genScore(destination, via);
			}
			score = score + score1;
		}
		else {
			score = genScore(origin, destination);
			if (score == 0){
				score = genScore(destination, origin);
			}
		}
		
		//rounding to nearest 10
		score = (int) Math.round((double) score/10) * 10;
		
		//string to test goal scores
		String scoreString = "Score for going from  " + origin.getName();
		if(via!=null){
			scoreString += " via " + via.getName();
		}
		scoreString += " to " + destination.getName()  +" is " + score;
		System.out.println(scoreString);
		
		Goal goal = new Goal(origin, destination, via, turn, score);
		
		// Goal with a specific train
		if(random.nextInt(2) == 1) {
			//System.out.println(player.getResources().size());
			goal.addConstraint("train", player.getResources().get(random.nextInt(player.getResources().size())).toString());
		}	

		return goal;
	}
	
	/**
	 * Adds a new random goal to a player, but takes into account their current goals to attempt to give both players goals of similar scores so that the RNG is not too harsh on one player.
	 * @param player to add goal to.
	 */
	public void addRandomGoalToPlayer(Player player) {
		Goal g;
		int total = 0;
		//a players first two goals must add up to less than scoreLimit
		int count = 0;
		for (int i=0; i<player.getGoals().size(); i++){
			if (count < 2){
				total += player.getGoals().get(i).getReward();
				count++;
			}
		}
		int limit = scoreLimit - total;
		if (player.getGoals().size()<3 ){
			do {
				g = generateRandom(player.getPlayerManager().getTurnNumber(), player);
			}
			while (limit - g.getReward() < 0);
		}
		else {
			do {
				g = generateRandom(player.getPlayerManager().getTurnNumber(), player);
			}
			while (g.getReward() < 400 && g.getReward() > 1200);
		}
		player.addGoal(g);
		
		if (player.getGoals().size() < 3){
			soundManager.playAnimal(g.getCargo());
		}
		
	}

	
	public ArrayList<String> trainArrived(Train train, Player player) {
		ArrayList<String> completedString = new ArrayList<String>();
		for(Goal goal:player.getGoals()) {
			if(goal.isComplete(train)) {
				String compString = ("Player " + player.getPlayerNumber() + " completed the goal \nfrom " 
										+ goal.getOrigin() + " to " + goal.getDestination() + "!");
				player.completeGoal(goal);
				player.removeResource(train);
				completedString.add(compString);
				
			}
		}
		System.out.println("Train arrived to final destination: " + train.getFinalDestination().getName());
		return completedString;
	}
	
	/** Look up in the array for the distance between to stations
	 * 
	 * @param argOrigin - origin station
	 * @param argTarget - destination station
	 * @return int - distance between nodes
	 */
	private int genScore (Station argOrigin, Station argTarget){
		//look up in distances array for value
		return (int) (Dijkstra.allDistances[Dijkstra.lookupNode(argOrigin).getCount()][Dijkstra.lookupNode(argTarget).getCount()]);
			
	}

}
