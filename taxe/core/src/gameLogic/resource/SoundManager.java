package gameLogic.resource;

import gameLogic.resource.Cargo.trainCargo;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Manages the playng of all sound clips, inlcudes music and voice overs.
 * @author Jamie
 *
 */
public class SoundManager {
	Random random  = new Random();

	Music[] animals = new Music[13];
	Music[] newTrainArray = new Music[5];
	Music[] speedBoost = new Music[2];
	Music[] junctionFailure = new Music[2];
	
	Music bGMusic;
	Music highSpeed;
	
	/**
	 * Initialises all of the sound files
	 */
	public SoundManager(){
		this.animals[Cargo.trainCargo.BEAR.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/bear.ogg"));
		this.animals[Cargo.trainCargo.ELEPHANT.ordinal()]= Gdx.audio.newMusic(Gdx.files.internal("sounds/elephantBiggerTrain.ogg"));
		this.animals[Cargo.trainCargo.GIRAFFE.ordinal()]= Gdx.audio.newMusic(Gdx.files.internal("sounds/giraffe.ogg"));
		this.animals[Cargo.trainCargo.LION.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/lion.ogg"));
		this.animals[Cargo.trainCargo.MONKEY.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/monkey.ogg"));
		this.animals[Cargo.trainCargo.PENGUIN.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/penguin.ogg"));
		this.animals[Cargo.trainCargo.YETI.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/yetiGL.ogg"));
		this.animals[Cargo.trainCargo.ZEBRA.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("sounds/zebra.ogg"));
		
		this.newTrainArray[0] = Gdx.audio.newMusic(Gdx.files.internal("sounds/newTrain.ogg"));
		this.newTrainArray[1] = Gdx.audio.newMusic(Gdx.files.internal("sounds/newTrain1.ogg"));
		this.newTrainArray[2] = Gdx.audio.newMusic(Gdx.files.internal("sounds/here'sANewTrainForYou.ogg"));
		this.newTrainArray[3] = Gdx.audio.newMusic(Gdx.files.internal("sounds/here'sANewTrainForYou1.ogg"));
		this.newTrainArray[4] = Gdx.audio.newMusic(Gdx.files.internal("sounds/fundingForNewTrain.ogg"));

		this.speedBoost[0] = Gdx.audio.newMusic(Gdx.files.internal("sounds/transportAnimalsFaster.ogg"));
		this.speedBoost[1] = Gdx.audio.newMusic(Gdx.files.internal("sounds/transportAnimalsFaster1.ogg"));
		
		this.junctionFailure[0] = Gdx.audio.newMusic((Gdx.files.internal("sounds/junctionFailure.ogg")));
		this.junctionFailure[1] = Gdx.audio.newMusic((Gdx.files.internal("sounds/junctionFailure.ogg")));

		this.bGMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/BGMusicLoop.ogg"));
		
		this.highSpeed = Gdx.audio.newMusic(Gdx.files.internal("sounds/highSpeed.ogg"));

	}

	/**
	 * Play the background music
	 */
	public void playBGMusic(){
		bGMusic.setVolume(0.55f);
		bGMusic.play();
		bGMusic.setLooping(true);
	}
	
	/**
	 * 
	 * @return the background music track
	 */
	public Music getBGMusic(){
		return bGMusic;
	} 
	
	/**
	 * Play a random voiceover from the list of junction failure voiceovers.
	 */
	public void playRandomJunctionFailure(){
		junctionFailure[random.nextInt(2)].play();
	}
	
	/**
	 * Play a random voiceover from the list of new train voiceovers.
	 */
	public void playRandomNewTrain(){
		if (random.nextInt(4) == 0){
			Boolean play = true;
			for (Music m : speedBoost){
				if (m.isPlaying()){
					play = false;
				}
			}
			for (Music m : animals){
				if (m != null){
					if (m.isPlaying()){
						play = false;
					}
				}
			}
			if(play){
				newTrainArray[random.nextInt(5)].play();
			}
		}
	}

	/**
	 * Play a random voiceover from the list of speed boost voiceovers.
	 */
	public void playSpeedBoost(){
		Boolean play = true;
		for (Music m : newTrainArray){
			if (m.isPlaying()){
				play = false;
			}
		}
		for (Music m : animals){
			if (m != null){
				if (m.isPlaying()){
					play = false;
				}
			}
		}
		if (play){
			speedBoost[random.nextInt(2)].play();
		}
	}

	/**
	 * Play the voicevoer clip for the appropriate animal
	 * @param cargo 
	 */
	public void playAnimal(trainCargo cargo){
		if (true){
			Boolean play = true;
			for (Music m : newTrainArray){
				if (m.isPlaying()){
					play = false;
				}
			}
			for (Music m : speedBoost){
				if (m.isPlaying()){
					play = false;
				}
			}
			if (play){
				if (animals[cargo.ordinal()] != null){
					animals[cargo.ordinal()].play();
				}
				
			}
		}
	}
	
	public void playHighSpeed(){
		bGMusic.pause();
		highSpeed.setVolume(0.45f);
		highSpeed.play();
	}
	
	public Music getHighSpeed(){
		return highSpeed;
	}
	



}
