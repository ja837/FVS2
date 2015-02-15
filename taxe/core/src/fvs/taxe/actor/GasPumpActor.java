package fvs.taxe.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import gameLogic.map.IPositionable;

public class GasPumpActor extends Image {
    private static int width = 20;
    private static int height = 20;
    
    /**
     * Graphic to show which stations have speed modifiers. Unused in this release.
     * @param location of station to place gas pump next to
     */
    public GasPumpActor(IPositionable location) {
        super(new Texture(Gdx.files.internal("pump.png")));

        setSize(width, height);
        setPosition((location.getX() - width / 2) - 10, (location.getY() - height / 2) - 10);
    }
}
