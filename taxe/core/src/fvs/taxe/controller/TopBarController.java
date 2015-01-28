package fvs.taxe.controller;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.Player;
import gameLogic.TurnListener;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TopBarController {
    public final static int CONTROLS_HEIGHT = 40;

    private Context context;
    private Color controlsColor = Color.LIGHT_GRAY;
    private TextButton endTurnButton;
    private Label LblP1Score;
    private Label LblP2Score;
    private Label flashMessage;

    public TopBarController(Context context) {
        this.context = context;

        context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                switch (state) {
                    case ANIMATING:
                        controlsColor = Color.GREEN;
                        break;
                    default:
                        controlsColor = Color.LIGHT_GRAY;
                        break;
                }
            }
        });

        createFlashActor();
    }

    private void createFlashActor() {
        flashMessage = new Label("", context.getSkin());
        flashMessage.setPosition(400, TaxeGame.HEIGHT - 24);
        context.getStage().addActor(flashMessage);
    }

    public void displayFlashMessage(String message, Color color) {
        displayFlashMessage(message, color, 1.75f);
    }

    public void displayFlashMessage(String message, Color color, float time) {
        flashMessage.setText(message);
        flashMessage.setColor(color);
        flashMessage.addAction(sequence(delay(time), fadeOut(0.25f)));

    }

    public void drawBackground() {
        TaxeGame game = context.getTaxeGame();

        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(controlsColor);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, CONTROLS_HEIGHT);
        game.shapeRenderer.setColor(Color.BLACK);
        game.shapeRenderer.rect(0, TaxeGame.HEIGHT - CONTROLS_HEIGHT, TaxeGame.WIDTH, 1);
        game.shapeRenderer.end();
    }

    public void addEndTurnButton() {
        endTurnButton = new TextButton("End Turn", context.getSkin());
        endTurnButton.setPosition(TaxeGame.WIDTH - 100.0f, TaxeGame.HEIGHT - 33.0f);
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
    
    public void refreshScores(List<Player> playerList) {
    	LblP1Score.setText("Player 1:   " + playerList.get(0).getPlayerScore());
    	LblP2Score.setText("Player 2:   " + playerList.get(1).getPlayerScore());
    }
    
    public void displayScores(final List<Player> playerList) {
    	LblP1Score = new Label("Player 1:   " + playerList.get(0).getPlayerScore(), context.getSkin());
    	LblP2Score = new Label("Player 2:   " + playerList.get(1).getPlayerScore(), context.getSkin());
    	LblP1Score.setPosition(TaxeGame.WIDTH - 800.0f, TaxeGame.HEIGHT - 33.0f);
    	LblP2Score.setPosition(TaxeGame.WIDTH - 600.0f, TaxeGame.HEIGHT - 33.0f);
    	
    	// whenever game state is changed to normal update score values
    	context.getGameLogic().subscribeStateChanged(new GameStateListener() {
            @Override
            public void changed(GameState state) {
                if(state == GameState.NORMAL) {
                	LblP1Score.setText("Player 1:   " + playerList.get(0).getPlayerScore());
                	LblP2Score.setText("Player 2:   " + playerList.get(1).getPlayerScore());
                    LblP1Score.setVisible(true);
                    LblP2Score.setVisible(true);
                } else {
                	LblP1Score.setVisible(false);
                	LblP2Score.setVisible(false);
                }
            }
        });

    	context.getStage().addActor(LblP1Score);
    	context.getStage().addActor(LblP2Score);
    	
    	System.out.println(LblP1Score.getText());
    	System.out.println(LblP2Score.getText());
    }
}
