package test;

import gameLogic.map.Map;
import gameLogic.map.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MapTest extends LibGdxTest{
    private Map map;

    @Before
    public void mapSetup() throws Exception {
        map = new Map();
    }

    @Test
    public void addStationAndConnectionTest() throws Exception {
        String name1 = "station1";
        String name2 = "station2";
        String acro1 = "st1";
        String acro2 = "st2";

        int previousSize = map.getStations().size();

        map.addStation(name1,acro1, new Position(9999, 9999),true);
        map.addStation(name2,acro2, new Position(200,200),true);

        assertTrue("Failed to add stations", map.getStations().size() - previousSize == 2);

        map.addConnection(name1, name2);
        assertTrue("Connection addition failed", map.doesConnectionExist(name2,  name1));

        // Should throw an error by itself
        map.getStationFromPosition(new Position(9999, 9999));
    }
}
