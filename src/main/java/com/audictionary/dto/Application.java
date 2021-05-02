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


	private int passStatus;
	
	private int extra__ap_id;
	private String extra__ap_name;
	private String extra__aw_title;
	private String extra__ar_name;
}
