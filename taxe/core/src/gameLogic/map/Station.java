package gameLogic.map;

import fvs.taxe.actor.StationActor;

public class Station{
	private String name;
	private boolean isPassable;
	private boolean isControlled;
	private IPositionable location;
	private StationActor actor;
	private String acronym;
	
	private float speedModifier = 0f;


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

	public boolean isPassable() {
		return isPassable;
	}

	public void setPassable(boolean isPassable) {
		this.isPassable = isPassable;
	}

	public boolean isControlled() {
		return isControlled;
	}

	public void setControlled(boolean isControlled) {
		this.isControlled = isControlled;
	}

	public float getSpeedModifier() {
		return speedModifier;
	}

	public void setSpeedModifier(float speedmodifier) {
		this.speedModifier = speedmodifier;
	}
	
	public String toString(){
		return getName();
	}
	

}
