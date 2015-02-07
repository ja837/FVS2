package fvs.taxe.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.goal.Goal;
import gameLogic.resource.Cargo;

import java.util.ArrayList;
import java.util.List;

public class GoalController {
	public final static int CONTROLS_WIDTH = 250;
    private Context context;
    private TextButton endTurnButton;
    private Color controlsColor = Color.LIGHT_GRAY;
    private Group goalButtons = new Group();

    public GoalController(Context context) {
        this.context = context;
    }

    private List<String> playerGoalStrings() {
        ArrayList<String> strings = new ArrayList<String>();
        PlayerManager pm = context.getGameLogic().getPlayerManager();
        Player currentPlayer = pm.getCurrentPlayer();

        for (Goal goal : currentPlayer.getGoals()) {
            if(goal.getComplete()) {
                continue;
            }

            strings.add(goal.toString());
        }

        return strings;
    }
    
    private List<String> playerGoalAnimals() {
        ArrayList<String> animals = new ArrayList<String>();
        PlayerManager pm = context.getGameLogic().getPlayerManager();
        Player currentPlayer = pm.getCurrentPlayer();

        for (Goal goal : currentPlayer.getGoals()) {
            if(goal.getComplete()) {
                continue;
            }

            animals.add(goal.getImgFile());
        }

        return animals;
    }
    
    public void addEndTurnButton() {
        endTurnButton = new TextButton("End Turn", context.getSkin());
        endTurnButton.setPosition(TaxeGame.WIDTH - CONTROLS_WIDTH + 25, TaxeGame.HEIGHT - 623.0f);
        endTurnButton.setHeight(50);
        endTurnButton.setWidth(100);
        endTurnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                context.getGameLogic().getPlayerManager().turnOver();
            }
        });

        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                if(state == GameState.NORMAL) {
                    endTurnButton.setVisible(true);
                } else {
                    endTurnButton.setVisible(false);
                }
            }
        });

        context.getStage().addActor(endTurnButton);
    }
    
    public void drawBackground() {
        TaxeGame game = context.getTaxeGame();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(controlsColor);
        game.shapeRenderer.rect(TaxeGame.WIDTH - CONTROLS_WIDTH, 0, CONTROLS_WIDTH, TaxeGame.HEIGHT);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(TaxeGame.WIDTH - CONTROLS_WIDTH, 0, 1, TaxeGame.HEIGHT );
        game.shapeRenderer.end();
    }

    public void showCurrentPlayerGoals() {
        TaxeGame game = context.getTaxeGame();
        
        goalButtons.remove();
        goalButtons.clear();

        float top = (float) TaxeGame.HEIGHT;
        float x = TaxeGame.WIDTH - CONTROLS_WIDTH + 10;
        float y = top - 10.0f;

        game.batch.begin();
        game.fontSmall.setColor(Color.NAVY);
        game.fontSmall.draw(game.batch, playerGoalHeader(), x, y);
        game.batch.end();
        
        y -= 90;

        for (int i = 0; i < playerGoalAnimals().size(); i++) {
        	
        	String animal = playerGoalAnimals().get(i);
        	String goalString = playerGoalStrings().get(i);
        	Texture animalTexture = new Texture(Gdx.files.internal(animal));
        	Color c = game.batch.getColor();
            game.batch.begin();
            //draw goal animal
            game.batch.setColor(c.r, c.g, c.b, (float) 1.0);
            game.batch.draw(animalTexture, x, y);
            game.batch.setColor(c);
            //draw goal text
            game.fontTiny.setColor(Color.BLACK);
            game.fontTiny.drawWrapped(game.batch, goalString, x+60, y+50, CONTROLS_WIDTH - 80);
            game.batch.end();

            y -= 70;
        }
        
        
        
        context.getStage().addActor(goalButtons);
    }

    private String playerGoalHeader() {
        return "Player " + context.getGameLogic().getPlayerManager().getCurrentPlayer().getPlayerNumber() + " Goals:";
    }
}
