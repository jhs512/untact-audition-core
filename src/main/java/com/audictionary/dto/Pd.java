package com.audictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pd {
	
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	private String loginPw;
	private String name;
	private String gender;
	private String regNumber;
	private String address;
	private String email;
	private String cellPhonoNe;
	private String jobPosition;
	private String corpName;
	private String corpType;
	private String authLevel;

}
