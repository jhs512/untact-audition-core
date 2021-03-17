package com.audictionary.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ap {
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	@JsonIgnore
	private String loginPw;
	private String name;
	private String nickName;
	private String gender;
	private String regNumber;
	private String address;
	private String email;
	private String cellPhonoNe;
	private int feet;
	private int weight;
    private String skinTone;
    private boolean eyelid;
    private String feature;
    private String filmgraphy;
    private String jobArea;
	private String corp;
	private int authLevel;
	@JsonIgnore
	private String authKey;
}
