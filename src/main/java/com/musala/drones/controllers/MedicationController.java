package com.musala.drones.controllers;

import com.musala.drones.dto.MedicationDto;
import com.musala.drones.services.MedicationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class MedicationController {
	@Autowired
	private MedicationService medicationService;
	
	@PostMapping("medications/upload-image")
	public ResponseEntity<?> uploadImage(@RequestParam(value = "image", required = false) MultipartFile file) {
		Map<String, String> imageUrl = this.medicationService.uploadImage(file);
		
		return imageUrl != null
				? new ResponseEntity<>(imageUrl, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping("/medications")
	public ResponseEntity<?> saveMedication(@RequestBody @Valid MedicationDto medicationDto) {
		MedicationDto response = this.medicationService.saveMedication(medicationDto);

		return response != null
				? new ResponseEntity<>(response, HttpStatus.CREATED)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping("/medications")
	public ResponseEntity<?> findMedications() {
		List<MedicationDto> medicationDtos = this.medicationService.findMedications();
		
		return new ResponseEntity<>(medicationDtos, HttpStatus.OK);
	}
	
	@PostMapping("/drones/{serialNumber}/medications")
	public ResponseEntity<?> loadMedicationsOnDrone(@PathVariable UUID serialNumber, @RequestBody List<String> codes) {
		Map<String, HttpStatus> map = this.medicationService.loadMedicationsOnDrone(serialNumber, codes);
		Optional<Map.Entry<String,HttpStatus>> entry = map.entrySet().stream().findFirst();
		Map.Entry<String, HttpStatus> result = entry.get();
		
		String message = result.getKey();
		
		Map<String, String> response = new HashMap<String, String>();
		response.put("response", message);
		
		HttpStatus status = result.getValue();

		return new ResponseEntity<>(response, status);
	}
	
	@GetMapping("/drones/{serialNumber}/medications")
	public ResponseEntity<?> findMedicationsOnDrone(@PathVariable UUID serialNumber) {
		List<MedicationDto> medicationDtos = this.medicationService.findMedicationsOnDrone(serialNumber);
		
		return new ResponseEntity<>(medicationDtos, HttpStatus.OK);
	}
}
