package com.audictionary.controller;

import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Ap;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ApService;

@Controller
public class UsrApController {
	@Autowired
	private ApService apService;

	@PostMapping("/usr/ap/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) throws MessagingException {
		
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

		if (param.get("cellPhoneNo") == null) {
			return new ResultData("F-1", "전화번로를 입력해 주세요.");
		}
		
		return apService.doJoin(param);
	}
	
	@PostMapping("/usr/ap/getAuthKey")
	@ResponseBody
	public ResultData getAuthKey(String loginId, String loginPw) {
		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Ap existingAp = apService.getApByLoginId(loginId);

		if (existingAp == null) {
			return new ResultData("F-2", "존재하지 않는 로그인아이디 입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingAp.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}
		
		System.out.println("출력용 existingAp.getName() : " + existingAp.getName() + "authKey : " + existingAp.getAuthKey() + "member : " + existingAp);

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingAp.getNickName()), "authKey", existingAp.getAuthKey(), "member", existingAp);
	}
	
	@PostMapping("/usr/ap/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param, String loginedMemberId) {
		
		if ( param.isEmpty() ) {
			return new ResultData("F-1", "수정할 정보를 입력해주세요.");
		}
		param.put("id", loginedMemberId);
		
		return apService.doModify(param);
	}
	
	@GetMapping("/usr/ap/doIdDupCheck")
	@ResponseBody
	public ResultData doIdDupCheck(@RequestParam String loginId) {
		
		Ap ap = apService.doIdDupCheck(loginId);
		
		if ( ap != null ) {
			return new ResultData("F-1", "이미 사용중인 아이디입니다.");
		} else {
			return new ResultData("S-1", "사용 가능한 아이디입니다.");
		}
	}
	
	@RequestMapping("/usr/ap/emailCertForJoin")
	@ResponseBody
	public ResultData doEmailCert(@RequestParam Map<String,Object> param) throws MessagingException {
		Ap ap = apService.getApByAuthKey((String)param.get("authKey"));
		
		if ( ap == null ) {
			return new ResultData("F-1", "유효하지 않은 인증입니다.");
		} else {
			apService.setAuthStatusValid(ap.getAuthKey());
			return new ResultData("S-1", "인증성공", "isCert" , true);	
		}
		
	}
	
}
