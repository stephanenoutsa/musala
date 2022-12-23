package com.musala.drones.repositories;

import com.musala.drones.constants.State;
import com.musala.drones.entities.Drone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Integer> {
	List<Drone> findByState(State state);
	
	Optional<Drone> findBySerialNumber(UUID serialNumber);
	
	@Modifying
	@Query("update Drone d set d.state = :state where d.serialNumber = :serialNumber")
	void updateDroneState(@Param(value = "state") State state, @Param(value = "serialNumber") UUID serialNumber);
}
