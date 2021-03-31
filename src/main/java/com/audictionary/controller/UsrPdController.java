package com.audictionary.controller;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Attr;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Pd;
import com.audictionary.dto.ResultData;
import com.audictionary.service.AttrService;
import com.audictionary.service.EmailService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.PdService;
import com.audictionary.util.Util;

@Controller
public class UsrPdController {
	@Autowired
	private PdService pdService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private AttrService attrService;

	@PostMapping("usr/pd/doJoin")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {

		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해 주세요.");
		}

		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
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

		if (param.get("jobPosition") == null) {
			return new ResultData("F-1", "직급을 입력해 주세요.");
		}

		pdService.doJoin(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "회원가입성공", "id", id);
	}
	
	@RequestMapping("/usr/pd/emailDupCheck")
	@ResponseBody
	public ResultData doEmailDupCheck(@RequestParam String email) throws MessagingException {
		
		Pd pd = pdService.getMemberByEmail(email);
		
		if( pd == null ) {
			return new ResultData("S-1" , "메일 발송");	
		}
		else {
			return new ResultData("F-1" , "이미 가입된 이메일입니다.");
		}
		
	}
	
	@RequestMapping("/usr/pd/sendEmail")
	@ResponseBody
	public ResultData sendEmail(@RequestParam String email) throws MessagingException {
		
		emailService.sendMailForCert(email);
		return new ResultData("S-1" , "메일 발송");
		
	}
	
	@RequestMapping("/usr/pd/emailCert")
	@ResponseBody
	public ResultData doEmailCert(@RequestParam Map<String,Object> param) throws MessagingException {
		String emailCertkey = attrService.getValue("pd", 0, "emailCertKey", (String)param.get("email"));
		
		if (!param.get("key").equals(emailCertkey)) {
			return new ResultData("F-1", "인증실패");
		}else {
			attrService.remove("pd", 0, "emailCertKey", (String)param.get("email"));
			return new ResultData("S-1", "인증성공", "isCert" , true);	
		}
		
	}
	
	@RequestMapping("/usr/pd/isEmailCert")
	@ResponseBody
	public ResultData checkEmailCert(@RequestParam String email) throws MessagingException {
		
		Attr attr = attrService.get("pd", 0, "emailCertKey", email);
		
		if(attr == null) {
			return new ResultData("S-1", "인증된 이메일입니다." , "emailCert", true);
		}else {
			return new ResultData("F-1", "인증되지 않은 이메일입니다." , "emailCert", false);
		}
		
	}

	@PostMapping("/usr/pd/doLogin")
	@ResponseBody
	public ResultData doLogin(@RequestParam Map<String, Object> param) {

		if (param.get("email") == null) {
			return new ResultData("F-1", "이메일을 입력해 주세요.");
		}
		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "비밀번호를 입력해 주세요.");
		}

		Pd pd = pdService.getMemberByEmail((String)param.get("email"));

		if (pd == null) {
			return new ResultData("F-2", "일치하는 회원이 없습니다.");
		}

		if (!pd.getLoginPw().equals(param.get("loginPw"))) {
			return new ResultData("F-2", "비밀번호가 맞지 않습니다.");
		}

		if ( pd.getDelStatus() == 1 ) {
			return new ResultData("F-3","탈퇴한 회원입니다.");
		}
		
		pd = pdService.updateForPrint(pd);
		
		return new ResultData("S-1", "로그인 성공", "authKey", pd.getAuthKey(), "pd", pd);
	}

	@PostMapping("/usr/pd/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {
		String loginedMemberId = (String)param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);
		
		if ( param.get("isFileUploaded").equals("true") ) {
			genFileService.deleteGenFiles("pd", id);
		}
		
		Pd pd = pdService.getMemberById(id);

		boolean isNeedToModify = false;

		if (!pd.getName().equals(param.get("name")) || !pd.getAddress().equals(param.get("address"))
				|| !pd.getLoginPw().equals(param.get("loginPw"))
				|| !pd.getCellPhoneNo().equals(param.get("cellPhoneNo"))
				|| !pd.getJobPosition().equals(param.get("jobPosition"))
				|| !pd.getCorpName().equals(param.get("corpName"))) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		param.put("id", loginedMemberId);
		
		pdService.doModify(param);
		
		pd = pdService.getMemberById(id);
		
		return new ResultData("S-1", "회원정보수정","authKey", pd.getAuthKey(), "pd", pd);
	}
	
	@PostMapping("/usr/pd/update")
	@ResponseBody
	public ResultData doUpdatePd(@RequestParam String loginedMemberId) {
		int id = Integer.parseInt(loginedMemberId);
		Pd pd = pdService.getMemberById(id);
		
		return new ResultData("S-1", "갱신", "pd", pd);
	}
	
	@PostMapping("/usr/pd/doFindLoginId")
	@ResponseBody
	public ResultData doFindLoginId(@RequestParam Map<String,Object> param) {
		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		
		Pd pd = pdService.doFindLoginId(param);
		
		if ( pd == null ) {
			return new ResultData("F-2" , "일치하는 회원이 없습니다.");
		}
		
		return new ResultData("S-1", "로그인 아이디 찾기 성공", "pd", pd, "loginId", pd.getLoginId());
	}
	
	@PostMapping("/usr/pd/doFindLoginPw")
	@ResponseBody
	public ResultData doFindLoginPw(@RequestParam Map<String,Object> param) {
		if (param.get("email") == null) {
			return new ResultData("F-1", "이메일(아이디)을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		String email = (String)param.get("email");
		Pd pd = pdService.getMemberByEmail(email);
		
		if ( pd == null ) {
			return new ResultData("F-2" , "일치하는 회원이 없습니다.");
		}
		
		String tempPw = Util.getTempPassword(6);
		String tempRealPw = (String)Util.sha256(tempPw);
		pdService.setTempPw(email,tempRealPw);
		
		return new ResultData("S-1", "임시 비밀번호 발송", "pd", pd, "loginPw", tempPw);
	}
	
	@PostMapping("/usr/pd/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam int loginedMemberId) {
		
		Pd pd = pdService.getMemberById(loginedMemberId);
		
		if( pd.getDelStatus() == 1 ) {
			return new ResultData("F-1", "이미 탈퇴한 회원입니다.");
		}
		
		pdService.doDeleteMemberById(loginedMemberId);
		
		return new ResultData("S-1", "회원탈퇴성공");
	}
	
}
