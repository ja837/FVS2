package gameLogic.goal.dijkstra;

import com.badlogic.gdx.math.Vector2;

import gameLogic.map.Connection;
import gameLogic.map.Station;


/**
 * Class for converting connections into edges to use in dijkstra
 * @author Chris
 *
 */
public class Edge {
	
	private final Node target;
	private final Node current;
	private final Connection conn;
	private float weight;
	
	public Edge(Connection argConn){
		this.conn = argConn;
		this.current = Dijkstra.lookupNode(conn.getStation1());
		this.target = Dijkstra.lookupNode(conn.getStation2());
		this.weight = getDistance(conn.getStation1(), conn.getStation2()); 
	}
	
	
	
	/**
	 * gets the distance between 2 stations
	 * @param a - first station
	 * @param b - second station
	 * @return the distance between them
	 */
	private float getDistance(Station a, Station b){
		return Vector2.dst(a.getLocation().getX(), a.getLocation().getY(), b.getLocation().getX(), b.getLocation().getY());
	}
	
	/**
	 * 
	 * @return Return the weight of an edge
	 */
	public float getWeight(){
		return weight;
	}
	
	/**
	 * 
	 * @return The connection the edge derived from
	 */
	public Connection getConnection(){
		return conn;
	}
	
	/**
	 * 
	 * @return The node at the other side of the edge
	 */
	public Node getTarget(){
		return target;
	}
	
	/**
	 * 
	 * @return The node at the start of the edge
	 */
	public Node getCurrent(){
		return current;
	}
	
	/**
	 * 
	 * @return A string with station names
	 */
	@Override
	public String toString(){
		return (conn.getStation1().getName() + " to " + conn.getStation2().getName() + " - " + getWeight());
	}

}
