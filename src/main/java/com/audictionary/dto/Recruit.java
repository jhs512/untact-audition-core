package com.audictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Recruit extends EntityDto {
	
	private int id;
	private String regDate;
	private String updateDate;
	private String delDate;
	private int delStatus;
	private int memberId;
	private int boardId;
	private String title;
	private String body;
	private String roleType;
	private String location;
	private String period;
	private String deadline;
	private String manager;
}
