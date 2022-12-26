package com.musala.drones.controllers;

import com.musala.drones.constants.State;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.services.DroneService;

import java.util.List;
import java.util.Map;
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

@RestController
@Validated
public class DroneController {
	@Autowired
	private DroneService droneService;
	
	@GetMapping("/drones")
	public ResponseEntity<?> findDrones(@RequestParam(name = "state", required = false) State state) {
		List<DroneDto> drones = this.droneService.findDrones(state);
		
		return new ResponseEntity<>(drones, HttpStatus.OK);
	}
	
	@GetMapping("/drones/{serialNumber}")
	public ResponseEntity<?> getDroneBatteryCapacity(@PathVariable UUID serialNumber) {
		Map<String, Integer> result = this.droneService.getDroneBatteryCapacity(serialNumber);
		
		return result != null
				? new ResponseEntity<>(result, HttpStatus.OK)
				: new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/drones")
	public ResponseEntity<?> registerDrone(@RequestBody @Valid DroneDto droneDto) {
		DroneDto response = this.droneService.registerDrone(droneDto);
		
		return response != null
				? new ResponseEntity<>(response, HttpStatus.CREATED)
				: new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
