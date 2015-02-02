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

public class MainMenuScreen extends ScreenAdapter {
    TaxeGame game;
    OrthographicCamera camera;
    Rectangle playBounds;
    Rectangle exitBounds;
    Vector3 touchPoint;
    Texture mapTexture;
    Image mapImage;
    

    public MainMenuScreen(TaxeGame game) {
        this.game = game;
        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false);

        playBounds = new Rectangle(TaxeGame.WIDTH/2 - 0, 250, 150, 50);
        exitBounds = new Rectangle(TaxeGame.WIDTH/2 - 200, 250, 150, 50);
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
        game.shapeRenderer.rect(exitBounds.getX(), exitBounds.getY(), exitBounds.getWidth(), exitBounds.getHeight());
        game.shapeRenderer.end();

        //Draw text into rectangles
        game.batch.begin();
        String startGameString = "Start game";
        game.font.draw(game.batch, startGameString, playBounds.getX() + playBounds.getWidth()/2 - game.font.getBounds(startGameString).width/2,
                playBounds.getY() + playBounds.getHeight()/2 + game.font.getBounds(startGameString).height/2); // centre the text
        String exitGameString = "Exit";
        game.font.draw(game.batch, exitGameString, exitBounds.getX() + exitBounds.getWidth()/2 - game.font.getBounds(exitGameString).width/2,
                exitBounds.getY() + exitBounds.getHeight()/2 + game.font.getBounds(exitGameString).height/2); // centre the text
        game.fontSmall.draw(game.batch, startGameString, playBounds.getX() + playBounds.getWidth()/2 - game.fontSmall.getBounds(startGameString).width/2,
                playBounds.getY() + playBounds.getHeight()/2 + game.fontSmall.getBounds(startGameString).height/2); // center the text
    }

    @Override
    public void render(float delta) {
    	update();
        draw();
    }
    
}