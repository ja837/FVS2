package test;
/*
 * Tests Goal completion and also via goal logic
 */

import gameLogic.Game;
import gameLogic.Player;
import gameLogic.PlayerManager;
import gameLogic.goal.Goal;
import gameLogic.goal.GoalManager;
import gameLogic.map.Position;
import gameLogic.map.Station;
import gameLogic.resource.ResourceManager;
import gameLogic.resource.Train;

import org.junit.Before;
import org.junit.Test;



import java.util.ArrayList;

import static org.junit.Assert.*;

public class GoalManagerTest extends LibGdxTest {
    GoalManager gm;
    PlayerManager pm;

    @Before
    public void setup() throws Exception {
        Game game = gameLogic.Game.getInstance();
        gm = new GoalManager(game);
        pm = new PlayerManager();

    }

    @Test
    public void goalManagerTest() throws Exception {
        pm.createPlayers(2);
        Player player1 = pm.getCurrentPlayer();

        Train train = new Train("Green", "", "", 100);

        Station station1 = new Station("station1","s1", new Position(5, 5));
        Station station2 = new Station("station2", "s2", new Position(2, 2));
        Station station3 = new Station("station3", "s3", new Position(4, 3));

        
        Goal goal = new Goal(station1, station2, station3,0,0);
        player1.addGoal(goal);
        player1.addResource(train);

        ArrayList<Station> route = new ArrayList<Station>();
        route.add(station1);
        route.add(station3);
        route.add(station2);
        
        train.setRoute(route);

        train.addHistory("station1", 0);
       
        pm.turnOver();
        pm.turnOver();
        
        train.addHistory("station3", 1);
       

        //assertFalse("Goal was completed in error", goal.isComplete(train)

        
        pm.turnOver();
        pm.turnOver();
        
        train.addHistory("station2", 2);
        
        ArrayList<String> completedStrings = gm.trainArrived(train, player1);
        assertTrue("Goal wasn't completed", goal.isComplete(train));
        assertTrue("Completed goal string not right", completedStrings.size() > 0);

    }
}
