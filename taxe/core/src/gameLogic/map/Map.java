package gameLogic.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import gameLogic.goal.dijkstra.Dijkstra;
import gameLogic.goal.dijkstra.Edge;
import gameLogic.goal.dijkstra.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Map {
    private List<Station> stations;
    private List<Connection> connections;
    private Random random = new Random();
    //public Dijkstra d;

    public Map() {    	
        stations = new ArrayList<Station>();
        connections = new ArrayList<Connection>();
        initialise();
    }

    private void initialise() {
        JsonReader jsonReader = new JsonReader();
        JsonValue jsonVal = jsonReader.parse(Gdx.files.local("stations.json"));

        parseStations(jsonVal);
        parseConnections(jsonVal);
        
        initialiseNodeList();
    
    }

    private void parseConnections(JsonValue jsonVal) {
        for(JsonValue connection = jsonVal.getChild("connections"); connection != null; connection = connection.next) {
            String station1 = "";
            String station2 = "";

            for(JsonValue val = connection.child; val != null; val = val.next) {
                if(val.name.equalsIgnoreCase("station1")) {
                    station1 = val.asString();
                } else {
                    station2 = val.asString();
                }
            }

            addConnection(station1, station2);
        }
    }

    private void parseStations(JsonValue jsonVal) {
        for(JsonValue station = jsonVal.getChild("stations"); station != null; station = station.next) {
            String name = "";
            String acronym = "";
            int x = 0;
            int y = 0;
            boolean isJunction = false;
            boolean isControlled = false;

            for(JsonValue val = station.child; val != null; val = val.next) {
                if(val.name.equalsIgnoreCase("name")) {
                    name = val.asString();
                } else if(val.name.equalsIgnoreCase("acronym")) {
                    acronym = val.asString();
                } else if(val.name.equalsIgnoreCase("x")) {
                    x = val.asInt();
                } else if(val.name.equalsIgnoreCase("y")) {
                    y = val.asInt();
                } else if(val.name.equalsIgnoreCase("junction")) {
                    isJunction = val.asBoolean();
                } else{
                	isControlled = val.asBoolean();
                }
            }

            if (isJunction) {
                addJunction(name, "", new Position(x,y), isControlled);
            } else {
                addStation(name, acronym, new Position(x, y), isControlled);
            }
        }
        
    }

    public boolean doesConnectionExist(String stationName, String anotherStationName) {
        for (Connection connection : connections) {
            String s1 = connection.getStation1().getName();
            String s2 = connection.getStation2().getName();

            if (s1.equals(stationName) && s2.equals(anotherStationName)
                || s1.equals(anotherStationName) && s2.equals(stationName)) {
                return true;
            }
        }

        return false;
    }
    
    /**Setting up for Dijkstras
     */
    public void initialiseNodeList(){
    	//making nodes
    	int i =0;
    	Dijkstra.nodeList = new ArrayList<Node>(); 
    	for (Station stn : stations){
			Node n = new Node(stn); 
			Dijkstra.nodeList.add(n);
			n.setCount(i);
			i += 1;
    	}
    	//making connections
    	for (Node n : Dijkstra.nodeList){
    		n.addConnectionsAsEdges(this);	
    		}
    	//adding score from x to y in array
       	Dijkstra.allDistances = new int[Dijkstra.nodeList.size()][Dijkstra.nodeList.size()];
    	//adding to array table for lookup
    	for (Node n1 : Dijkstra.nodeList){
    		Dijkstra.computePath(n1); //compute paths to all nodes
    		for (Node n2 : Dijkstra.nodeList){
    			Dijkstra.allDistances[n1.getCount()][n2.getCount()] = (int) n2.minDistance;
    		}
    	}   	
    	System.out.println(Arrays.deepToString(Dijkstra.allDistances));
    }

	public Station getRandomStation() {
        return stations.get(random.nextInt(stations.size()));
    }

    public Station addStation(String name, String acronym, Position location, Boolean isControlled) {
        Station newStation = new Station(name, acronym, location);
        newStation.setPassable(true);
        if(random.nextInt(10)<2){
        	newStation.setControlled(true);
        }
        else{
        	newStation.setControlled(false);
        }
        stations.add(newStation);
        return newStation;
    }
    
    public Station deleteStation(Station station){
    	stations.remove(station);
		return station;    	
    }
    
    public CollisionStation addJunction(String name, String acronym, Position location, Boolean isControlled) {
    	CollisionStation newJunction = new CollisionStation(name, acronym, location);
    	stations.add(newJunction);
    	newJunction.setPassable(true);
        newJunction.setControlled(isControlled);
    	return newJunction;
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Connection addConnection(Station station1, Station station2) {
        Connection newConnection = new Connection(station1, station2);
        connections.add(newConnection);
        return newConnection;
    }

    //Add Connection by Names
    public Connection addConnection(String station1, String station2) {
        Station st1 = getStationByName(station1);
        Station st2 = getStationByName(station2);
        return addConnection(st1, st2);
    }

    //Get connections from station
    public List<Connection> getConnectionsFromStation(Station station) {
        List<Connection> results = new ArrayList<Connection>();
        for(Connection connection : connections) {
            if(connection.getStation1() == station || connection.getStation2() == station) {
                results.add(connection);
            }
        }
        return results;
    }

    public Station getStationByName(String name) {
        int i = 0;
        while(i < stations.size()) {
            if(stations.get(i).getName().equals(name)) {
                return stations.get(i);
            } else{
                i++;
            }
        }
        return null;
    }

    public Station getStationFromPosition(IPositionable position) {
        for (Station station : stations) {
            if (station.getLocation().equals(position)) {
                return station;
            }
        }

        throw new RuntimeException("Station does not exist for that position");
    }

    public List<Station> createRoute(List<IPositionable> positions) {
        List<Station> route = new ArrayList<Station>();

        for (IPositionable position : positions) {
            route.add(getStationFromPosition(position));
        }

        return route;
    }
    
    private float getDistance(Station a, Station b){
		return Vector2.dst(a.getLocation().getX(), a.getLocation().getY(), b.getLocation().getX(), b.getLocation().getY());
	}
}
