package com.musala.drones.repositories;

import com.musala.drones.entities.Drone;
import com.musala.drones.entities.Medication;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Integer> {
	@Query("select m from Medication m where m.drone.serialNumber = :serialNumber")
	List<Medication> findMedicationsOnDrone(@Param(value = "serialNumber") UUID serialNumber);
	
	@Modifying
	@Query("update Medication m set m.drone = :drone where m.code = :code")
	void loadMedicationOnDrone(@Param(value = "drone") Drone drone, @Param(value = "code") String code);
}
