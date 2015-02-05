package fvs.taxe.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import gameLogic.map.IPositionable;

public class StopSignActor extends Image {
    private static int width = 20;
    private static int height = 20;

    public StopSignActor(IPositionable location) {
        super(new Texture(Gdx.files.internal("stop3.png")));

        setSize(width, height);
        setPosition(location.getX() - width / 2, location.getY() - height / 2 + 15);
    }
}
