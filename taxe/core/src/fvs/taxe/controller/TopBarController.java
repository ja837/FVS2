package fvs.taxe.controller;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import fvs.taxe.TaxeGame;
import gameLogic.GameState;
import gameLogic.GameStateListener;
import gameLogic.Player;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TopBarController {
    public final static int CONTROLS_HEIGHT = 40;

    private Context context;
    private Color controlsColor = Color.LIGHT_GRAY;
    private TextButton endTurnButton;
    private Group playerScores = new Group();
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
                        controlsColor = Color.CLEAR;
                        break;
                }
            }
        });

        createFlashActor();
    }

    private void createFlashActor() {
        flashMessage = new Label("", context.getSkin());
        flashMessage.setPosition(400, TaxeGame.HEIGHT - 35);
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
    
    public void updateScores(List<Player> playerList){
    	TaxeGame game = context.getTaxeGame();
    	playerScores.remove();
        playerScores.clear();
        game.batch.begin();
        game.fontSmall.setColor(Color.NAVY);
        game.font.setColor(Color.NAVY);
        game.fontSmall.draw(game.batch, "Player 1", 20.0f, TaxeGame.HEIGHT - 25.0f);
        game.font.draw(game.batch, "" + playerList.get(0).getPlayerScore(), 20.0f, TaxeGame.HEIGHT - 50.0f);
        game.fontSmall.draw(game.batch, "Player 2", 120.0f, TaxeGame.HEIGHT - 25.0f);
        game.font.draw(game.batch, "" + playerList.get(1).getPlayerScore(), 120.0f, TaxeGame.HEIGHT - 50.0f);
        game.batch.end();
        context.getStage().addActor(playerScores);
    }
}
