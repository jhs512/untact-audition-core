package com.audictionary.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ActingRole;
import com.audictionary.dto.Artwork;
import com.audictionary.dto.Recruit;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ActingRoleService;
import com.audictionary.service.ArtworkService;
import com.audictionary.service.GenFileService;
import com.audictionary.service.RecruitService;
import com.audictionary.util.Util;

@Controller
public class UsrRecruitController {
	@Autowired
	private RecruitService recruitService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private ArtworkService artworkService;
	@Autowired
	private ActingRoleService actingRoleService;

	@PostMapping("/usr/recruit/write")
	@ResponseBody
	public ResultData doWrite(@RequestParam Map<String, Object> param) {

		recruitService.doWrite(param);

		int rmId = Util.getAsInt(param.get("id"), 0);

		param.put("rmId", rmId);
		if(rmId == 0) {
			return new ResultData("F-1", "오류가 발생하여 공고를 등록하지 못했습니다.");
		}
		
		int artworkId = 0;
		if (rmId != 0) {
			artworkId = artworkService.doWriteArtworkForRecruitment(param);
		}
		
		int actingRoleId = 0;
		if (artworkId != 0) {
			actingRoleId = actingRoleService.doWriteActingRole(param);
		}
		
		if ( artworkId == 0 || actingRoleId == 0) {
			recruitService.deleteById(rmId);
			return new ResultData("F-1", "오류가 발생하여 공고를 등록하지 못했습니다.");
		}
		
		genFileService.changeInputFileRelIds(param, rmId);

		return new ResultData("S-1", "공고가 등록되었습니다.", "id", rmId);
	}

	@RequestMapping("/usr/recruit/list")
	@ResponseBody
	public ResultData showList(@RequestParam Map<String, Object> param) {
		int limit = 0;

		if (!param.get("limit").equals("null")) {
			limit = Integer.parseInt((String) param.get("limit"));
		}

		List<Recruit> recruits = recruitService.getListForPrintByFilter(param);

		boolean isAllLoaded = false;

		if (limit > recruits.size()) {
			isAllLoaded = true;
		}

		return new ResultData("S-1", "공고 리스트 받기", "recruits", recruits, "isAllLoaded", isAllLoaded);
	}

	@GetMapping("/usr/recruit/detail")
	@ResponseBody
	public ResultData showDetail(@RequestParam int id) {

		Recruit recruit = recruitService.getRecruitForPrintById(id);

		return new ResultData("S-1", "공고 불러오기", "recruit", recruit);
	}

	@PostMapping("/usr/recruit/modify")
	@ResponseBody
	public ResultData modifyRecruitment(@RequestParam Map<String, Object> param) {
		Recruit recruit = recruitService.getRecruitById(Integer.parseInt((String) param.get("recruitmentId")));
		
		if (recruit == null) {
			return new ResultData("F-1", "존재하지 않는 공고입니다.");
		}

		if (param.get("isFileUploaded").equals("true")) {
			genFileService.deleteGenFiles("recruit", recruit.getId());
		}

		boolean isNeedToModify = false;

		if (!recruit.getTitle().equals(param.get("rmTitle")) || !recruit.getBody().equals(param.get("rmBody"))
				|| !recruit.getRoleType().equals(param.get("rmRoleType"))
				|| !recruit.getPay().equals(param.get("rmPay"))
				|| !recruit.getLocation().equals(param.get("rmLocation"))
				|| !recruit.getPeriod().equals(param.get("rmPeriod"))
				|| !recruit.getDeadline().equals(param.get("rmDeadline"))
				|| !recruit.getGender().equals(param.get("rmGender")) || !recruit.getAge().equals(param.get("rmAge"))
				|| !recruit.getScript().equals(param.get("rmScript"))
				|| !recruit.getVideoTime().equals(param.get("rmVideoTime"))
				|| !recruit.getEtc().equals(param.get("rmEtc"))) {
			isNeedToModify = true;
		}

		param.put("isNeedToModify", isNeedToModify);

		recruitService.doModify(param);

		modifyArtwork(param);
		
		modifyActingRole(param);

		recruit = recruitService.getRecruitById(Integer.parseInt((String) param.get("recruitmentId")));

		return new ResultData("S-1", "공고가 수정되었습니다.", "recruit", recruit);
	}

