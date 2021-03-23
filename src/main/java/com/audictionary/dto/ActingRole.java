package com.audictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActingRole extends EntityDto {
	private int id;
	private String regDate;
	private String updateDate;
	private int recruitmentId;
	private String realName;
	private String name;
	private String pay;
	private String age;
	private String job;
	private int scriptStatus;
	private String gender;
	private int scenesCount;
	private int shootingsCount;
	private String character;
	private String etc;
	}
