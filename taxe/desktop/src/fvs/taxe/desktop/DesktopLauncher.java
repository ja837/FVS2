package fvs.taxe.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import fvs.taxe.TaxeGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//Set window size
		config.height = TaxeGame.HEIGHT;
		config.width = TaxeGame.WIDTH;
		config.title = "TaxE";
		config.resizable = false;
		config.addIcon("icon/ic_launcher4.png", FileType.Internal);
		config.addIcon("icon/ic_launcher3.png", FileType.Internal);
		config.addIcon("icon/ic_launcher2.png", FileType.Internal);
		config.addIcon("icon/ic_launcher1.png", FileType.Internal);
		config.addIcon("icon/ic_launcher0.png", FileType.Internal);
		//config.fullscreen = true;
		new LwjglApplication(new TaxeGame(), config);
	}
}
