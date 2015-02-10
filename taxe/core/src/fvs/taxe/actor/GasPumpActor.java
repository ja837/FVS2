package fvs.taxe.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import gameLogic.map.IPositionable;

public class GasPumpActor extends Image {
    private static int width = 30;
    private static int height = 30;

    public GasPumpActor(IPositionable location) {
        super(new Texture(Gdx.files.internal("pump.png")));

        setSize(width, height);
        setPosition((location.getX() - width / 2) - 30, (location.getY() - height / 2) - 30);
    }
}
