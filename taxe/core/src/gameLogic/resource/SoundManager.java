package gameLogic.resource;

import gameLogic.resource.Cargo.trainCargo;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;


public class SoundManager {
	Random random  = new Random();

	Music bananaPeel;

	Music bear ;
	Music elephant;
	Music giraffe;
	Music lion;
	Music monkey;
	Music penguin;
	Music snake;
	Music yeti;
	Music zebra;

	Music[] animals = new Music[13];

	Music[] newTrainArray = new Music[5];
	Music elephantBigger;

	Music[] speedBoost = new Music[2];

	Music bGMusic;

	public SoundManager(){
		this.bananaPeel = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/bananaPeel.wav"));
		this.animals[Cargo.trainCargo.BEAR.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/bear.wav"));
		this.animals[Cargo.trainCargo.ELEPHANT.ordinal()]= Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/elephantBiggerTrain.wav"));
		this.animals[Cargo.trainCargo.GIRAFFE.ordinal()]= Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/giraffe.wav"));
		this.animals[Cargo.trainCargo.LION.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/lion.wav"));
		this.animals[Cargo.trainCargo.MONKEY.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/monkey.wav"));
		this.animals[Cargo.trainCargo.PENGUIN.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/penguin.wav"));
		this.animals[Cargo.trainCargo.YETI.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/yetiGL.wav"));
		this.animals[Cargo.trainCargo.ZEBRA.ordinal()] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/zebra.wav"));

		this.newTrainArray[0] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/newTrain.wav"));
		this.newTrainArray[1] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/newTrain1.wav"));
		this.newTrainArray[2] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/here'sANewTrainForYou.wav"));
		this.newTrainArray[3] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/here'sANewTrainForYou1.wav"));
		this.newTrainArray[4] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/fundingForNewTrain.wav"));

		this.speedBoost[0] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/transportAnimalsFaster.wav"));
		this.speedBoost[1] = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/transportAnimalsFaster1.wav"));

		this.bGMusic = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/BGMusicLoop.wav"));

		this.elephantBigger = Gdx.audio.newMusic(Gdx.files.internal("wavs/wavs/elephantBiggerTrain.wav"));

	}


	public void playBGMusic(){
		bGMusic.play();
		bGMusic.setLooping(true);
	}

	public void playRandomNewTrain(){
		if (random.nextInt(2) == 0){
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


	public void playAnimal(trainCargo a){
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
				if (animals[a.ordinal()] != null){
					animals[a.ordinal()].play();
				}
				
			}
		}
	}



}
