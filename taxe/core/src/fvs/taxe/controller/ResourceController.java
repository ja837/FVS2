package fvs.taxe.controller;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import fvs.taxe.TaxeGame;
import fvs.taxe.dialog.TrainClicked;
import gameLogic.Player;
import gameLogic.PlayerChangedListener;
import gameLogic.resource.Resource;
import gameLogic.resource.Train;

public class ResourceController {
    private Context context;
    private Group resourceButtons = new Group();
	private String infoText ;

    public ResourceController(final Context context) {
        this.context = context;
        this.infoText = "";

        context.getGameLogic().getPlayerManager().subscribePlayerChanged(new PlayerChangedListener() {
            @Override
            public void changed() {
                drawPlayerResources(context.getGameLogic().getPlayerManager().getCurrentPlayer());
            }
        });
    }

    public void drawHeaderText() {
        TaxeGame game = context.getTaxeGame();

        game.batch.begin();
        game.fontSmall.setColor(Color.NAVY);
        game.fontSmall.draw(game.batch, "Unplaced Resources:", TaxeGame.WIDTH - 225.0f, (float) TaxeGame.HEIGHT - 250.0f);
        game.batch.end();
    }
    
    public void setInfoText(String text) {
        infoText = text;
    }
    
    public void drawInfoText() {
        TaxeGame game = context.getTaxeGame();

        game.batch.begin();
        game.fontTiny.setColor(Color.NAVY);
        game.fontTiny.draw(game.batch, infoText, TaxeGame.WIDTH - 225.0f, (float) TaxeGame.HEIGHT - 550.0f);
        game.batch.end();
    }

    public void drawPlayerResources(Player player) {

        float top = (float) TaxeGame.HEIGHT;
        float x = TaxeGame.WIDTH - 225.0f;
        float y = top - 250.0f;
        y -= 50;

        resourceButtons.remove();
        resourceButtons.clear();
        
        for (final Resource resource : player.getResources()) {
            if (resource instanceof Train) {
                Train train = (Train) resource;

                // don't show a button for trains that have been placed
                if (train.getPosition() != null) {
                    continue;
                }

                TrainClicked listener = new TrainClicked(context, train);

                TextButton button = new TextButton(resource.toString(), context.getSkin());
                button.setPosition(x, y);
                button.setWidth(125);
                button.addListener(listener);
                
                //set button colour depending on train speed
                //red = fast, green = slow
                switch (((Train) resource).getSpeed()){
                	case 10: //steam
                		button.setColor(Color.valueOf("0DD131"));;
                		break;
                	case 20: //Green
                		button.setColor(Color.valueOf("39FF03"));;
                		break;
                	case 30: //Diesel
                		button.setColor(Color.valueOf("C8FF03"));;
                		break;
                	case 40: //Electric
                		button.setColor(Color.valueOf("FFFF03"));;
                		break;
                	case 50: //Bullet
                		button.setColor(Color.valueOf("FFEE03"));;
                		break;
                	case 60: //Petrol
                		button.setColor(Color.valueOf("FFBB00"));;
                		break;
                	case 70: //Solar
                		button.setColor(Color.valueOf("FF9100"));;
                		break;
                	case 80: //MagLev
                		button.setColor(Color.valueOf("F26907"));;
                		break;
                	case 90: //Hydrogen
                		button.setColor(Color.valueOf("F23A07"));;
                		break;
                	case 100: //Nuclear
                		button.setColor(Color.valueOf("B31807"));;
                		break;
                }

                resourceButtons.addActor(button);

                y -= 30;
            }
        }

        context.getStage().addActor(resourceButtons);
    }
}
