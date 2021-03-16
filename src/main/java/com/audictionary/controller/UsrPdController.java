package com.audictionary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ResultData;
import com.audictionary.service.PdService;

@Controller
public class UsrPdController {
	@Autowired
	private PdService pdService;
	
	@RequestMapping("usr/pd/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String,Object> param) {
		
		if ( param.get("loginId") == null ) {
			
		}
		
		int id = 0;
		
		return new ResultData("S-1", "회원가입성공", "id" , id );
	}
}
