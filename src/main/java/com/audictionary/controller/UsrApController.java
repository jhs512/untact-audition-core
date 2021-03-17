package com.audictionary.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Ap;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ApService;
import com.audictionary.util.Util;

@Controller
public class UsrApController {
	@Autowired
	private ApService ApService;

	@PostMapping("/usr/ap/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {
		
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "아이디를 입력해 주세요.");
		}

		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해 주세요.");
		}

		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
		}
		
		if (param.get("engName") == null) {
			return new ResultData("F-1", "영문이름을 입력해 주세요.");
		}
		
		if (param.get("gender") == null) {
			return new ResultData("F-1", "성별을 선택해 주세요.");
		}

		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}

		if (param.get("address") == null) {
			return new ResultData("F-1", "주소를 입력해 주세요.");
		}

		if (param.get("email") == null) {
			return new ResultData("F-1", "이메일을 입력해 주세요.");
		}

		if (param.get("cellPhoneNo") == null) {
			return new ResultData("F-1", "전화번로를 입력해 주세요.");
		}

		ApService.doJoin(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "회원가입성공", "id", id);
	}
	
	@PostMapping("/usr/ap/getAuthKey")
	@ResponseBody
	public ResultData getAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Ap existingAp = ApService.getApByLoginId(loginId);

		if (existingAp == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingAp.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingAp.getNickName()), "authKey", existingAp.getAuthKey(), "member", existingAp);
	}
}
