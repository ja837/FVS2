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


/**The instructions screen
 * 
 * @author Chris
 *
 */
public class InstructionsScreen extends ScreenAdapter {
    TaxeGame game;
    OrthographicCamera camera;
    Rectangle backBounds;
    Vector3 touchPoint;
    Texture mapTexture;
    Image mapImage;
    
    /**The constructor
     * 
     * @param game The current Taxe Game
     */
    public InstructionsScreen(TaxeGame game) {
        this.game = game;
        camera = new OrthographicCamera(TaxeGame.WIDTH, TaxeGame.HEIGHT);
        camera.setToOrtho(false);
        
        /**
         * button position
         */
        backBounds = new Rectangle(TaxeGame.WIDTH/2 - 570, 550, 150, 50);
        
        touchPoint = new Vector3();
        mapTexture = new Texture(Gdx.files.internal("instructions_screen.png"));
        mapImage = new Image(mapTexture);
        
    }

	public void update() {
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            /**
             * go back to main menu screen
             */
            if (backBounds.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new MainMenuScreen(game));
                return;
            }
        }
    }

	/**
	 * Drawing the actual screen
	 */
    public void draw() {
        GL20 gl = Gdx.gl;
        gl.glClearColor(1, 1, 1, 1);
        gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /**
         * draw image in background
         */
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        Color c = game.batch.getColor();
        game.batch.setColor(c.r, c.g, c.b, (float) 1.0);
        game.batch.draw(mapTexture, -1, 0);
        game.batch.setColor(c);
        game.batch.end();
    
    
        /**
         * draw rectangles
         */
        game.shapeRenderer.setProjectionMatrix(camera.combined);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.setColor(Color.MAROON);
        game.shapeRenderer.rect(backBounds.getX(), backBounds.getY(), backBounds.getWidth(), backBounds.getHeight());
        game.shapeRenderer.end();

        /**
         * draw text into rectangles
         */
        game.batch.begin();
        String backString = "Back";
        game.fontMed.draw(game.batch, backString, backBounds.getX() + backBounds.getWidth()/2 - game.fontMed.getBounds(backString).width/2,
        		backBounds.getY() + backBounds.getHeight()/2 + game.fontMed.getBounds(backString).height/2); // centre the text
        game.batch.end();
        
    }

    
	@Override
    public void render(float delta) {
    	draw();
    	update();
    }
    
}