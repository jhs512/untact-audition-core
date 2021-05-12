package com.audictionary.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.audictionary.dto.Ap;
import com.audictionary.dto.Application;
import com.audictionary.dto.ResultData;
import com.audictionary.service.ApService;
import com.audictionary.service.ApplicationService;
import com.audictionary.service.GenFileService;
import com.audictionary.util.Util;

@Controller
public class UsrApplicationController {
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private ApService apService;
	
	@RequestMapping("/usr/application/list")
	@ResponseBody
	public ResultData showList(@RequestParam Map<String,Object> param) {
		
		List<Application> applications = applicationService.getListForPrint(param);
		
		return new ResultData("S-1", "지원 목록 불러오기 성공", "applications", applications);
	}
	

	@PostMapping("/usr/application/write")
	@ResponseBody
	public ResultData doWrite(@RequestParam Map<String, Object> param) {
		
		applicationService.doWrite(param);
		
		int applicationId = Util.getAsInt(param.get("id"), 0);
		
		genFileService.changeInputFileRelIdsForApplication(param, applicationId);
		
		applicationService.getProfileImgFileIdsStr(param, applicationId);

		return new ResultData("S-1", "지원이 완료되었습니다.", "id", applicationId);
	}

	@RequestMapping("/usr/application/select")
	@ResponseBody
	public ResultData doSelect(@RequestParam Map<String,Object> param) {
		
		applicationService.doSelect(param);
		
		return new ResultData("S-1", "지원자선정");
	}
	
	@RequestMapping("/usr/application/fail")
	@ResponseBody
	public ResultData doFail(@RequestParam Map<String,Object> param) {
		
		applicationService.doFail(param);
		
		return new ResultData("S-1", "지원자탈락");
	}
	
	@RequestMapping("/usr/application/like")
	@ResponseBody
	public ResultData doLike(@RequestParam Map<String,Object> param) {
		param.put("relTypeCode", "application");
		param.put("memberTypeCode", "pd");
		int likeCount = applicationService.getLike(param);
		
		if( likeCount == 0 ) {
			applicationService.doLike(param);
		}
		if ( likeCount > 0) {
			cancelLike(param);
			return new ResultData("S-1","좋아요 취소");
		}
		
		return new ResultData("S-1", "좋아요 성공");
	}
	

	@RequestMapping("/usr/application/cancelLike")
	@ResponseBody
	public ResultData cancelLike(@RequestParam Map<String,Object> param) {
		
		int relId = Integer.parseInt((String)param.get("applicationId"));
		param.put("relId", relId);
		param.put("relTypeCode", "application");
		param.put("memberTypeCode", "pd");
		
		applicationService.cancelLike(param);
		
		List<Application> aps = apService.getListByLikedAp(param);
		
		return new ResultData("S-1","좋아요 취소 성공", "applications", aps);
	}
	
	@RequestMapping("/usr/application/detail")
	@ResponseBody
	public ResultData showDetail(@RequestParam Map<String, Object> param) {
		
		Application application = applicationService.getApplicationById(param);
		
		Ap ap = apService.getApById(application.getMemberId());
		
		return new ResultData("S-1", "공고 지원서 불러오기 성공", "application", application, "ap", ap);
	}

	@GetMapping("/usr/application/getApplications")
	@ResponseBody
	public ResultData getApplications(int memberId) {
		
		List<Application> applications = applicationService.getApplications(memberId);
		
		return new ResultData("S-1", "불러오기 성공", "applications", applications);
	}
	
	@GetMapping("/usr/application/getApplicationsAndRecruit")
	@ResponseBody
	public ResultData getApplicationsAndRecruit(int memberId) {
		
		List<Application> applications = applicationService.getApplicationsAndRecruit(memberId);
		
		return new ResultData("S-1", "불러오기 성공", "applications", applications);
	}
	

}
