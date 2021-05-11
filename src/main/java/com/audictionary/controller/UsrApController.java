package com.audictionary.controller;

import java.util.HashMap;
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
import com.audictionary.dto.api.KapiKakaoCom__v2_user_me__ResponseBody;
import com.audictionary.service.ApService;
import com.audictionary.service.AttrService;
import com.audictionary.service.EmailService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.KakaoService;
import com.audictionary.service.RecruitService;

@Controller
public class UsrApController {
	@Autowired
	private ApService apService;
	
	@Autowired
	private GenFileService genFileService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private AttrService attrService;
	
	@Autowired
	private KakaoService kakaoService;

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

		boolean NeedToModify = apService.isNeedToModify(ap, param);
		
		param.put("isNeedToModify", NeedToModify);
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
	public ResultData showListForAdmPage(@RequestParam Map<String,Object> param) {
		
		Map<String, List<Ap>> apList = apService.getApListGroupByPassStatusByRecruitId(param);
		
		List<Ap> apListAll = apList.get("apListAll");
		List<Ap> apList1Pass = apList.get("apList1Pass");
		List<Ap> apList2Pass = apList.get("apList2Pass");
		List<Ap> apList3Pass = apList.get("apList3Pass");
		
		List<Ap> apListLike = apService.getListByLikedApplication(param);
		
		return new ResultData("S-1", "지원자선정", "apListAll", apListAll, "apListLike", apListLike, "apList1Pass", apList1Pass, "apList2Pass", apList2Pass, "apList3Pass", apList3Pass);
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
	
	@PostMapping("/usr/ap/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String,Object> param) {

		String loginedMemberId = (String)param.get("memberId");
		int memberId = Integer.parseInt(loginedMemberId);
		
		Ap ap = apService.getApById(memberId);
		
		if( ap.getDelStatus() == 1 ) {
			return new ResultData("F-1", "이미 탈퇴한 회원입니다.");
		}
		
		apService.doDeleteMemberById(memberId);
		
		return new ResultData("S-1", "회원탈퇴성공");
	}
	
	@GetMapping("/usr/ap/findLoginId")
	@ResponseBody
	public ResultData findLoginId(@RequestParam Map<String,Object> param) {
		
		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		
		Ap ap = apService.findLoginId(param);
		
		if ( ap == null ) {
			return new ResultData("F-2", "입력된 정보로 가입된 회원이 존재하지 않습니다.");
		}
		
		return new ResultData("S-1", "아이디 찾기 성공", "loginId", ap.getLoginId());
	}
	
	@PostMapping("/usr/ap/findLoginPw")
	@ResponseBody
	public ResultData findLoginPw(@RequestParam Map<String,Object> param) {
		if (param.get("loginId") == null) {
			return new ResultData("F-1", "이메일(아이디)을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		String email = (String)param.get("loginId");
		Ap ap = apService.getApByLoginId(email);
		
		if ( ap == null ) {
			return new ResultData("F-2" , "일치하는 회원이 존재하지 않습니다.");
		}
		if( !ap.getRegNumber().equals(param.get("regNumber"))){
			return new ResultData("F-2" , "주민등록번호가 일치하지 않습니다.");
		}
		int id = ap.getId();
		try {
			emailService.sendMailForFindLoginPwByAp(email,id);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return new ResultData("S-1", "임시 비밀번호 발송", "ap", ap);
	}
	
	@PostMapping("usr/ap/modifyPw")
	@ResponseBody
	public ResultData doModifyPw(@RequestParam Map<String,Object> param) {
		Ap ap = apService.getApByLoginId((String)param.get("loginId"));
		
		int id = ap.getId();
		
		String key = attrService.getValue("ap", id, "emailCertKey", ap.getLoginId());
		
		if( key == null ) {
			return new ResultData("F-1","비정상적인 접근입니다.", "key", key);
		}
		
		if( !key.equals(param.get("key"))) {
			return new ResultData("F-1","비정상적인 접근입니다.", "key", key);
		}
		
		apService.doModifyPw(param);
		attrService.remove("ap", id, "emailCertKey", ap.getLoginId());
		
		return new ResultData("S-1", "비밀번호 변경 완료");
	}
	
	@RequestMapping("/usr/ap/kakaoLogin")
	@ResponseBody
	public ResultData kakaoLogin(@RequestParam String code) {

		String token = kakaoService.getAccessTokenForKakaoLoginAp(code);

		KapiKakaoCom__v2_user_me__ResponseBody kakaoUser = kakaoService.getPdByKakaoAccessToken(token);

		Ap ap = apService.getMemberByOnLoginProviderMemberId("kakao", kakaoUser.getId());

		if (ap != null) {
			ap.getExtraNotNull().put("existsMember", true);
			ap.getExtraNotNull().put("thumbnail_image_url", kakaoUser.getKakao_account().profile.thumbnail_image_url);
			
			return new ResultData("S-1", "카카오로 로그인 되었습니다.", "authKey", ap.getAuthKey(), "member", ap);
		} else {
			apService.doJoinByKakao(kakaoUser);
			ap = apService.getMemberByOnLoginProviderMemberId("kakao", kakaoUser.getId());
			ap.getExtraNotNull().put("existsMember", false);
			
			return new ResultData("S-1", "카카오로 로그인 되었습니다.", "authKey", ap.getAuthKey(), "member", ap);
		}
	}
	
	@PostMapping("/usr/ap/doModifyForKakao")
	@ResponseBody
	public ResultData doModifyForKakao(@RequestParam Map<String, Object> param) {
		String loginedMemberId = (String)param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);
		
		Ap ap = apService.getApById(id);

		if ( ap != null ) {
			boolean isNeedToModify = apService.isNeedToModify(ap, param);
			
			param.put("isNeedToModify", isNeedToModify);
			param.put("id", loginedMemberId);
			
			apService.doModifyForKakao(param);
			
			ap = apService.getApById(id);
		}
		
		
		return new ResultData("S-1", "회원정보수정","authKey", ap.getAuthKey(), "ap", ap);
	}
	
	
}
