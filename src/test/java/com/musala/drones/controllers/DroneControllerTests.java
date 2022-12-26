package com.musala.drones.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.constants.Model;
import com.musala.drones.constants.State;
import com.musala.drones.dto.DroneDto;
import com.musala.drones.services.DroneService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DroneController.class)
class DroneControllerTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private DroneService droneService;
	
	UUID serialNumber;
	
	DroneDto droneDto;
	List<DroneDto> expectedDrones;
	Map<String, Integer> expectedBatteryLevel;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();
		
		serialNumber = new UUID(20, 20);
		this.expectedDrones = new ArrayList<>();
		this.expectedBatteryLevel = new HashMap<String, Integer>();
		this.droneDto = new DroneDto();
		
		DroneDto droneDto = new DroneDto();
		droneDto.setSerialNumber(serialNumber);
		
		this.expectedDrones.add(droneDto);
		
		this.expectedBatteryLevel.put("batteryCapacity", 75);
		
		this.droneDto.setBatteryCapacity(20);
		this.droneDto.setModel(Model.Cruiserweight);
		this.droneDto.setState(State.RETURNING);
		this.droneDto.setWeightLimit(100);
	}

	@Test
	public void itShouldReturnAListOfDronesFromService() throws Exception {
		when(droneService.findDrones(null)).thenReturn(expectedDrones);
		
		MvcResult result = mockMvc.perform(get("/drones"))
				.andDo(print())
				.andReturn();
		String actualDrones = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		assertEquals(200, status);
		assertThat(actualDrones).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedDrones));
	}

	@Test
	public void itShouldReturnDroneBatteryCapacityFromService() throws Exception {
		when(droneService.getDroneBatteryCapacity(serialNumber)).thenReturn(expectedBatteryLevel);
		
		MvcResult result = mockMvc.perform(get("/drones/" + serialNumber))
				.andDo(print())
				.andReturn();
		String actualBatteryLevel = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		assertEquals(200, status);
		assertThat(actualBatteryLevel).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedBatteryLevel));
	}

	@Test
	public void itShouldReturn404ErrorForDroneBatteryCapacityFromService() throws Exception {
		when(droneService.getDroneBatteryCapacity(serialNumber)).thenReturn(null);
		
		MvcResult result = mockMvc.perform(get("/drones/" + serialNumber))
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(404, status);
	}

	@Test
	public void itShouldRegisterADrone() throws Exception {
		DroneDto expectedDroneDto = droneDto;
		
		when(droneService.registerDrone(any(DroneDto.class))).thenReturn(expectedDroneDto);
		
		MvcResult result = mockMvc.perform(
					post("/drones")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(droneDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualDroneDto = result.getResponse().getContentAsString();
		
		assertEquals(201, status);
		assertThat(actualDroneDto).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedDroneDto));
	}

	@Test
	public void itShouldFailToRegisterADroneIfWeightLimitValidationFails() throws Exception {
		droneDto.setWeightLimit(501);
		when(droneService.registerDrone(any(DroneDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/drones")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(droneDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(400, status);
	}

	@Test
	public void itShouldFailToRegisterADroneIfBatteryValidationFails() throws Exception {
		droneDto.setBatteryCapacity(101);
		when(droneService.registerDrone(any(DroneDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/drones")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(droneDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(400, status);
	}

	@Test
	public void itShouldFailToRegisterADroneIfServerFails() throws Exception {
		when(droneService.registerDrone(any(DroneDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/drones")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(droneDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(500, status);
	}

}
