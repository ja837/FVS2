package fvs.taxe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

import fvs.taxe.controller.*;
import fvs.taxe.dialog.DialogEndGame;
import gameLogic.Game;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.TurnListener;
import gameLogic.map.Map;
import gameLogic.map.Station;


public class GameScreen extends ScreenAdapter {
	final private TaxeGame game;
	private Stage stage;
	private Texture mapTexture;
	private Game gameLogic;
	private Skin skin;
	private Map map;
	private float timeAnimated = 0;
	public static final int ANIMATION_TIME = 2;
	private Tooltip tooltip;
	private Context context;

	private StationController stationController;
	private TopBarController topBarController;
	private ResourceController resourceController;
	private GoalController goalController;
	private RouteController routeController;


	/**
	 * 
	 * @param game
	 */
	 public GameScreen(TaxeGame game) {
		 this.game = game;
		 stage = new Stage();
		 skin = new Skin(Gdx.files.internal("data/uiskin.json"));

		 gameLogic = Game.getInstance();
		 context = new Context(stage, skin, game, gameLogic);
		 Gdx.input.setInputProcessor(stage);

		 mapTexture = new Texture(Gdx.files.internal("gamemap5.png"));
		 map = gameLogic.getMap();

		 tooltip = new Tooltip(skin);
		 stage.addActor(tooltip);

		 stationController = new StationController(context, tooltip);
		 topBarController = new TopBarController(context);
		 resourceController = new ResourceController(context);
		 goalController = new GoalController(context);
		 routeController = new RouteController(context);

		 context.setRouteController(routeController);
		 context.setTopBarController(topBarController);

		 gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
			 @Override
			 public void changed() {

				 gameLogic.setState(GameState.ANIMATING);
				 topBarController.displayFlashMessage("Time is passing...", Color.BLACK);

				 //Change the stations that have the speed boost every 5 turns			 
				 if ((gameLogic.getPlayerManager().getTurnNumber() % 5) == 0){
					 ChangeSpecialStations();
				 }
				 //Have a chance to fix broken stations
				 Random rand = new Random();
				 for (Station station : map.getStations()){
					 if (station.isPassable() == false){
						 if(rand.nextInt(10)<3){
							 station.setPassable(true);
							 topBarController.displayFlashMessage("The station at "+station.toString()+" was fixed!", Color.GREEN);
							 Array<Actor> stageActors = stage.getActors();
							 for (Actor a: stageActors){
								 if (a.getName() != null){									 
									 a.remove();									 
								 }
							 }							
						 }
					 }
				 }
				 
			 }

			
		 });
		 gameLogic.subscribeStateChanged(new GameStateListener() {
			 @Override
			 public void changed(GameState state){
				 if(gameLogic.getPlayerManager().getTurnNumber() == gameLogic.TOTAL_TURNS && state == GameState.NORMAL) {
					 //
					 DialogEndGame dia = new DialogEndGame(GameScreen.this.game, gameLogic.getPlayerManager(), skin);
					 dia.show(stage);
				 }
			 }
		 });
	 }


	 // called every frame
	 @Override
	 public void render(float delta) {
		 Gdx.gl.glClearColor(0, 0, 0, 1);
		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		 game.batch.begin();
		 game.batch.draw(mapTexture, 0, 0);
		 game.batch.end();

		 goalController.drawBackground();
		 //topBarController.drawBackground();

		 stationController.renderConnections(map.getConnections(), Color.GRAY);
		 stationController.renderStationLbls();
		 stationController.renderStationSpeedModifierLbls();
		 stationController.renderSigns();
		 
		 if(gameLogic.getState() == GameState.ROUTING) {
			 routeController.drawRoute(Color.BLACK);
		 }

		 if(gameLogic.getState() == GameState.ANIMATING) {

			 //Fix for player 1 getting an extra go at the start of the game
			 int adjustedAnimationTime = ANIMATION_TIME;
			 if (gameLogic.getPlayerManager().getTurnNumber() == 1){
				 adjustedAnimationTime = 0;
			 }

			 timeAnimated += delta;
			 if (timeAnimated >= adjustedAnimationTime) {
				 gameLogic.setState(GameState.NORMAL);
				 timeAnimated = 0;
			 }

		 }

		 if(gameLogic.getState() == GameState.NORMAL || gameLogic.getState() == GameState.PLACING){
			 stationController.renderRoutedConnections(map.getConnections(), Color.RED);
			 stationController.displayNumberOfTrainsAtStations();
		 }

		 stage.act(Gdx.graphics.getDeltaTime());
		 stage.draw();

		 if (gameLogic.getPlayerManager().getTurnNumber() < gameLogic.TOTAL_TURNS) {
			 game.batch.begin();
			 game.fontSmall.setColor(Color.NAVY);
			 game.fontSmall.draw(game.batch, "Turn " + (gameLogic.getPlayerManager().getTurnNumber()+1) + "/" + gameLogic.TOTAL_TURNS, 20, TaxeGame.HEIGHT - 100.0f);
			 game.batch.end();
		 }

		 resourceController.drawHeaderText();
		 goalController.showCurrentPlayerGoals();
		 topBarController.updateScores(gameLogic.getPlayerManager().getAllPlayers());
	 }

	 @Override
	 // Called when GameScreen becomes current screen of the game
	 public void show() {
		 stationController.renderStations();
		 goalController.addEndTurnButton();
		 resourceController.drawPlayerResources(gameLogic.getPlayerManager().getCurrentPlayer());
	 }


	 @Override
	 public void dispose() {
		 mapTexture.dispose();
		 stage.dispose();
	 }
	 
	 /**
	  * Changes which stations have the speed alterations. Called every 5 turns and changes 3 stations.
	  */
	 private void ChangeSpecialStations() {
			// TODO Auto-generated method stub
		 List<Station> stations = gameLogic.getMap().getStations();
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
