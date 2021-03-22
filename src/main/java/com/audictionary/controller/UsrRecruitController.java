package com.audictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Recruit;
import com.audictionary.dto.ResultData;
import com.audictionary.service.GenFileService;
import com.audictionary.service.RecruitService;
import com.audictionary.util.Util;

@Controller
public class UsrRecruitController {
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private GenFileService genFileService;
	
	@RequestMapping("/usr/recruit/write")
	@ResponseBody
	public ResultData doWrite(@RequestParam Map<String,Object> param) {
		
		recruitService.doWrite(param);
		
		int id = Util.getAsInt(param.get("id"),0);
		
		genFileService.changeInputFileRelIds(param, id);
		
		return new ResultData("S-1", "공고 등록 성공", "id", id);
	}
	
	@PostMapping("/usr/recruit/list")
	@ResponseBody
	public ResultData showList() {
		
		List<Recruit> recruits = recruitService.getListForPrint();
		
		return new ResultData("S-1", "공고 리스트 출력", "recruits" , recruits);
	}
}
