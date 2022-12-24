package com.musala.drones.repositories;

import com.musala.drones.entities.BatteryLevelRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryLevelRecordRepository extends JpaRepository<BatteryLevelRecord, Integer> {
	//
}
