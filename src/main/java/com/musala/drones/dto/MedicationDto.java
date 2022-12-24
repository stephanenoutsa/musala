package com.musala.drones.dto;

import javax.validation.constraints.NotBlank;

public class MedicationDto {
	@NotBlank(message = "Code value cannot be empty")
	private String code;
	
	@NotBlank(message = "Name value cannot be empty")
	private String name;
	
	@NotBlank(message = "Weight value cannot be empty")
	private Integer weight;
	
	private String image;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
