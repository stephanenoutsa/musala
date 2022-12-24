package com.musala.drones.services;

import com.musala.drones.constants.State;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.entities.Drone;
import com.musala.drones.entities.Medication;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.MedicationRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedicationService {
	@Autowired
	private DroneRepository droneRepository;
	
	@Autowired
	private MedicationRepository medicationRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public Boolean loadMedicationsOnDrone(String serialNumber, List<String> medicationCodes) {
		try {
			UUID uuid = UUID.fromString(serialNumber);
			
			Optional<Drone> drone = this.droneRepository.findBySerialNumber(uuid);
			
			if (!drone.isPresent()) {
				return false;
			}
			int droneCapacity = drone.get().getWeightLimit();
			
			List<Medication> loadedMedications = drone.get().getMedications();

			List<String> loadedMedicationCodes = loadedMedications
					.stream()
					.map(medication -> medication.getCode())
					.collect(Collectors.toList());

			int totalWeight = loadedMedications
					.stream()
					.reduce(0, (subtotal, medication) -> subtotal + medication.getWeight(), Integer::sum);		
			int availableCapacity = droneCapacity - totalWeight;
			
			List<Medication> medicationsToLoad = this.medicationRepository.findByCodeIn(medicationCodes);
			
			if (drone.get().getBatteryCapacity() < 25) {
				return false;
			}
			
			// Set drone state to 'LOADING'
			this.droneRepository.updateDroneState(State.LOADING, uuid);
			
			for (Medication medication : medicationsToLoad) {
				int weight = medication.getWeight();
				String code = medication.getCode();
				
				if (weight > availableCapacity || loadedMedicationCodes.contains(code)) {
					continue;
				}
				
				this.medicationRepository.loadMedicationOnDrone(drone.get(), code);
				
				availableCapacity -= weight;
			}
			
			// Set drone state to 'LOADED'
			this.droneRepository.updateDroneState(State.LOADED, uuid);
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			
			return false;
		}
	}
	
	public List<MedicationDto> findMedicationsOnDrone(String serialNumber) {
		try {
			UUID uuid = UUID.fromString(serialNumber);
			
			List<Medication> medications = this.medicationRepository.findMedicationsOnDrone(uuid);
			
			List<MedicationDto> medicationDtos = medications
					.stream()
					.map(medication -> convertEntityToDto(medication))
					.collect(Collectors.toList());
			
			if (medicationDtos == null) {
				return new ArrayList<>();
			}
			
			return medicationDtos;
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ArrayList<>();
		}
	}
	
	private MedicationDto convertEntityToDto(Medication medication) {
		MedicationDto medicationDto = modelMapper.map(medication, MedicationDto.class);
		
		return medicationDto;
	}
}