	/* 공고가 수정될 때 해당 공고의 작품도 수정하는 메소드 */
	public void modifyArtwork(Map<String, Object> param) {

		Artwork artwork = artworkService
				.getArtworkByRecruitmentId(Integer.parseInt((String) param.get("recruitmentId")));

		if (artwork == null) {
			return;
		}
		boolean isNeedToModify = false;

		if (!artwork.getMedia().equals(param.get("awMedia")) || !artwork.getTitle().equals(param.get("awTitle"))
				|| !artwork.getCorp().equals(param.get("awCorp"))
				|| !artwork.getDirector().equals(param.get("awDirector"))
				|| !artwork.getProducer().equals(param.get("awProducer"))
				|| !artwork.getCastingManager().equals(param.get("awManager"))
				|| !artwork.getGenre().equals(param.get("awGenre")) || !artwork.getStory().equals(param.get("awStory"))
				|| !artwork.getEtc().equals(param.get("awEtc"))) {
			isNeedToModify = true;
		}

		param.put("isNeedToModify", isNeedToModify);

		artworkService.doModify(param);

	}

	/* 공고가 수정될 때 해당 공고의 배역도 수정하는 메소드 */
	public void modifyActingRole(Map<String, Object> param) {

		ActingRole actingRole = actingRoleService
				.getActingRoleByRecruitmentId(Integer.parseInt((String) param.get("recruitmentId")));

		if (actingRole == null) {
			return;
		}
		boolean isNeedToModify = false;

		if (!actingRole.getRealName().equals(param.get("arRealName"))
				|| !actingRole.getName().equals(param.get("arName")) || !actingRole.getAge().equals(param.get("arAge"))
				|| !actingRole.getGender().equals(param.get("arGender"))
				|| !actingRole.getJob().equals(param.get("arJob"))
				|| actingRole.getScriptStatus() != (String) param.get("arScript")
				|| actingRole.getScenesCount() != Integer.parseInt((String) param.get("arScenesCount"))
				|| actingRole.getShootingsCount() != Integer.parseInt((String) param.get("arShootingsCount"))
				|| actingRole.getCharacter().equals(param.get("arCharacter"))
				|| actingRole.getEtc().equals(param.get("arEtc"))) {
			isNeedToModify = true;
		}

		param.put("isNeedToModify", isNeedToModify);

		actingRoleService.doModify(param);

	}

	/* 공고 검색 컨트롤러 (현재 검색 기능 제거로 쓰이지 않음) */
	@RequestMapping("/usr/recruit/search")
	@ResponseBody
	public ResultData doSearchByKeyword(@RequestParam Map<String, Object> param) {

		List<Recruit> recruits = recruitService.getListForPrintByKeyword(param);

		return new ResultData("S-1", "검색 성공", "recruits", recruits);
	}

	/* 회원이 작성한 공고 리스트만 가져오기 */
	@RequestMapping("/usr/recruit/listByMemberId")
	@ResponseBody
	public ResultData showListByMemberId(@RequestParam Map<String, Object> param) {

		List<Recruit> recruits = recruitService.getListForPrintByMemberId(param);

		return new ResultData("S-1", "공고 리스트 불러오기 성공", "recruits", recruits);
	}
	
	@RequestMapping("/usr/recruit/finish")
	@ResponseBody
	public ResultData doFinish(@RequestParam Map<String, Object> param) {
		int id = Integer.parseInt((String)param.get("id"));
		
		Recruit recruit = recruitService.getRecruitById(id);
		
		if( recruit == null ) {
			return new ResultData("F-1", "존재하지 않는 공고입니다.");
		}
		
		recruitService.doFinishById(id);
		
		return new ResultData("S-1", "공고가 마감되었습니다.");
	}
}
