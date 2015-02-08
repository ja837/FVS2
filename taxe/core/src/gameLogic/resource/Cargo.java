package gameLogic.resource;

public class Cargo {
	
	/**
	 * Enum for the potential animals
	 *
	 */
	public enum Animal{
		SNAKE, BEAR, MONKEY, GIRAFFE, PENGUIN, SHEEP, ELEPHANT, OCTOPUS, ZEBRA, LION, PIG, DRAGON 	
	}
	
	public Cargo(){	
	}

	public static Animal getCargo(int x){
		return Animal.values()[x];
	}
	
	
	
	

}
