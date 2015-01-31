package gameLogic.map;

import fvs.taxe.actor.StationActor;

public class Station{
	private String name;
	private IPositionable location;
	private StationActor actor;
	private String acronym;

	public Station(String name, String acronym, IPositionable location) {
		this.name = name;
		this.acronym = acronym; //3 letter station acronym
		this.location = location;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}
	
	public IPositionable getLocation() {
		return location;
	}
	public void setLocation(IPositionable location) {
		this.location = location;
	}
	
	public void setActor(StationActor actor){
		this.actor = actor;
	}
	
	public StationActor getActor(){
		return actor;
	}
	
}
