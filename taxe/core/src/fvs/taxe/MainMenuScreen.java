package fvs.taxe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import gameLogic.Game;

public class MainMenuScreen extends ScreenAdapter {
    TaxeGame game;
    OrthographicCamera camera;
    Rectangle playBounds;
    Rectangle instructionsBounds;
    Rectangle turnBounds50;
    Rectangle turnBounds40;
    Rectangle turnBounds30;
    Vector3 touchPoint;
    Texture mapTexture;
    Image mapImage;
    

    public MainMenuScreen(TaxeGame game) {
        this.game = game;
        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false);

        playBounds = new Rectangle(TaxeGame.WIDTH/2 - -20, 200, 150, 50);
        instructionsBounds = new Rectangle(TaxeGame.WIDTH/2 - 180, 200, 150, 50);
        turnBounds30 = new Rectangle(TaxeGame.WIDTH/2 - 60, 310, 26, 25);
        turnBounds40 = new Rectangle(TaxeGame.WIDTH/2 - 20, 310, 26, 25);
        turnBounds50 = new Rectangle(TaxeGame.WIDTH/2 - -20, 310, 26, 25);
        touchPoint = new Vector3();
        mapTexture = new Texture(Gdx.files.internal("splash_screen.png"));
        mapImage = new Image(mapTexture);
        
    }

	public void update() {
    	
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            //start the game
            if (playBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new GameScreen(game));
                return;
            }
            //close the game
            if (instructionsBounds.contains(touchPoint.x, touchPoint.y)) {
            	game.setScreen(new InstructionsScreen(game));
            }
            //turn selection
            if (turnBounds30.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(30);
            	System.out.println(gameLogic.Game.TOTAL_TURNS);
            }
            if (turnBounds40.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(40);
            	System.out.println(gameLogic.Game.TOTAL_TURNS);
            }
            if (turnBounds50.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(50);            	
            	System.out.println(gameLogic.Game.TOTAL_TURNS);
            }
        }
    }

    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw map in the background
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        Color c = game.batch.getColor();
        game.batch.setColor(c.r, c.g, c.b, (float) 1.0);
        game.batch.draw(mapTexture, -1, 0);
        game.batch.setColor(c);
        game.batch.end();
    
    
        //Draw rectangles, did not use TextButtons because it was easier not to
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.MAROON);
        game.shapeRenderer.rect(playBounds.getX(), playBounds.getY(), playBounds.getWidth(), playBounds.getHeight());
        game.shapeRenderer.rect(turnBounds30.getX(), turnBounds30.getY(), turnBounds30.getWidth(), turnBounds30.getHeight());
        game.shapeRenderer.rect(turnBounds40.getX(), turnBounds40.getY(), turnBounds40.getWidth(), turnBounds40.getHeight());
        game.shapeRenderer.rect(turnBounds50.getX(), turnBounds50.getY(), turnBounds50.getWidth(), turnBounds50.getHeight());
        game.shapeRenderer.setColor(Color.MAROON);
        game.shapeRenderer.rect(instructionsBounds.getX(), instructionsBounds.getY(), instructionsBounds.getWidth(), instructionsBounds.getHeight());
        game.shapeRenderer.end();

        //Draw text into rectangles
        game.batch.begin();
        game.fontSmall.setColor(Color.WHITE);
        String startGameString = "Start Game";
        game.fontMed.draw(game.batch, startGameString, playBounds.getX() + playBounds.getWidth()/2 - game.fontMed.getBounds(startGameString).width/2,
                playBounds.getY() + playBounds.getHeight()/2 + game.fontMed.getBounds(startGameString).height/2); // centre the text
        String instructionsString = "How To Play";
        game.fontMed.draw(game.batch, instructionsString, instructionsBounds.getX() + instructionsBounds.getWidth()/2 - game.fontMed.getBounds(instructionsString).width/2,
        		instructionsBounds.getY() + instructionsBounds.getHeight()/2 + game.fontMed.getBounds(instructionsString).height/2); // centre the text
        String turn30String = "30";
        game.fontSmall.draw(game.batch, turn30String, turnBounds30.getX() + turnBounds30.getWidth()/2 - game.fontSmall.getBounds(turn30String).width/2,
        		turnBounds30.getY() + turnBounds30.getHeight()/2 + game.fontSmall.getBounds(turn30String).height/2); // centre the text
        String turn40String = "40";
        game.fontSmall.draw(game.batch, turn40String, turnBounds40.getX() + turnBounds40.getWidth()/2 - game.fontSmall.getBounds(turn40String).width/2,
        		turnBounds40.getY() + turnBounds40.getHeight()/2 + game.fontSmall.getBounds(turn40String).height/2); // centre the text
        String turn50String = "50";
        game.fontSmall.draw(game.batch, turn50String, turnBounds50.getX() + turnBounds50.getWidth()/2 - game.fontSmall.getBounds(turn50String).width/2,
        		turnBounds50.getY() + turnBounds50.getHeight()/2 + game.fontSmall.getBounds(turn50String).height/2); // centre the text
        game.batch.end();
        
        game.batch.begin();
    	game.fontSmall.setColor(Color.MAROON);
    	String turnsString = gameLogic.Game.TOTAL_TURNS + " turns selected";
    	game.fontSmall.draw(game.batch, turnsString, 520, 300);
    	game.batch.end();
        
    }

    
	@Override
    public void render(float delta) {
    	draw();
    	update();
    	
    	
    }
    
}