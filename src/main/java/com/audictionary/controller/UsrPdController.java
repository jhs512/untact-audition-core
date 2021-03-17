package com.audictionary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ResultData;
import com.audictionary.service.PdService;
import com.audictionary.util.Util;

@Controller
public class UsrPdController {
	@Autowired
	private PdService pdService;
	
	@RequestMapping("usr/pd/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String,Object> param) {
	
		
		if ( param.get("loginPw") == null ) {
			return new ResultData("F-1", "비밀번호를 입력해 주세요.");
		}
		
		if ( param.get("name") == null ) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
		}
		
		if ( param.get("regNumber") == null ) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		
		if ( param.get("address") == null ) {
			return new ResultData("F-1", "주소를 입력해 주세요.");
		}
		
		if ( param.get("email") == null ) {
			return new ResultData("F-1", "이메일을 입력해 주세요.");
		}
		
		if ( param.get("cellPhoneNo") == null ) {
			return new ResultData("F-1", "전화번로를 입력해 주세요.");
		}
		
		if ( param.get("jobPosition") == null ) {
			return new ResultData("F-1", "직급을 입력해 주세요.");
		}
		
		pdService.doJoin(param);
		
		int id = Util.getAsInt(param.get("id") , 0);
		
		return new ResultData("S-1", "회원가입성공", "id" , id );
	}
}
