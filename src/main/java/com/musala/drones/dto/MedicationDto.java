package com.musala.drones.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class MedicationDto {
	@NotBlank(message = "Code value cannot be empty")
	@Pattern(regexp = "^[A-Z0-9_]*$")
	private String code;
	
	@NotBlank(message = "Name value cannot be empty")
	@Pattern(regexp = "^[a-zA-Z0-9_-]*$")
	private String name;
	
	@Min(1)
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
