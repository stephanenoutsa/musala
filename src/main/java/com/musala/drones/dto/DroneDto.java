package com.musala.drones.dto;

import com.musala.drones.constants.Model;
import com.musala.drones.constants.State;

import java.util.UUID;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class DroneDto {
	private UUID serialNumber;
	
	@NotBlank(message = "Model value cannot be empty")
	private Model model;
	
	@NotBlank(message = "Weight limit cannot be empty")
	@Length(min = 0, max = 500, message = "Weight limit should be betweeen 0 and 500gr")
	private Integer weightLimit;
	
	@NotBlank(message = "Battery capacity cannot be empty")
	@Length(min = 0, max = 100, message = "Battery capacity should be betweeen 0 and 100%")
	private Integer batteryCapacity;

	@NotBlank(message = "State cannot be empty")
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
