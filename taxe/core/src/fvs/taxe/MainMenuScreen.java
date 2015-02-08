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
    Rectangle exitBounds;
    Rectangle turnBounds10;
    Rectangle turnBounds20;
    Rectangle turnBounds30;
    Vector3 touchPoint;
    Texture mapTexture;
    Image mapImage;
    

    public MainMenuScreen(TaxeGame game) {
        this.game = game;
        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false);

        playBounds = new Rectangle(TaxeGame.WIDTH/2 - -20, 200, 150, 50);
        exitBounds = new Rectangle(TaxeGame.WIDTH/2 - 180, 200, 150, 50);
        turnBounds10 = new Rectangle(TaxeGame.WIDTH/2 - 60, 310, 26, 25);
        turnBounds20 = new Rectangle(TaxeGame.WIDTH/2 - 20, 310, 26, 25);
        turnBounds30 = new Rectangle(TaxeGame.WIDTH/2 - -20, 310, 26, 25);
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
            if (exitBounds.contains(touchPoint.x, touchPoint.y)) {
            	Gdx.app.exit();
            }
            //turn selection
            if (turnBounds10.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(10);
            	game.shapeRenderer.setProjectionMatrix(camera.combined);
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(Color.WHITE);
                game.shapeRenderer.rect(turnBounds10.getX(), turnBounds10.getY(), turnBounds10.getWidth(), turnBounds10.getHeight());
                game.shapeRenderer.end();
            	System.out.println(gameLogic.Game.TOTAL_TURNS);
            }
            if (turnBounds20.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(20);
            	game.shapeRenderer.setProjectionMatrix(camera.combined);
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(Color.WHITE);
                game.shapeRenderer.rect(turnBounds20.getX(), turnBounds20.getY(), turnBounds20.getWidth(), turnBounds20.getHeight());
                game.shapeRenderer.end();
            	System.out.println(gameLogic.Game.TOTAL_TURNS);
            }
            if (turnBounds30.contains(touchPoint.x, touchPoint.y)) {
            	gameLogic.Game.changeTurns(30);
            	game.shapeRenderer.setProjectionMatrix(camera.combined);
                game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                game.shapeRenderer.setColor(Color.WHITE);
                game.shapeRenderer.rect(turnBounds30.getX(), turnBounds30.getY(), turnBounds30.getWidth(), turnBounds30.getHeight());
                game.shapeRenderer.end();
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
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.rect(playBounds.getX(), playBounds.getY(), playBounds.getWidth(), playBounds.getHeight());
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.rect(turnBounds10.getX(), turnBounds10.getY(), turnBounds10.getWidth(), turnBounds10.getHeight());
        game.shapeRenderer.rect(turnBounds20.getX(), turnBounds20.getY(), turnBounds20.getWidth(), turnBounds20.getHeight());
        game.shapeRenderer.rect(turnBounds30.getX(), turnBounds30.getY(), turnBounds30.getWidth(), turnBounds30.getHeight());
        game.shapeRenderer.setColor(Color.GRAY);
        game.shapeRenderer.rect(exitBounds.getX(), exitBounds.getY(), exitBounds.getWidth(), exitBounds.getHeight());
        game.shapeRenderer.end();

        //Draw text into rectangles
        game.batch.begin();
        String startGameString = "Start Game";
        game.fontMed.draw(game.batch, startGameString, playBounds.getX() + playBounds.getWidth()/2 - game.fontMed.getBounds(startGameString).width/2,
                playBounds.getY() + playBounds.getHeight()/2 + game.fontMed.getBounds(startGameString).height/2); // centre the text
        String exitGameString = "How To Play";
        game.fontMed.draw(game.batch, exitGameString, exitBounds.getX() + exitBounds.getWidth()/2 - game.fontMed.getBounds(exitGameString).width/2,
                exitBounds.getY() + exitBounds.getHeight()/2 + game.fontMed.getBounds(exitGameString).height/2); // centre the text
        String turn10String = "10";
        game.fontSmall.draw(game.batch, turn10String, turnBounds10.getX() + turnBounds10.getWidth()/2 - game.fontSmall.getBounds(turn10String).width/2,
        		turnBounds10.getY() + turnBounds10.getHeight()/2 + game.fontSmall.getBounds(turn10String).height/2); // centre the text
        String turn20String = "20";
        game.fontSmall.draw(game.batch, turn20String, turnBounds20.getX() + turnBounds20.getWidth()/2 - game.fontSmall.getBounds(turn20String).width/2,
        		turnBounds20.getY() + turnBounds20.getHeight()/2 + game.fontSmall.getBounds(turn20String).height/2); // centre the text
        String turn30String = "30";
        game.fontSmall.draw(game.batch, turn30String, turnBounds30.getX() + turnBounds30.getWidth()/2 - game.fontSmall.getBounds(turn30String).width/2,
        		turnBounds30.getY() + turnBounds30.getHeight()/2 + game.fontSmall.getBounds(turn30String).height/2); // centre the text
        game.batch.end();
    }

    @Override
    public void render(float delta) {
    	draw();
    	update();
    }
    
}