package fvs.taxe.dialog;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import fvs.taxe.MainMenuScreen;
import fvs.taxe.TaxeGame;
import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.goal.Goal;

public class DialogEndGame extends Dialog{
	private TaxeGame game;
	
	public DialogEndGame(TaxeGame game, PlayerManager pm, Skin skin) {
		super("GAME OVER", skin);
		this.game = game;
		
		int highscore = 0;
		ArrayList<Integer> winners = new ArrayList<Integer>();
		for(Player player : pm.getAllPlayers()) {
			int goalsComplete = 0;
			for(Goal goal : player.getGoals()) {
				if(goal.getComplete()){
					goalsComplete++;
				}
			}
			
			text("Player " + player.getPlayerNumber() + " completed " + goalsComplete + " goals, worth " + player.getPlayerScore() + " points");
			getContentTable().row();
			
			if(player.getPlayerScore() > highscore) {
				highscore = player.getPlayerScore();
				winners = new ArrayList<Integer>();
				winners.add(player.getPlayerNumber());
			} else if(player.getPlayerScore() == highscore) {
				winners.add(player.getPlayerNumber());
			}
		}
		
		if(winners.isEmpty()) {
			text("NO WINNER");
		} else if (winners.size() == 1){
			text("PLAYER " + winners.get(0) + " WINS!");
		} else {
			String draw;
			draw = "DRAW BETWEEN";
			for(int winner: winners){
				draw += " PLAYER " + winner;
			}
			text(draw);
		}
		
		//button("Main Menu","MENU");
		button("Exit","EXIT");
	}
	
	@Override
	public Dialog show(Stage stage) {
		show(stage, null);
		setPosition(Math.round((stage.getWidth() - getWidth()) / 2), Math.round((stage.getHeight() - getHeight()) / 2));
		return this;
	}
	
	@Override
	public void hide() {
		hide(null);
	}
	
	@Override
	protected void result(Object obj) {
		if(obj == "EXIT"){
			Gdx.app.exit();
		} else {
			game.setScreen(new MainMenuScreen(game));
		}
	}
}
