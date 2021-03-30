package com.audictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pd extends EntityDto{
	
	private int id;
	private String regDate;
	private String updateDate;
	private String delDate;
	private int delStatus;
	private String loginId;
	@JsonIgnore
	private String loginPw;
	private String name;
	private String gender;
	private String regNumber;
	private String address;
	private String email;
	private String cellPhoneNo;
	private String jobPosition;
	private String corpName;
	private String corpType;
	private String authLevel;
	private String extra__thumbImg;
	private String extra__fileType;
	@JsonIgnore
	private String authKey;

}
