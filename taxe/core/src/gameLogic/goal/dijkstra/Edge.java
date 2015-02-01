package gameLogic.goal.dijkstra;

import com.badlogic.gdx.math.Vector2;

import gameLogic.map.Connection;
import gameLogic.map.Station;

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
	
	
	
	
	private float getDistance(Station a, Station b){
		return Vector2.dst(a.getLocation().getX(), a.getLocation().getY(), b.getLocation().getX(), b.getLocation().getY());
	}
	
	public float getWeight(){
		return weight;
	}
	
	public Connection getConnection(){
		return conn;
	}
	public Node getTarget(){
		return target;
	}
	public Node getCurrent(){
		return current;
	}
	
	
	@Override
	public String toString(){
		return (conn.getStation1().getName() + " to " + conn.getStation2().getName() + " - " + getWeight());
	}

}
