package com.mana;

import java.util.List;

public class State {

	List<Dimension> configuration;
    int presentColumn;
    int lizCount; 
    
	public int getPresentColumn() {
		return presentColumn;
	}

	public void setPresentColumn(int presentColumn) {
		this.presentColumn = presentColumn;
	}

	public int getLizCount() {
		return lizCount;
	}

	public void setLizCount(int lizCount) {
		this.lizCount = lizCount;
	}

	public State(List<Dimension> configuration,int lizCount,int presentColoumn) {
		this.configuration = configuration;
		this.presentColumn=presentColoumn;
		this.lizCount=lizCount;
	}

	public List<Dimension> getConfiguration() {
		return configuration;
	}

	public void setConfiguration(List<Dimension> configuration) {
		this.configuration = configuration;
	}
	
	public String toString() {
		return "State:"+configuration.toString();
		
	}

}
