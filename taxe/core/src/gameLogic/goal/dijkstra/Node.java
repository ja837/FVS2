package gameLogic.goal.dijkstra;

import gameLogic.map.Connection;
import gameLogic.map.Map;
import gameLogic.map.Station;

import java.util.ArrayList;

/** Class for converting stations into nodes for dijkstra

*/

public class Node implements Comparable<Node>{
	private Station station;
	private String name;
	private ArrayList<Edge> edges;
	public float minDistance;
	public Node previous;
	private int count;
	
	
	
	public Node(Station argStation){
		this.name = argStation.getName();
		this.station = argStation;
		this.edges = new ArrayList<Edge>();
		this.minDistance = Float.POSITIVE_INFINITY;
	}
	
	
	/**
	 * converting Connection -> edges for dijkstra
	 * @param m
	 * @return
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
	 * @param m
	 * @param argStation
	 * @return
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
	
	
	public int compareTo(Node other)
    {
        return Double.compare(this.minDistance, other.minDistance);
    }
	
	
	public String getName(){
		return name;
	}
	public ArrayList<Edge> getEdges(){
		return edges;
	}
	/*
	public float getMinDistance(){
		return minDistance;
	}*/
	
	public Station getStation(){
		return station;
	}
	
	public int setCount(int i){
		return count = i;
	}
	public int getCount(){
		return count;
	}
	
	
	
	public String edgeToString(){
		String edgeString = "";
		for (Edge e : getEdges()){
			edgeString += (e.toString() + " ");
		}
		
		return edgeString;
	}
	
	@Override
	public String toString(){
		return (("Name : " + name)  + " minDistance : " + minDistance); 
	}

}
