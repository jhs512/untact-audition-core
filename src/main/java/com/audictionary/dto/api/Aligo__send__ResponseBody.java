package com.audictionary.dto.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aligo__send__ResponseBody {
	public String result_code; // 1=성공 or ...
	public String message; // EX : success
	public String msg_id; // EX : 195531394
	public int success_cnt; // EX : 1
	public int error_cnt; // EX : 0
	public String msg_type; // EX : SMS
}