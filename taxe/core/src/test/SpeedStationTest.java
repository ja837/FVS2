package test;


import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.map.Position;
import gameLogic.map.Station;
import gameLogic.resource.Train;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class SpeedStationTest {
    Train train;
    
    @Before
    public void trainSetup() throws Exception {
        train = new Train("RedTrain", "RedTrain.png", "RedTrainRight.png", 250);

    }

    @Test
    public void speedStationTest() throws Error {
        
    	PlayerManager pm = new PlayerManager();
        pm.createPlayers(2);
        Player player1 = pm.getCurrentPlayer();
        player1.addResource(train);
    	Station station1 = new Station("station1","st1", new Position(5, 5));
        Station station2 = new Station("station2","st2", new Position(6, 6));
        
        station2.setSpeedModifier(2); // modifier of 2 so train speed should double when it reaches the station
       
        ArrayList<Station> route = new ArrayList<Station>();
        
        route.add(station1);
    
        route.add(station2);
        route.add(station1);
        
        train.setRoute(route);
        
        assertTrue("Train has wrong initial speed", train.getSpeed() == 250);
       
        train.addHistory("station1", 5);
        
        pm.turnOver();
        pm.turnOver();
        
        train.addHistory("station2", 6);
        
        pm.turnOver();
        pm.turnOver();
        
        if(station2.getSpeedModifier() != 0){
        	int newSpeed = (int) (train.getSpeed() * station2.getSpeedModifier());
            System.out.println("Train '" + train.toString() + "' speed modified. Old speed = " + train.getSpeed() + ". New speed = " + newSpeed);
            train.setSpeed(newSpeed);
            
        }
        
        
        assertTrue("Train has wrong final speed", train.getSpeed() == 500);
    }


}
