package com.musala.drones.dto;

import com.musala.drones.annotations.EnumPattern;
import com.musala.drones.constants.Model;
import com.musala.drones.constants.State;

import java.util.UUID;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class DroneDto {
	private UUID serialNumber;
	
	@EnumPattern(regexp = "Lightweight|Middleweight|Cruiserweight|Heavyweight")
	private Model model;
	
	@Min(0)
	@Max(500)
	private Integer weightLimit;
	
	@Min(0)
	@Max(100)
	private Integer batteryCapacity;

	@EnumPattern(regexp = "IDLE|LOADING|LOADED|DELIVERING|DELIVERED|RETURNING")
	private State state;

	public UUID getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(UUID serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public Integer getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Integer weightLimit) {
		this.weightLimit = weightLimit;
	}

	public Integer getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(Integer batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
