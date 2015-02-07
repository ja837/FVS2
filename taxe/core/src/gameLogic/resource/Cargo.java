package gameLogic.resource;

public class Cargo {
	
	/**
	 * Enum for the potential animals
	 *
	 */
	public enum trainCargo{
		PYHTON, BEAR, MONKEY, GIRAFFE, PENGUIN, HORSE, FROG, COCKROACH, ZEBRA, LION, TOUCAN, PANDA, OCTOPUS, SNAKE, SHEEP, DRAGON, PIG, ELEPHANT
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
