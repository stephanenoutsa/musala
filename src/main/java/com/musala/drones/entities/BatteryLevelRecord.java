package com.musala.drones.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.Type;

@Entity
@Table(name = "battery_level_record")
public class BatteryLevelRecord implements Serializable {
	private static final long serialVersionUID = 602294547891100986L;

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	@Type(type="uuid-char")
	private UUID id;
	
	@Column(name = "battery_capacity")
	@Min(0)
	@Max(100)
	private Integer batteryCapacity;
	
	@Column(name = "recorded_at")
	private LocalDateTime recordedAt;

	@ManyToOne()
	@JoinColumn(name = "serial_number", referencedColumnName = "serial_number", nullable = false)
	private Drone drone;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getBatteryCapacity() {
		return batteryCapacity;
	}

	public void setBatteryCapacity(Integer batteryCapacity) {
		this.batteryCapacity = batteryCapacity;
	}

	public LocalDateTime getRecordedAt() {
		return recordedAt;
	}

	public void setRecordedAt(LocalDateTime recordedAt) {
		this.recordedAt = recordedAt;
	}

	public Drone getDrone() {
		return drone;
	}

	public void setDrone(Drone drone) {
		this.drone = drone;
	}

	@Override
	public String toString() {
		return "BatteryLevelRecord [id=" + id + ", batteryCapacity=" + batteryCapacity + ", recordedAt=" + recordedAt
				+ ", drone=" + drone.getSerialNumber().toString() + "]";
	}
}
