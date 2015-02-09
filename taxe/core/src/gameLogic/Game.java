package gameLogic;

import gameLogic.goal.GoalManager;
import gameLogic.map.Map;
import gameLogic.map.Station;
import gameLogic.resource.ResourceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {
	private static Game instance;
	private PlayerManager playerManager;
	private GoalManager goalManager;
	private ResourceManager resourceManager;
	private Map map;
	private GameState state;
	private List<GameStateListener> gameStateListeners = new ArrayList<GameStateListener>();

	private final int CONFIG_PLAYERS = 2;
	public static int TOTAL_TURNS = 30;
	
	public static void changeTurns(int n) {
		TOTAL_TURNS = n;
	};

	private Game() {
		playerManager = new PlayerManager();
		playerManager.createPlayers(CONFIG_PLAYERS);

		resourceManager = new ResourceManager();
		goalManager = new GoalManager(resourceManager);
		
		map = new Map();
		
		state = GameState.NORMAL;

		playerManager.subscribeTurnChanged(new TurnListener() {
			@Override
			public void changed() {
				Player currentPlayer = playerManager.getCurrentPlayer();
				resourceManager.addRandomResourceToPlayer(currentPlayer);
				resourceManager.addRandomResourceToPlayer(currentPlayer);
				goalManager.addRandomGoalToPlayer(currentPlayer);
				
				//Change the stations that have the speed boost every 5 turns			 
				 if ((getPlayerManager().getTurnNumber() % 5) == 0){
					 ChangeSpecialStations();
				 }
			}
		});
	}

	public static Game getInstance() {
		if (instance == null) {
			instance = new Game();
			// initialisePlayers gives them a goal, and the GoalManager requires an instance of game to exist so this
			// method can't be called in the constructor
			instance.initialisePlayers();
			
		}

		return instance;
	}

	// Only the first player should be given goals and resources during init
	// The second player gets them when turn changes!
	private void initialisePlayers() {
		Player player = playerManager.getAllPlayers().get(0);
		resourceManager.addRandomResourceToPlayer(player);
		resourceManager.addRandomResourceToPlayer(player);
		goalManager.addRandomGoalToPlayer(player);
	}
	

	public PlayerManager getPlayerManager() {
		return playerManager;
	}

	public GoalManager getGoalManager() {
		return goalManager;
	}

	public ResourceManager getResourceManager() {
		return resourceManager;
	}

	public Map getMap() {
		return map;
	}

	public GameState getState() {
		return state;
	}

	public void setState(GameState state) {
		this.state = state;
		stateChanged();
	}

	public void subscribeStateChanged(GameStateListener listener) {
		gameStateListeners.add(listener);
	}

	private void stateChanged() {
		for(GameStateListener listener : gameStateListeners) {
			listener.changed(state);
		}
	}
	
	/**
	  * Changes which stations have the speed alterations. Called every 5 turns and changes 3 stations.
	  */
	 private void ChangeSpecialStations() {
			// TODO Auto-generated method stub
		 List<Station> stations = getMap().getStations();
		 Random r = new Random();
		 int station1 = r.nextInt(stations.size());
		 int station2 = r.nextInt(stations.size());;
		 int station3 = r.nextInt(stations.size());;
		 while (station2 == station1){
			 station2 = r.nextInt(stations.size());
		 }
		 while (station3 == station1 || station3 == station2){
			 station3 = r.nextInt(stations.size());
		 }
		 
		 //Reset Speed modifier for every station
		 for (Station s : stations){
			 s.setSpeedModifier(0);
		 }
		 
		 //Set the new speed modifiers
		 stations.get(station1).setSpeedModifier(generateRandomSpeedModifier());
		 stations.get(station2).setSpeedModifier(generateRandomSpeedModifier());
		 stations.get(station3).setSpeedModifier(generateRandomSpeedModifier());
		 
		 System.out.println("New special stations are: " + stations.get(station1) + " " + stations.get(station2) + " " + stations.get(station3));
		}


	private float generateRandomSpeedModifier() {
		//Create a random speed modifier between -50% and 50% that only includes the 10s. i.e. -50, -40, -30 etc. Ensure that it is not 0. 75% chance it is positive.
		 Random r2 = new Random();
		 float speedModifier = r2.nextInt(5);
		 while (speedModifier == 0){
			 speedModifier = r2.nextInt(5);
		 }
		 speedModifier *= 10;				 
		 if ((r2.nextInt(4)) == 0){
			 speedModifier *= -1;
		 }
		 speedModifier = speedModifier / 100;
		 speedModifier++;
		return speedModifier;
	}
}
