package com.audictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Artwork extends EntityDto {
	
	private int id;
	private String regDate;
	private String updateDate;
	private int recruitmentId;
	private String name;
	private String genre;
	private String corp;
	private String director;
	private String etc;

}
