package com.audictionary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ResultData;
import com.audictionary.service.RecruitService;

@Controller
public class UsrRecruitController {
	@Autowired
	private RecruitService recruitService;
	
	@RequestMapping("/usr/recruit/write")
	@ResponseBody
	public ResultData doWrite(@RequestParam Map<String,Object> param) {
		return recruitService.doWrite(param);
	}
}
