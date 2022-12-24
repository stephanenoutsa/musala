package com.musala.drones.tasks;

import com.musala.drones.entities.BatteryLevelRecord;
import com.musala.drones.entities.Drone;
import com.musala.drones.repositories.BatteryLevelRecordRepository;
import com.musala.drones.repositories.DroneRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PeriodicTasks {
	@Autowired
	private BatteryLevelRecordRepository batteryLevelRecordRepository;
	
	@Autowired
	private DroneRepository droneRepository;

	@Scheduled(fixedRate = 60000)
	public void recordBatteryLevels() {
		try {
			List<Drone> drones = this.droneRepository.findAll();

			for (Drone drone : drones) {
				BatteryLevelRecord record = new BatteryLevelRecord();
				
				record.setDrone(drone);
				record.setBatteryCapacity(drone.getBatteryCapacity());
				record.setRecordedAt(LocalDateTime.now());

				record = this.batteryLevelRecordRepository.save(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
