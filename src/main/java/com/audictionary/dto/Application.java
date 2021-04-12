package com.audictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Application extends EntityDto {
	
	private int id;
	private String regDate;
	private String updateDate;
	private String delDate;
	private int delStatus;
	private int memberId;
	private int recruitId;
	private int exp;
}
