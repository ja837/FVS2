package gameLogic.resource;

public class Cargo {
	
	/**
	 * Enum for the potential animals
	 *
	 */
	public enum trainCargo{
		SNAKE, BEAR, MONKEY, GIRAFFE, PENGUIN, ZEBRA, LION, OCTOPUS, SHEEP, DRAGON, PIG, ELEPHANT, YETI
	}
	
	public Cargo(){	
	}


	public static trainCargo getCargo(int x){
		return trainCargo.values()[x];
	}	
	
	public static String toString(int x){
		return trainCargo.values()[x].toString();
	}

}
