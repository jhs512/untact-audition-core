package com.audictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.ActingRole;
import com.audictionary.dto.Artwork;
import com.audictionary.dto.GenFile;
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

		int id = Util.getAsInt(param.get("id"), 0);
		
		param.put("recruitId", id);
		
		artworkService.doWriteArtwork(param);
		
		actingRoleService.doWriteActingRole(param);

		genFileService.changeInputFileRelIds(param, id);

		return new ResultData("S-1", "공고 등록 성공", "id", id);
	}

	@GetMapping("/usr/recruit/list")
	@ResponseBody
	public ResultData showList(@RequestParam int limit) {

		List<Recruit> recruits = recruitService.getListForPrint(limit);

		boolean isAllLoaded = false;

		if (limit > recruits.size()) {
			isAllLoaded = true;
		}

		return new ResultData("S-1", "공고 리스트 출력", "recruits", recruits, "isAllLoaded", isAllLoaded);
	}

	@GetMapping("/usr/recruit/detail")
	@ResponseBody
	public ResultData showDetail(@RequestParam int id) {

		Recruit recruit = recruitService.getRecruitById(id);
		
		int recruitmentId = id;
		
		Artwork artwork = artworkService.getArtworkByRecruitmentId(recruitmentId);

		ActingRole actingRole = actingRoleService.getActingRoleByRecruitmentId(recruitmentId);
		
		return new ResultData("S-1", "공고 불러오기", "recruit", recruit, "artwork", artwork, "actingRole", actingRole);
	}


	@PostMapping("/usr/recruit/modify")
	@ResponseBody
	public ResultData modifyRecruitment(@RequestParam Map<String, Object> param) {
		Recruit recruit = recruitService.getRecruitById(Integer.parseInt((String)param.get("recruitmentId")));

		if (recruit == null) {
			return new ResultData("F-1", "해당 공고가 없습니다.");
		}
		
		if ( param.get("isFileUploaded").equals("true") ) {
			genFileService.deleteGenFiles("recruit", recruit.getId());
		}
		
		boolean isNeedToModify = false;

		if ( !recruit.getTitle().equals(param.get("title")) || !recruit.getBody().equals(param.get("body"))
				|| !recruit.getRoleType().equals(param.get("roleType"))
				|| !recruit.getLocation().equals(param.get("location"))
				|| !recruit.getPeriod().equals(param.get("period"))
				|| !recruit.getDeadline().equals(param.get("deadline"))
				|| !recruit.getManager().equals(param.get("manager"))
				) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		
		recruitService.doModify(param);
		
		modifyArtwork(param);
		modifyActingRole(param);
		
		
		recruit = recruitService.getRecruitById(Integer.parseInt((String)param.get("recruitmentId")));

		return new ResultData("S-1", "공고 수정", "recruit", recruit);
	}
	
	public void modifyArtwork(Map<String, Object> param) {
		
		Artwork artwork = artworkService.getArtworkByRecruitmentId(Integer.parseInt((String)param.get("recruitmentId")));
		
		if( artwork == null ) {
			return;
		}
		boolean isNeedToModify = false;

		if ( !artwork.getName().equals(param.get("artworkName")) || !artwork.getGenre().equals(param.get("genre"))
				|| !artwork.getCorp().equals(param.get("corp"))
				|| !artwork.getDirector().equals(param.get("director"))
				|| !artwork.getEtc().equals(param.get("artworkEtc"))
				) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		
		artworkService.doModify(param);
		
	}
	
	public void modifyActingRole(Map<String, Object> param) {
		
		ActingRole actingRole = actingRoleService.getActingRoleByRecruitmentId(Integer.parseInt((String)param.get("recruitmentId")));
		
		if( actingRole == null ) {
			return;
		}
		boolean isNeedToModify = false;

		if ( !actingRole.getRealName().equals(param.get("roleRealName")) || !actingRole.getName().equals(param.get("roleName"))
				|| !actingRole.getPay().equals(param.get("pay"))
				|| !actingRole.getAge().equals(param.get("roleAge"))
				|| !actingRole.getGender().equals(param.get("roleGender"))
				|| !actingRole.getJob().equals(param.get("roleJob"))
				|| actingRole.getScriptStatus() != Integer.parseInt((String)param.get("roleScript"))
				|| actingRole.getScenesCount() != Integer.parseInt((String)param.get("roleScenesCount"))
				|| actingRole.getShootingsCount() != Integer.parseInt((String)param.get("roleShootingsCount"))
				|| actingRole.getCharacter().equals(param.get("roleCharacter"))
				|| actingRole.getEtc().equals(param.get("actingRoleEtc"))
				) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		
		actingRoleService.doModify(param);
		
	}
}
