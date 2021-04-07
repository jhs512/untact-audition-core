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
	private String relTypeCode;
	private int relId;
	private String title;
	private String subtitle;
	private String media;
	private String genre;
	private String corp;
	private String director;
	private String producer;
	private String castingManager;
	private String writer;
	private String story;
	private String image;
	private String link;
	private String etc;

}
