package fvs.taxe.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.map.Station;

public class StationWorksController {
	public final static int CONTROLS_WIDTH = 175;
	private Context context;
    private Color controlsColor = Color.LIGHT_GRAY;

    public StationWorksController(Context context) {
        this.context = context;
       
    };
    
	
	public void showSpeedBoostStations() {
        TaxeGame game = context.getTaxeGame();
        
        float top = (float) TaxeGame.HEIGHT;
        float x = CONTROLS_WIDTH - 170;
        float y = top - 220;

        game.batch.begin();
        game.fontTiny.setColor(Color.MAROON);
        game.fontTiny.draw(game.batch, "Acceleration Stations", x, y);
        game.batch.end();
        
        y -= 20;
        game.fontTiny.setColor(Color.NAVY);
        for (Station s : context.getGameLogic().getMap().getStations()) {
        	// display station speed modifier
        	
        	if (s.getSpeedModifier() != 0){
        		String acronym = s.getName();
        		game.batch.begin();
        		if(acronym == ""){
        			acronym = "junction";
        		}
                game.fontTiny.draw(game.batch, acronym + "(" + s.getSpeedModifier() + ")", x, y);
                game.batch.end();
                y -=20;
        	}
        }
        
        game.batch.begin();
        game.fontTiny.setColor(Color.MAROON);
        game.fontTiny.draw(game.batch, "Border Controlled Stations", x, y);
        game.batch.end();
        y-=20;
        game.fontTiny.setColor(Color.NAVY);
        for (Station s : context.getGameLogic().getMap().getStations()) {
        	if(s.isControlled() == true){
        		String acronym = s.getName();
        		game.batch.begin();
        		if(acronym == ""){
        			acronym = "junction";
        		}
                game.fontTiny.draw(game.batch, acronym, x, y);
                game.batch.end();
                y -=20;
        	}
        }
	}
}

