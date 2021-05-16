package com.audictionary.controller;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Application;
import com.audictionary.dto.Artwork;
import com.audictionary.dto.Pd;
import com.audictionary.dto.ResultData;
import com.audictionary.dto.api.Aligo__send__ResponseBody;
import com.audictionary.dto.api.KapiKakaoCom__v2_user_me__ResponseBody;
import com.audictionary.service.ApService;
import com.audictionary.service.ArtworkService;
import com.audictionary.service.AttrService;
import com.audictionary.service.EmailService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.KakaoService;
import com.audictionary.service.PdService;
import com.audictionary.service.SmsService;
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
	@Autowired
	private ArtworkService artworkService;
	@Autowired
	private ApService apService;
	@Autowired
	private KakaoService kakaoService;
	@Autowired
	private SmsService smsService;

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
		String isEmailCert = attrService.getValue("pd", 0, "isEmailCert", (String) param.get("email"));
		if (isEmailCert == null) {
			return new ResultData("F-1", "이메일 인증을 진행해주세요.");
		}

		pdService.doJoin(param);

		int id = Util.getAsInt(param.get("id"), 0);

		return new ResultData("S-1", "회원 가입 되었습니다!", "id", id);
	}

	/* 이메일 중복 체크 */
	@RequestMapping("/usr/pd/emailDupCheck")
	@ResponseBody
	public ResultData doEmailDupCheck(@RequestParam String email) throws MessagingException {

		Pd pd = pdService.getMemberByEmail(email);

		if (pd == null) {
			return new ResultData("S-1", "가입 가능한 이메일입니다.");
		} else {
			return new ResultData("F-1", "이미 가입된 이메일입니다.");
		}

	}

	/* 이메일 인증 메일 발송 */
	@RequestMapping("/usr/pd/sendEmailForJoin")
	@ResponseBody
	public ResultData sendEmailForJoin(@RequestParam String email) throws MessagingException {

		ResultData rs = emailService.sendMailForCert(email);
		return rs;

	}

	/* 이메일 인증 처리 */
	@RequestMapping("/usr/pd/emailCertForJoin")
	@ResponseBody
	public ResultData doEmailCert(@RequestParam Map<String, Object> param) throws MessagingException {
		String emailCertkey = attrService.getValue("pd", 0, "emailCertKey", (String) param.get("email"));

		/*
		 * attr 테이블에서 해당 이메일과 키로 된 데이터가 있는지 확인 이메일과 키가 일치하면 인증처리 키를 제거하고 체크가 완료되었다는 데이터
		 * 저장
		 */

		if (!param.get("key").equals(emailCertkey)) {
			return new ResultData("F-1", "이메일 인증에 실패했습니다.");
		} else {
			attrService.remove("pd", 0, "emailCertKey", (String) param.get("email"));
			attrService.setValue("pd", 0, "isEmailCert", (String) param.get("email"), "emailCertTrue", null);
			return new ResultData("S-1", "이메일이 인증되었습니다.", "isCert", true);
		}

	}

	/* 이메일 인증 여부 체크 */
	@RequestMapping("/usr/pd/isEmailCert")
	@ResponseBody
	public ResultData checkEmailCert(@RequestParam String email) throws MessagingException {
		/*
		 * attr 테이블에서 인증처리 된 데이터가 있는지 확인 데이터가 있으면 인증 확인
		 */
		String isEmailCert = attrService.getValue("pd", 0, "isEmailCert", email);

		if (isEmailCert == null) {
			return new ResultData("F-1", "인증되지 않은 이메일입니다.");
		}

		if (isEmailCert.equals("emailCertTrue")) {
			attrService.remove("pd", 0, "isEmailCert", email);
			return new ResultData("S-1", "인증된 이메일입니다.", "emailCert", true);
		} else {
			return new ResultData("F-1", "인증되지 않은 이메일입니다.", "emailCert", false);
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

		Pd pd = pdService.getMemberByEmail((String) param.get("email"));

		if (pd == null) {
			return new ResultData("F-2", "일치하는 회원이 없습니다.");
		}

		if (!pd.getLoginPw().equals(param.get("loginPw"))) {
			return new ResultData("F-2", "비밀번호가 맞지 않습니다.");
		}

		if (pd.getDelStatus() == 1) {
			return new ResultData("F-3", "탈퇴한 회원입니다.");
		}

		pd = pdService.updateForPrint(pd);

		return new ResultData("S-1", "로그인 되었습니다.", "authKey", pd.getAuthKey(), "pd", pd);
	}

	@GetMapping("/usr/pd/doLogout")
	@ResponseBody
	public ResultData doLogout(HttpServletRequest request) {

		if (request.getAttribute("loginedMemberId") != null) {
			request.removeAttribute("isLogined");
			request.removeAttribute("loginedMemberId");
		}

		return new ResultData("S-1", "로그아웃 성공");
	}

	@GetMapping("/usr/sms/doSendSms")
	@ResponseBody
	public ResultData doSendSms(@RequestParam Map<String, Object> param) {

		Aligo__send__ResponseBody rb = smsService.sendSms(param);

		String from = (String) param.get("from");
		String to = (String) param.get("to");
		String msg = (String) param.get("msg");

		return new ResultData("S-1", "SMS문자가 발송되었습니다.", "from", from, "to", to, "msg", msg, "rb", rb);
	}

	@PostMapping("/usr/pd/doModify")
	@ResponseBody
	public ResultData doModify(@RequestParam Map<String, Object> param) {

		String loginedMemberId = (String) param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);

		if (param.get("isFileUploaded").equals("true")) {
			genFileService.deleteGenFiles("pd", id);
		}

		Pd pd = pdService.getMemberById(id);

		boolean isNeedToModify = pdService.isNeedToModify(pd, param);

		param.put("isNeedToModify", isNeedToModify);
		param.put("id", loginedMemberId);

		pdService.doModify(param);

		pd = pdService.getMemberById(id);

		/*
		 * 회원과 관련된 작품 데이터가 있는지 확인 관련 작품 데이터가 있으면 제거 후 새로 업로드된 작품 데이터를 저장
		 */

		String artworksParam = (String) param.get("artwork");

		List<String> artworks = Util.getObjectFromJsonString(artworksParam, List.class);
		if(artworks == null || artworks.size() == 0 ) {
			artworkService.deleteByPdId(Integer.parseInt((String) param.get("loginedMemberId")));
		}

		if (artworks != null && artworks.size() > 0) {
			artworkService.deleteByPdId(Integer.parseInt((String) param.get("loginedMemberId")));
			artworkService.doWriteArtWorkForPdProfile(param);
		}

		return new ResultData("S-1", "회원 정보가 수정되었습니다.", "authKey", pd.getAuthKey(), "pd", pd);
	}

	/* 회원 정보 수정 시 파일 업로드 되었으면 업로드 후 관련 파일 데이터를 받아서 갱신 후 보내줌 */
	@PostMapping("/usr/pd/update")
	@ResponseBody
	public ResultData doUpdatePd(@RequestParam Map<String, Object> param) {

		String loginedMemberId = (String) param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);

		Pd pd = pdService.getMemberById(id);

		return new ResultData("S-1", "회원 정보가 갱신되었습니다.", "pd", pd);
	}

	@PostMapping("/usr/pd/doFindLoginId")
	@ResponseBody
	public ResultData doFindLoginId(@RequestParam Map<String, Object> param) {
		if (param.get("name") == null) {
			return new ResultData("F-1", "이름을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}

		Pd pd = pdService.doFindLoginId(param);

		if (pd == null) {
			return new ResultData("F-2", "일치하는 회원이 없습니다.");
		}

		return new ResultData("S-1", "회원님의 아이디는 "+ pd.getId() +" 입니다.", "pd", pd, "loginId", pd.getLoginId());
	}

	@PostMapping("/usr/pd/doFindLoginPw")
	@ResponseBody
	public ResultData doFindLoginPw(@RequestParam Map<String, Object> param) throws MessagingException {
		if (param.get("email") == null) {
			return new ResultData("F-1", "이메일(아이디)을 입력해 주세요.");
		}
		if (param.get("regNumber") == null) {
			return new ResultData("F-1", "주민등록번호를 입력해 주세요.");
		}
		String email = (String) param.get("email");
		Pd pd = pdService.getMemberByEmail(email);

		if (pd == null) {
			return new ResultData("F-2", "일치하는 회원이 없습니다.");
		}
		if (!pd.getRegNumber().equals(param.get("regNumber"))) {
			return new ResultData("F-2", "주민등록번호가 일치하지 않습니다.");
		}
		int id = pd.getId();

		ResultData rs = emailService.sendMailForFindLoginPw(email, id);

		return rs;
	}

	@PostMapping("usr/pd/doModifyPw")
	@ResponseBody
	public ResultData doModifyPw(@RequestParam Map<String, Object> param) {
		Pd pd = pdService.getMemberByEmail((String) param.get("email"));

		int id = pd.getId();
		
		/*
		 * attr 테이블에서 회원의 인증키가 있는지 확인 후 없으면 비정상적인 접근
		 */

		String key = attrService.getValue("pd", id, "emailCertKey", pd.getEmail());

		if (key == null) {
			return new ResultData("F-1", "비정상적인 접근입니다.", "key", key);
		}

		if (!key.equals(param.get("key"))) {
			return new ResultData("F-1", "비정상적인 접근입니다.", "key", key);
		}

		/*
		 * attr 테이블에서 회원의 인증키가 있는지 확인 후 있으면 비밀번호 변경 후 해당 데이터 제거
		 */
		pdService.doModifyPw(param);
		attrService.remove("pd", id, "emailCertKey", pd.getEmail());

		return new ResultData("S-1", "비밀번호 재설정 성공");
	}

	@PostMapping("/usr/pd/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String, Object> param, HttpSession session) {

		String loginedMemberId = (String) param.get("loginedMemberId");
		int id = Integer.parseInt(loginedMemberId);

		Pd pd = pdService.getMemberById(id);

		if (pd.getDelStatus() == 1) {
			return new ResultData("F-1", "이미 탈퇴한 회원입니다.");
		}
		String loginProviderTypeCode = (String) param.get("loginedMemberType");
		param.put("loginProviderTypeCode", loginProviderTypeCode);
		param.put("id", id);
		pdService.doDeleteMemberById(param);

		if (session.getAttribute("loginedMemberId") != null) {
			session.removeAttribute("isLogined");
			session.removeAttribute("loginedMemberId");
		}

		return new ResultData("S-1", "회원 탈퇴 되었습니다.");
	}

	/* 마이페이지 PROFILE 정보를 가져오기 위함 */
	@GetMapping("/usr/pd/getArtwork")
	@ResponseBody
	public ResultData getArtworks(@RequestParam int loginedMemberId) {

		List<Artwork> artworks = artworkService.getArtworksByPdId(loginedMemberId);

		if (artworks == null || artworks.size() == 0) {
			return new ResultData("F-1", "작품이 없습니다.");
		}

		return new ResultData("S-1", "작품 불러오기 성공", "artworks", artworks);
	}

	/* 마이페이지 PROFILE 정보에서 제거 */
	@GetMapping("/usr/pd/deleteArtwork")
	@ResponseBody
	public ResultData doDeletePdArtwork(@RequestParam int id) {

		artworkService.deleteById(id);

		return new ResultData("S-1", "작품 제거 성공");
	}

	/* 마이페이지 정보를 가져오기 위함 */
	@GetMapping("/usr/pd/showDetail")
	@ResponseBody
	public ResultData showDetail(@RequestParam Map<String, Object> param) {

		String loginedMemberId = (String) param.get("id");
		int id = Integer.parseInt(loginedMemberId);

		Pd pd = pdService.getMemberById(id);

		List<Application> aps = apService.getListByLikedAp(param);

		if (pd != null) {
			return new ResultData("S-1", "회원 정보 불러오기 성공", "pd", pd, "aps", aps);
		} else {
			return new ResultData("F-1", "일치하는 회원이 없습니다.");
		}
	}

	/* 마이페이지 프로필 사진 제거 */
	@RequestMapping("/usr/pd/deleteProfileImg")
	@ResponseBody
	public ResultData deleteProfileImg(@RequestParam int id) {

		genFileService.deleteGenFiles("pd", id);

		return new ResultData("S-1", "프로필 이미지 삭제 성공");
	}

	@RequestMapping("/usr/pd/kakaoLogin")
	@ResponseBody
	public ResultData kakaoLogin(@RequestParam String code) {

		String token = kakaoService.getAccessTokenForKakaoLogin(code);

		if (token == null || token.length() < 1) {
			return new ResultData("F-1", "카카오로 로그인하지 못했습니다.(토큰오류)");
		}

		KapiKakaoCom__v2_user_me__ResponseBody kakaoUser = kakaoService.getPdByKakaoAccessToken(token);

		if (kakaoUser == null || kakaoUser.getId() < 1) {
			return new ResultData("F-1", "카카오로 로그인하지 못했습니다.(유저를 불러올 수 없음)");
		}

		Pd pd = pdService.getMemberByOnLoginProviderMemberId("kakao", kakaoUser.getId());

		if (pd != null) {
			pdService.updateMember(pd, kakaoUser);
		} else {
			int newId = pdService.doJoinByKakao(kakaoUser);
			pd = pdService.getMemberById(newId);
		}

		if (kakaoUser.getKakao_account().profile.thumbnail_image_url != null) {
			pd.getExtraNotNull().put("thumbnail_image_url", kakaoUser.getKakao_account().profile.thumbnail_image_url);
		}

		return new ResultData("S-1", "카카오로 로그인 되었습니다.", "authKey", pd.getAuthKey(), "pd", pd);

	}

}
