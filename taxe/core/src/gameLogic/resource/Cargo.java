package gameLogic.resource;

public class Cargo {
	
	public enum trainCargo{
		PYHTON, BEAR, MONKEY, GIRAFFE, PENGUIN, HORSE, FROG, COCKROACH, ZEBRA, LION, TOUCAN, PANDA
	}
	
	public Cargo(){	
	}

	public static String getCargo(int x){
		return trainCargo.values()[x].toString();
	}	
	
	

}
