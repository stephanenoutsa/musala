package com.musala.drones.services;

import com.musala.drones.constants.State;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.entities.Drone;
import com.musala.drones.entities.Medication;
import com.musala.drones.repositories.DroneRepository;
import com.musala.drones.repositories.MedicationRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

@Service
public class MedicationService {
	@Autowired
	private DroneRepository droneRepository;
	
	@Autowired
	private MedicationRepository medicationRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public static String UPLOAD_DIRECTORY = "uploads";
	
	public Map<String, String> uploadImage(MultipartFile file) {
		try {
			StringBuilder fileNames = new StringBuilder();
			
	        Path fileNameAndPath = Paths.get(UPLOAD_DIRECTORY, file.getOriginalFilename());
	        fileNames.append(file.getOriginalFilename());
	        Files.write(fileNameAndPath, file.getBytes());
	        System.out.println("Uploaded images: " + fileNameAndPath.toString());
	        
	        Map<String, String> imageUrl = new HashMap<String, String>();
	        imageUrl.put("imageUrl", fileNameAndPath.toString());
	        
	        return imageUrl;
		} catch (IOException e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public MedicationDto saveMedication(MedicationDto medicationDto) {
		try {
			// TODO: Validate medicationDto

			Medication medication = convertDtoToEntity(medicationDto);
			
			medication = this.medicationRepository.save(medication);
			
			if (medication == null) {
				return null;
			}
			
			return convertEntityToDto(medication);
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public List<MedicationDto> findMedications() {
		try {
			List<Medication> medications = this.medicationRepository.findAll();
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
	
	@Transactional
	public Map<String, HttpStatus> loadMedicationsOnDrone(UUID serialNumber, List<String> medicationCodes) {
		Map<String, HttpStatus> response = new HashMap<String, HttpStatus>();
		
		try {
			Optional<Drone> drone = this.droneRepository.findBySerialNumber(serialNumber);
			
			if (!drone.isPresent()) {
				response.put("Drone not found", HttpStatus.NOT_FOUND);
				
				return response;
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
				response.put("Drone battery level is too low", HttpStatus.FORBIDDEN);
				
				return response;
			}
			
			// Set drone state to 'LOADING'
			this.droneRepository.updateDroneState(State.LOADING, serialNumber);
			
			int numberLoaded = 0;
			
			for (Medication medication : medicationsToLoad) {
				int weight = medication.getWeight();
				String code = medication.getCode();
				
				if (weight > availableCapacity || loadedMedicationCodes.contains(code)) {
					continue;
				}
				
				this.medicationRepository.loadMedicationOnDrone(drone.get(), code);
				
				availableCapacity -= weight;
				numberLoaded++;
			}
			
			// Set drone state to 'LOADED'
			this.droneRepository.updateDroneState(State.LOADED, serialNumber);
			
			String message = String.format("%d out of %d medications loaded on", numberLoaded, medicationCodes.size());
			
			response.put(message, HttpStatus.OK);
			
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			
			response.put(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
			
			return response;
		}
	}
	
	public List<MedicationDto> findMedicationsOnDrone(UUID serialNumber) {
		try {
			List<Medication> medications = this.medicationRepository.findMedicationsOnDrone(serialNumber);
			
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
	
	private Medication convertDtoToEntity(MedicationDto medicationDto) {
		Medication medication = modelMapper.map(medicationDto, Medication.class);
		
		return medication;
	}
	
	private MedicationDto convertEntityToDto(Medication medication) {
		MedicationDto medicationDto = modelMapper.map(medication, MedicationDto.class);
		
		return medicationDto;
	}
}
