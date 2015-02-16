package gameLogic.resource;

import Util.Tuple;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import gameLogic.Player;

import java.util.ArrayList;
import java.util.Random;

public class ResourceManager {
    public final int CONFIG_MAX_RESOURCES = 7;
    private Random random = new Random();
    private ArrayList<Tuple<String, Integer>> trains;
    
    public ResourceManager() {
    	initialise();
    }
    
    private void initialise() {
    	JsonReader jsonReader = new JsonReader();
    	JsonValue jsonVal = jsonReader.parse(Gdx.files.local("trains.json"));
    	
    	trains = new ArrayList<Tuple<String, Integer>>();
    	for(JsonValue train = jsonVal.getChild("trains"); train != null; train = train.next()) {
    		String name = "";
    		int speed = 50;
    		for(JsonValue val  = train.child; val != null; val = val.next()) {
    			if(val.name.equalsIgnoreCase("name")) {
    				name = val.asString();
    			} else {
    				speed = val.asInt();
    			}
    		}
    		trains.add(new Tuple<String, Integer>(name, speed));
    	}
    }
    
    public ArrayList<String> getTrainNames() {
		ArrayList<String> names = new ArrayList<String>();
		for(Tuple<String,Integer> train : trains) {
			names.add(train.getFirst());
		}
		return names;
	}
	
	public ArrayList<Tuple<String, Integer>> getTrains() {
		return trains;
	}
/**
 * generates random integer, if integer between certain numbers, particular train is returned
 */
    private Resource getRandomResource() {
            	
    	int index = random.nextInt(100);
    	if (index == 98 || index == 99) {
    		Tuple<String, Integer> train = trains.get(4);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (94<=index && index<98) {
    		Tuple<String, Integer> train = trains.get(9);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (88<=index && index<94) {
    		Tuple<String, Integer> train = trains.get(3);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (80<=index && index<88) {
    		Tuple<String, Integer> train = trains.get(6);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (70<=index && index<80) {
    		Tuple<String, Integer> train = trains.get(5);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (60<=index && index<70) {
    		Tuple<String, Integer> train = trains.get(0);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (48<=index && index<60) {
    		Tuple<String, Integer> train = trains.get(2);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (34<=index && index<48) {
    		Tuple<String, Integer> train = trains.get(1);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else if (18<=index && index<34) {
    		Tuple<String, Integer> train = trains.get(8);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	} else {
    		Tuple<String, Integer> train = trains.get(7);
        	return new Train(train.getFirst(), train.getFirst().replaceAll(" ", "") + ".png", train.getFirst().replaceAll(" ", "") + "Right.png",train.getSecond());
    	}
    	
    }

    public void addRandomResourceToPlayer(Player player) {
        addResourceToPlayer(player, getRandomResource());
    }

    private void addResourceToPlayer(Player player, Resource resource) {
        if (player.getResources().size() >= CONFIG_MAX_RESOURCES) {
			return;
        }

        resource.setPlayer(player);
        player.addResource(resource);
    }
}