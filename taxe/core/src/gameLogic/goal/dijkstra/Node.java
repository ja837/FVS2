package gameLogic.goal.dijkstra;

import gameLogic.map.Connection;
import gameLogic.map.Map;
import gameLogic.map.Station;

import java.util.ArrayList;

/**
 * Class for converting stations into nodes to use in Dijkstra
 * @author Chris
 *
 */
public class Node implements Comparable<Node>{
	private Station station;
	private String name;
	private ArrayList<Edge> edges;
	public float minDistance;
	public Node previous;
	private int count;
	
	
	/**
	 * Constructor
	 * @param argStation Take a station as input
	 */
	public Node(Station argStation){
		this.name = argStation.getName();
		this.station = argStation;
		this.edges = new ArrayList<Edge>();
		this.minDistance = Float.POSITIVE_INFINITY;
	}
	
	
	/**
	 * Converting connections into edges
	 * @param m The game map
	 * @return An array list of edges
	 */
	public ArrayList<Edge> addConnectionsAsEdges(Map m){
		//does connection exist
		for (Connection c : m.getConnectionsFromStation(this.getStation())){
			if (m.doesConnectionExist(c.getStation1().getName(), c.getStation2().getName())){
				Edge e = new Edge(c);
				Dijkstra.lookupNode(this.getStation()).edges.add(e);
			}
		}
		
		return edges;	
	}

	/**
	 * look up from current node if connection exists
	 * @param m The game map
	 * @param argStation The station for lookup
	 * @return All edges from that station
	 */
	public Edge lookUpConnection(Map m, Station argStation){
		for (Edge e : edges){
			for (Connection c : m.getConnectionsFromStation(argStation)){
				if (Dijkstra.lookupNode(c.getStation2()) == e.getTarget()){
					return e;
				}
			}
		}
		throw new IllegalArgumentException("gfjhrfgj");
			
		}
	
	/**
	 * Comparing nodes
	 */
	public int compareTo(Node other)
    {
        return Double.compare(this.minDistance, other.minDistance);
    }
	
	/**
	 * 
	 * @return Node name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return All edges from this node
	 */
	public ArrayList<Edge> getEdges(){
		return edges;
	}

	/**
	 * 
	 * @return Station corresponding to this node
	 */
	public Station getStation(){
		return station;
	}
	
	/**
	 * Count to uniquely identify nodes
	 * @param i Integer
	 * @return A number for this node
	 */
	public int setCount(int i){
		return count = i;
	}
	
	/**
	 * 
	 * @return A node's count value
	 */
	public int getCount(){
		return count;
	}
	
	
	/**
	 * Converting all edges from this node into a string
	 * @return A string of all edges
	 */
	public String edgeToString(){
		String edgeString = "";
		for (Edge e : getEdges()){
			edgeString += (e.toString() + " ");
		}
		
		return edgeString;
	}
	
	@Override
	/**
	 * Converting a node to string
	 */
	public String toString(){
		return (("Name : " + name)  + " minDistance : " + minDistance); 
	}

}
