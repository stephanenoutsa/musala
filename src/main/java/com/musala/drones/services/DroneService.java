package com.musala.drones.services;

import com.musala.drones.constants.State;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.entities.Drone;
import com.musala.drones.repositories.DroneRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DroneService {
	@Autowired
	private DroneRepository droneRepository;
	
	@Autowired
	private ModelMapper modelMapper;
	
	public DroneDto registerDrone(DroneDto droneDto) {
		try {
			Drone drone = convertDtoToEntity(droneDto);
			
			drone = this.droneRepository.save(drone);
			
			if (drone == null) {
				return null;
			}
			
			return convertEntityToDto(drone);
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	public List<DroneDto> findIdleDrones() {
		try {
			List<Drone> idleDrones = this.droneRepository.findByState(State.IDLE);
			
			List<DroneDto> idleDroneDtos = idleDrones
					.stream()
					.map(drone -> convertEntityToDto(drone))
					.collect(Collectors.toList());
			
			if (idleDroneDtos == null) {
				return new ArrayList<>();
			}
			
			return idleDroneDtos;
		} catch (Exception e) {
			e.printStackTrace();
			
			return new ArrayList<>();
		}
	}
	
	public Integer getDroneBatteryCapacity(String serialNumber) {
		try {
			UUID uuid = UUID.fromString(serialNumber);
			
			Optional<Drone> drone = this.droneRepository.findBySerialNumber(uuid);
			
			if (!drone.isPresent()) {
				return null;
			}
			
			return drone.get().getBatteryCapacity();
		} catch (Exception e) {
			e.printStackTrace();
			
			return null;
		}
	}
	
	private Drone convertDtoToEntity(DroneDto droneDto) {
		Drone drone = modelMapper.map(droneDto, Drone.class);
		
		return drone;
	}
	
	private DroneDto convertEntityToDto(Drone drone) {
		DroneDto droneDto = modelMapper.map(drone, DroneDto.class);
		
		return droneDto;
	}
}
