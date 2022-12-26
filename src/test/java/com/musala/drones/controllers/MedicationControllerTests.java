package com.musala.drones.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.drones.dto.MedicationDto;
import com.musala.drones.services.MedicationService;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MedicationController.class)
class MedicationControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@MockBean
	private MedicationService medicationService;
	
	UUID serialNumber;
	
	MedicationDto medicationDto;
	List<MedicationDto> expectedMedications;
	Map<String, String> expectedUploadedImage;
	List<String> medicationCodes;
	Map<String, HttpStatus> medicationLoadResponse;

	@BeforeEach
	void setUp() throws Exception {
		objectMapper = new ObjectMapper();
		
		serialNumber = new UUID(20, 20);
		this.expectedMedications = new ArrayList<>();
		this.expectedUploadedImage = new HashMap<String, String>();
		this.medicationDto = new MedicationDto();
		this.medicationCodes = new ArrayList<>();
		this.medicationLoadResponse = new HashMap<String, HttpStatus>();
		
		MedicationDto medicationDto = new MedicationDto();
		
		this.expectedMedications.add(medicationDto);
		
		this.expectedUploadedImage.put("imageUrl", "url");
		
		this.medicationDto.setCode("ABC_4");
		this.medicationDto.setName("ABC_a-1");
		this.medicationDto.setWeight(100);
		
		this.medicationCodes.add("ABC_4");
	}

	@Test
	public void itShouldUploadAMedicationCaseImage() throws Exception {
		when(medicationService.uploadImage(any())).thenReturn(expectedUploadedImage);
		
		MvcResult result = mockMvc.perform(
					post("/medications/upload-image")
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.content(objectMapper.writeValueAsString(expectedUploadedImage))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualUploadedImage = result.getResponse().getContentAsString();
		
		assertEquals(200, status);
		assertThat(actualUploadedImage).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedUploadedImage));
	}

	@Test
	public void itShouldFailToUploadAMedicationCaseImage() throws Exception {
		when(medicationService.uploadImage(any())).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/medications/upload-image")
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.content(objectMapper.writeValueAsString(expectedUploadedImage))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(500, status);
	}

	@Test
	public void itShouldSaveAMedication() throws Exception {
		MedicationDto expectedMedicationDto = medicationDto;
		
		when(medicationService.saveMedication(any(MedicationDto.class))).thenReturn(expectedMedicationDto);
		
		MvcResult result = mockMvc.perform(
					post("/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualMedicationDto = result.getResponse().getContentAsString();
		
		assertEquals(201, status);
		assertThat(actualMedicationDto).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedMedicationDto));
	}

	@Test
	public void itShouldFailToSaveAMedicationIfCodeValidationFails() throws Exception {
		medicationDto.setCode("abc=");
		when(medicationService.saveMedication(any(MedicationDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(400, status);
	}

	@Test
	public void itShouldFailToSaveAMedicationIfNameValidationFails() throws Exception {
		medicationDto.setName("abc=");
		when(medicationService.saveMedication(any(MedicationDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(400, status);
	}

	@Test
	public void itShouldFailToSaveAMedicationIfServerFails() throws Exception {
		when(medicationService.saveMedication(any(MedicationDto.class))).thenReturn(null);
		
		MvcResult result = mockMvc.perform(
					post("/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationDto))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		
		assertEquals(500, status);
	}

	@Test
	public void itShouldReturnAListOfMedicationsFromService() throws Exception {
		when(medicationService.findMedications()).thenReturn(expectedMedications);
		
		MvcResult result = mockMvc.perform(get("/medications"))
				.andDo(print())
				.andReturn();
		String actualMedications = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		assertEquals(200, status);
		assertThat(actualMedications).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedMedications));
	}

	@Test
	public void itShouldLoadMedicationsOnADrone() throws Exception {
		String expectedMessage = "1 out of 1 medications loaded on";
		medicationLoadResponse.put(expectedMessage, HttpStatus.OK);
		
		Map<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("response", expectedMessage);
		
		when(medicationService.loadMedicationsOnDrone(serialNumber, medicationCodes)).thenReturn(medicationLoadResponse);
		
		MvcResult result = mockMvc.perform(
					post("/drones/" + serialNumber + "/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationCodes))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualResponse = result.getResponse().getContentAsString();
		
		assertEquals(200, status);
		assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
	}

	@Test
	public void itShouldFailToLoadMedicationsIfDroneNotFound() throws Exception {
		String expectedMessage = "Drone not found";
		medicationLoadResponse.put(expectedMessage, HttpStatus.NOT_FOUND);
		
		Map<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("response", expectedMessage);
		
		when(medicationService.loadMedicationsOnDrone(serialNumber, medicationCodes)).thenReturn(medicationLoadResponse);
		
		MvcResult result = mockMvc.perform(
					post("/drones/" + serialNumber + "/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationCodes))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualResponse = result.getResponse().getContentAsString();
		
		assertEquals(404, status);
		assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
	}

	@Test
	public void itShouldFailToLoadMedicationsIfDroneBatteryBelow25Percent() throws Exception {
		String expectedMessage = "Drone battery level is too low";
		medicationLoadResponse.put(expectedMessage, HttpStatus.FORBIDDEN);
		
		Map<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("response", expectedMessage);
		
		when(medicationService.loadMedicationsOnDrone(serialNumber, medicationCodes)).thenReturn(medicationLoadResponse);
		
		MvcResult result = mockMvc.perform(
					post("/drones/" + serialNumber + "/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationCodes))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualResponse = result.getResponse().getContentAsString();
		
		assertEquals(403, status);
		assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
	}

	@Test
	public void itShouldFailToLoadMedicationsIfServerFails() throws Exception {
		String expectedMessage = "Something unexpected happened";
		medicationLoadResponse.put(expectedMessage, HttpStatus.INTERNAL_SERVER_ERROR);
		
		Map<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("response", expectedMessage);
		
		when(medicationService.loadMedicationsOnDrone(serialNumber, medicationCodes)).thenReturn(medicationLoadResponse);
		
		MvcResult result = mockMvc.perform(
					post("/drones/" + serialNumber + "/medications")
						.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
						.content(objectMapper.writeValueAsString(medicationCodes))
				)
				.andDo(print())
				.andReturn();
		int status = result.getResponse().getStatus();
		String actualResponse = result.getResponse().getContentAsString();
		
		assertEquals(500, status);
		assertThat(actualResponse).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedResponse));
	}

	@Test
	public void itShouldListMedicationsLoadedOnADrone() throws Exception {
		when(medicationService.findMedicationsOnDrone(serialNumber)).thenReturn(expectedMedications);
		
		MvcResult result = mockMvc.perform(get("/drones/" + serialNumber + "/medications"))
				.andDo(print())
				.andReturn();
		String actualMedications = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		assertEquals(200, status);
		assertThat(actualMedications).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedMedications));
	}

}
