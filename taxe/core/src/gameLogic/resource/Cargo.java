package gameLogic.resource;

public class Cargo {
	
	/**
	 * Enum for the potential animals
	 *
	 */
	public enum Animal{
		PYTHON, BEAR, MONKEY, GIRAFFE, PENGUIN, HORSE, FROG, COCKROACH, ZEBRA, LION, TOUCAN, PANDA
	}
	
	public Cargo(){	
	}

	public static Animal getCargo(int x){
		return Animal.values()[x];
	}	
	
	

}
