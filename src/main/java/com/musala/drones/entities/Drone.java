package com.musala.drones.entities;

import com.musala.drones.constants.Model;
import com.musala.drones.constants.State;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "drone")
public class Drone implements Serializable {
	private static final long serialVersionUID = 5684597485999947004L;

	@Id
	@GeneratedValue
	@Column(name = "serial_number", length = 100, unique = true, nullable = false)
	@Type(type="uuid-char")
	private UUID serialNumber;
	
	@Column(name = "model")
	@Enumerated(EnumType.STRING)
	private Model model;
	
	@Column(name = "weight_limit")
	@Min(0)
	@Max(500)
	private Integer weightLimit;
	
	@Column(name = "battery_capacity")
	@Min(0)
	@Max(100)
	private Integer batteryCapacity;
	
	@Column(name = "state")
	@Enumerated(EnumType.STRING)
	private State state;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "drone", fetch = FetchType.LAZY)
	private List<Medication> medications;

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

	public int getWeightLimit() {
		return weightLimit;
	}

	public void setWeightLimit(Integer weightLimit) {
		this.weightLimit = weightLimit;
	}

	public int getBatteryCapacity() {
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

	public List<Medication> getMedications() {
		return medications;
	}

	public void setMedications(List<Medication> medications) {
		this.medications = medications;
	}

	@Override
	public String toString() {
		return "Drone [serialNumber=" + serialNumber + ", model=" + model + ", weightLimit=" + weightLimit
				+ ", batteryCapacity=" + batteryCapacity + ", state=" + state + ", medications=" + medications + "]";
	}
}
