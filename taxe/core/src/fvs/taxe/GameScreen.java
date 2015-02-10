package fvs.taxe;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	private InfoController infoController;
	private ResourceController resourceController;
	private GoalController goalController;
	private RouteController routeController;
	private StationWorksController stationWorksController;


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
		 infoController = new InfoController(context);
		 resourceController = new ResourceController(context);
		 goalController = new GoalController(context);
		 routeController = new RouteController(context);
		 stationWorksController = new StationWorksController(context);

		 context.setRouteController(routeController);
		 context.setInfoController(infoController);

		 gameLogic.getPlayerManager().subscribeTurnChanged(new TurnListener() {
			 @Override
			 public void changed() {

				 gameLogic.setState(GameState.ANIMATING);
				 //infoController.displayFlashMessage("Time is passing...", Color.BLACK);

				 
				 //Have a chance to fix broken stations
				 Random rand = new Random();
				 for (Station station : map.getStations()){
					 if (station.isPassable() == false){
						 if(rand.nextInt(10)<3){
							 station.setPassable(true);
							// infoController.displayFlashMessage("The station at "+station.toString()+" was fixed!", Color.GREEN);
							 Dialog dia = new Dialog("Junction Failure", context.getSkin());
				            dia.show(context.getStage());
				            TextButton button = new TextButton("Ok", context.getSkin());
				            dia.text("The junction at "+station.toString()+"\nwas fixed!");
				            dia.setHeight(125);
				            dia.setWidth(250);
				            dia.setPosition(400, 500);
				            dia.button(button);
				            station.setPassable(false);
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

		 stationController.renderConnections(map.getConnections(), Color.GRAY);
		 stationController.renderStationLbls();
		 stationController.renderStationSpeedModifierLbls();
		 //stationController.renderSigns();
		 
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
		 resourceController.drawInfoText();
		 goalController.showCurrentPlayerGoals();
		 //stationWorksController.showSpeedBoostStations();
		 infoController.updateScores(gameLogic.getPlayerManager().getAllPlayers());
	 }

	 @Override
	 // Called when GameScreen becomes current screen of the game
	 public void show() {
		 stationController.renderStations();
		 goalController.addEndTurnButton();
		 resourceController.drawPlayerResources(gameLogic.getPlayerManager().getCurrentPlayer());
		 gameLogic.getSoundManager().playBGMusic();
	 }

	 @Override
	 public void dispose() {
		 mapTexture.dispose();
		 stage.dispose();
	 }
	 
	 

}
