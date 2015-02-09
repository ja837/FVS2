package gameLogic.goal;

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

public class GoalManager {
	public final static int CONFIG_MAX_PLAYER_GOALS = 3;
	private ResourceManager resourceManager;
	private SoundManager soundManager;
	private Random random = new Random();
	private final int scoreLimit = 1500;
	
	public GoalManager(Game game) {
		this.resourceManager = game.getResourceManager();
		this.soundManager = game.getSoundManager();
	}

	private Goal generateRandom(int turn, Player player, int limitt) {
		Map map = Game.getInstance().getMap();
		int score, score1;
		Station origin = null;
		Station destination = null;
		Station via = null;
		score = scoreLimit + 1;
		while (score > limitt){
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
		}
		
		System.out.println("Score for going from  " + origin.getName() + " to " + destination.getName() /*+ " via " + via.getName()*/ +" is " + score);
		
		Goal goal = new Goal(origin, destination, via, turn, score);
		
		soundManager.playAnimal(goal.getCargo());
 
		// Goal with a specific train
		if(random.nextInt(2) == 1) {
			//System.out.println(player.getResources().size());
			goal.addConstraint("train", player.getResources().get(random.nextInt(player.getResources().size())).toString());
		}	

		return goal;
	}
	
	public void addRandomGoalToPlayer(Player player) {
		int total = 0;
		//players needs roughly same difficulty goals based on score
		for (Goal g : player.getGoals()){
			total += g.getReward();
		}
		int limit = scoreLimit - total;
		player.addGoal(generateRandom(player.getPlayerManager().getTurnNumber(), player, limit));
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
	
	/** Look up in the array for the distance between to stations
	 * 
	 * @param argOrigin
	 * @param argTarget
	 * @return int
	 */
	private int genScore (Station argOrigin, Station argTarget){
		//look up in distances array for value
		return (int) (Dijkstra.allDistances[Dijkstra.lookupNode(argOrigin).getCount()][Dijkstra.lookupNode(argTarget).getCount()]);
			
	}

}
