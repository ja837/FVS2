package gameLogic.goal.dijkstra;

import gameLogic.map.Map;
import gameLogic.map.Station;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/** Class for calculating distances to every node to use as a scoring mechanism
 * 
 * 
 */
public class Dijkstra {
	
	public static ArrayList<Node> nodeList;
	public static int[][] allDistances;
	
	//distance as number - use node.ninDistance
	public static void computePath(Node source){
		source.minDistance = 0;
		PriorityQueue<Node> stationQueue = new PriorityQueue<Node>();
		stationQueue.add(source);
		
		while (!stationQueue.isEmpty()){
			Node u = stationQueue.poll();

			for (Edge e : u.getEdges()){
				Node v = e.getTarget();
				if (v == u){
					v = e.getCurrent();
				}
				float weight = e.getWeight();
				float distanceThruU = u.minDistance + weight;
				if (distanceThruU < v.minDistance){
					stationQueue.remove(v);
					v.minDistance = distanceThruU;
					v.previous = u;
					stationQueue.add(v);
					allDistances[u.getCount()][v.getCount()] = (int) v.minDistance;
				}
			}
		}		
	}
	
	//actual path through edges
	//infinite loop when called
	public static List<Node> getShortestPathTo(Node argTarget){
		List<Node> path = new ArrayList<Node>();
		for (Node node = argTarget; node != null; node = node.previous){
			path.add(node);
		}
		Collections.reverse(path);
		return path;
	}

	
	//converting Stations -> nodes for dijkstra
	public ArrayList<Node> addStationsToNodeList(Map m){
		for (Station stn : m.getStations()){
			Node n = new Node(stn);
			nodeList.add(n);
		}
		return nodeList;
	}
	 
	//takes station and returns corresponding node
	public static Node lookupNode(Station argStation){
		for (Node n : nodeList){
			if (argStation.getName() == n.getName()){
				return n;
			}
		}
		throw new IllegalArgumentException("Station doens't exist");
	}
	
	
	public static String nodesToString(){
		String nodes = "";
		for (Node n : nodeList){
			nodes += (n.getName() + " ");
		}
		return nodes;
	}
	
	
}
