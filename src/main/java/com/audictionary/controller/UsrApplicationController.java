package com.audictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Application;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ApplicationService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.RecruitService;

@Controller
public class UsrApplicationController {
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private ApplicationService applicationService;
	
	@RequestMapping("/usr/application/list")
	@ResponseBody
	public ResultData showList(@RequestParam Map<String,Object> param) {
		
		List<Application> applications = applicationService.getListForPrint(param);
		
		return new ResultData("S-1", "지원 목록 불러오기 성공", "applications", applications);
	}
	
}
