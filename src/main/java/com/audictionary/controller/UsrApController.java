package com.audictionary.controller;

import java.util.List;
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
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Pd;
import com.audictionary.dto.Recruit;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ApService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.RecruitService;

@Controller
public class UsrApController {
	@Autowired
	private ApService apService;
	
	@Autowired
	private GenFileService genFileService;

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
		
		GenFile genFile = genFileService.getGenFile("ap", existingAp.getId(), "common", "attachment", 1);
		
		if ( genFile != null ) {
			String imgUrl = genFile.getForPrintUrl();
			existingAp.setExtra__thumbImg(imgUrl);
		}

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingAp.getNickName()), "authKey", existingAp.getAuthKey(), "member", existingAp);
	}
	
	@PostMapping("/usr/ap/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {
		String loginedMemberId = (String)param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);
		
		Ap ap = apService.getApById(id);

		boolean isNeedToModify = false;

		if (!ap.getNickName().equals(param.get("nickName")) 
				|| ap.getFeet() != Integer.parseInt((String)param.get("feet"))
				|| ap.getWeight() != Integer.parseInt((String)param.get("weight"))
				|| !ap.getFeature().equals(param.get("feature"))
				|| !ap.getFilmgraphy().equals(param.get("filmgraphy"))
				|| !ap.getJobArea().equals(param.get("jobArea"))
				|| !ap.getCorp().equals(param.get("corp"))) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		param.put("id", loginedMemberId);
		
		apService.doModify(param);
		
		ap = apService.getApById(id);
		
		return new ResultData("S-1", "회원정보수정","authKey", ap.getAuthKey(), "ap", ap);
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
	
	
	
	
	@RequestMapping("/usr/ap/list")
	@ResponseBody
	public ResultData showList(@RequestParam Map<String,Object> param) {
		
		List<Ap> aps = apService.getApListByRecruitId(param);
		
		return new ResultData("S-1", "지원자선정", "aps", aps);
	}
	
	@GetMapping("/usr/ap/like")
	@ResponseBody
	public ResultData doLike(int recruitId, int memberId) {
		
		int existsLike = apService.isDupLike(recruitId, memberId);
		
		if ( existsLike != 0 ) {
			return new ResultData("F-1", "이미 좋아요 처리 된 공고입니다.");
		} else {
			String relTypeCode = "recruit";
			int relId = recruitId;
			String memberTypeCode = "ap";
			
			apService.doLike(relTypeCode, relId, memberTypeCode, memberId);
		  
			return new ResultData("S-1", "처리 되었습니다.");
		}
	}
	
	@GetMapping("/usr/ap/disLike")
	@ResponseBody
	public ResultData disLike(int recruitId, int memberId) {
		
		int existsLike = apService.isDupLike(recruitId, memberId);
		
		if ( existsLike != 0 ) {
			apService.deleteLike(recruitId, memberId);
			return new ResultData("S-1", "취소 하였습니다.");
		} else {
			return new ResultData("F-1", "이미 취소 처리된 공고입니다.");
		}
	}
	
	@GetMapping("/usr/ap/checkLikeStatus")
	@ResponseBody
	public ResultData checkLikeStatus(int recruitId, int memberId) {
		
		int existsLike = apService.isDupLike(recruitId, memberId);
		String likeStatus = "";
		
		if ( existsLike != 0 ) {
			likeStatus = "like";
			return new ResultData("S-1", "출력 성공", "likeStatus", likeStatus);
		} else if ( existsLike == 0 ){
			likeStatus = "disLike";
			return new ResultData("S-1", "출력 성공", "likeStatus", likeStatus);
		} else {
			return new ResultData("F-1", "해당사항이 없습니다.");
		}
	}
	
	@GetMapping("/usr/ap/recruitLikeList")
	@ResponseBody
	public ResultData recruitLikeList(int memberId) {
		
		List<Integer> likedRecruitIds = apService.likeListsByMemberId(memberId);
		
		if( likedRecruitIds.isEmpty() ) {
			return new ResultData("F-1", "좋아요 내역이 존재하지 않습니다.");
		}
		
		List<Recruit> likedRecruits = apService.getLikedRecruits(likedRecruitIds);
		
		return new ResultData("S-1", "불러오기 성공", "likedRecruits", likedRecruits);
	}
	
	
	
}
