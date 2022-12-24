package com.musala.drones.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "medication")
public class Medication implements Serializable {
	private static final long serialVersionUID = 8657490796088845050L;

	@Id
	@Column(name = "code", unique = true, nullable = false)
	private String code;

	@Column(name = "name")
	private String name;
	
	@Column(name = "weight")
	private int weight;
	
	@Column(name = "image")
	private String image;

	@ManyToOne()
	@JoinColumn(name = "serial_number", referencedColumnName = "serial_number", nullable = true)
	private Drone drone;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	@Override
	public String toString() {
		return "Medication [code=" + code + ", name=" + name + ", weight=" + weight + ", image=" + image + ", drone="
				+ drone.getSerialNumber().toString() + "]";
	}
}
