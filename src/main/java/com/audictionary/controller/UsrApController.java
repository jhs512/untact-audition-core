package com.audictionary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ResultData;
import com.audictionary.service.ApService;
import com.audictionary.service.RecruitService;

@Controller
public class UsrApController {
	@Autowired
	private ApService ApService;
	
	@RequestMapping("/usr/ap/doJoin")
	@ResponseBody
	public String doWrite(@RequestParam Map<String,Object> param) {
		ApService.doJoin(param);
		
		return "";
	}
}
