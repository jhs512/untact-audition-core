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
		
		if( rmId != 0 ) {
			artworkService.doWriteArtworkForRecruitment(param);
			actingRoleService.doWriteActingRole(param);
		}
		
		genFileService.changeInputFileRelIds(param, rmId);

		return new ResultData("S-1", "공고 등록 성공", "id", rmId);
	}

	@RequestMapping("/usr/recruit/list")
	@ResponseBody
	public ResultData showList(@RequestParam Map<String,Object> param) {

		int limit = Integer.parseInt((String)param.get("limit"));
		
		List<Recruit> recruits = recruitService.getListForPrintByFilter(param);
		List<Artwork> artworks = new ArrayList<>();
		List<ActingRole> actingRoles = new ArrayList<>();
		
		for(Recruit recruit : recruits) {
			Artwork artwork = artworkService.getArtworkByRecruitmentId(recruit.getId());
			ActingRole actingRole = actingRoleService.getActingRoleByRecruitmentId(recruit.getId());
			
			artworks.add(artwork);
			actingRoles.add(actingRole);
		}
		
		boolean isAllLoaded = false;

		if (limit > recruits.size()) {
			isAllLoaded = true;
		}

		return new ResultData("S-1", "공고 리스트 출력", "recruits", recruits, "artworks", artworks, "actingRoles", actingRoles, "isAllLoaded", isAllLoaded);
	}

	@GetMapping("/usr/recruit/detail")
	@ResponseBody
	public ResultData showDetail(@RequestParam int id) {

		Recruit recruit = recruitService.getRecruitById(id);
		
		int rmId = id;
		
		Artwork artwork = artworkService.getArtworkByRecruitmentId(rmId);

		ActingRole actingRole = actingRoleService.getActingRoleByRecruitmentId(rmId);
		
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

		if ( !recruit.getTitle().equals(param.get("rmTitle")) || !recruit.getBody().equals(param.get("rmBody"))
				|| !recruit.getRoleType().equals(param.get("rmRoleType"))
				|| !recruit.getPay().equals(param.get("rmPay"))
				|| !recruit.getLocation().equals(param.get("rmLocation"))
				|| !recruit.getPeriod().equals(param.get("rmPeriod"))
				|| !recruit.getDeadline().equals(param.get("rmDeadline"))
				|| !recruit.getGender().equals(param.get("rmGender"))
				|| !recruit.getAge().equals(param.get("rmAge"))
				|| !recruit.getScript().equals(param.get("rmScript"))
				|| !recruit.getVideoTime().equals(param.get("rmVideoTime"))
				|| !recruit.getEtc().equals(param.get("rmEtc"))
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

		if ( !artwork.getMedia().equals(param.get("awMedia")) || !artwork.getTitle().equals(param.get("awTitle"))
				|| !artwork.getCorp().equals(param.get("awCorp"))
				|| !artwork.getDirector().equals(param.get("awDirector"))
				|| !artwork.getProducer().equals(param.get("awProducer"))
				|| !artwork.getCastingManager().equals(param.get("awManager"))
				|| !artwork.getGenre().equals(param.get("awGenre"))
				|| !artwork.getStory().equals(param.get("awStory"))
				|| !artwork.getEtc().equals(param.get("awEtc"))
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

		if ( !actingRole.getRealName().equals(param.get("arRealName")) || !actingRole.getName().equals(param.get("arName"))
				|| !actingRole.getAge().equals(param.get("arAge"))
				|| !actingRole.getGender().equals(param.get("arGender"))
				|| !actingRole.getJob().equals(param.get("arJob"))
				|| actingRole.getScriptStatus() != (String)param.get("arScript")
				|| actingRole.getScenesCount() != Integer.parseInt((String)param.get("arScenesCount"))
				|| actingRole.getShootingsCount() != Integer.parseInt((String)param.get("arShootingsCount"))
				|| actingRole.getCharacter().equals(param.get("arCharacter"))
				|| actingRole.getEtc().equals(param.get("arEtc"))
				) {
			isNeedToModify = true;
		}
		
		param.put("isNeedToModify", isNeedToModify);
		
		actingRoleService.doModify(param);
		
	}
	
	@RequestMapping("/usr/recruit/search")
	@ResponseBody
	public ResultData doSearchByKeyword(@RequestParam Map<String,Object> param) {
		
		List<Recruit> recruits = recruitService.getListForPrintByKeyword(param);
		List<Artwork> artworks = artworkService.getArtworksForPrintByKeyword(param);
		List<ActingRole> actingRoles = new ArrayList<>();

		for (Artwork artworkE : artworks) {
			if(artworkE.getRelTypeCode().equals("recruitment")) {
				if (recruits.indexOf(recruitService.getRecruitById(artworkE.getRelId())) < 0) {
					recruits.add(recruitService.getRecruitById(artworkE.getRelId()));	
				}
			}
		}
		
		for(Recruit recruit : recruits) {
			Artwork artwork = artworkService.getArtworkByRecruitmentId(recruit.getId());
			ActingRole actingRole = actingRoleService.getActingRoleByRecruitmentId(recruit.getId());
			if(artworks.indexOf(artwork) < 0) {
				artworks.add(artwork);
			}
			
			actingRoles.add(actingRole);
			
		}
		
		recruits = recruitService.getRecruitExtraFile(recruits);
		
		return new ResultData("S-1", "검색 성공", "recruits", recruits, "artworks", artworks, "actingRoles", actingRoles);
	}
}
