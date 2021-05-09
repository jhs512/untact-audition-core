package com.audictionary.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.audictionary.dto.api.Aligo__send__ResponseBody;
import com.audictionary.util.Util;

@Service
public class SmsService {
	
	public Aligo__send__ResponseBody sendSms(Map<String,Object> param) {
		String from = (String)param.get("from");
		String to = (String)param.get("to");
		String msg = (String)param.get("msg");
		Aligo__send__ResponseBody rb = Util.sendSms(from, to, msg);
		return rb;
	}
	
}
