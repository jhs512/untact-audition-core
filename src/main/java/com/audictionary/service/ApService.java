package com.audictionary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApDao;
import com.audictionary.dto.Ap;
import com.audictionary.dto.Application;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Recruit;
import com.audictionary.dto.ResultData;
import com.audictionary.util.Util;

@Service
public class ApService {
	
	@Autowired
	private ApDao apDao;
	@Autowired
	private EmailService emailService; 
	@Autowired
	private AttrService attrService;
	@Autowired
	private ApplicationService applicationService;
	@Autowired
	private GenFileService genFileService;
	@Autowired
	private RecruitService recruitService;

	public ResultData doJoin(Map<String, Object> param) throws MessagingException {
		apDao.doJoin(param);
		
		emailService.sendMailForCertAp((String)param.get("loginId"));
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "회원가입성공", "id", id);
	}

	public Ap getApByLoginId(String loginId) {
		return apDao.getApByLoginId(loginId);
	}

	public void doModify(Map<String, Object> param) {
		
		apDao.doModify(param);
	}
	
	public Ap getApByAuthKey(String authKey) {
		return apDao.getApByAuthKey(authKey);
	}

	public Ap getApById(int loginedMemberId) {
		return apDao.getApById(loginedMemberId);
	}

	public boolean isAdmin(int loginedMemberId) {
		Ap ap = apDao.isAdmin(loginedMemberId);
		
		if ( ap != null ) {
			return true;
		}else { 
			return false;
		}
	}

	public Ap doIdDupCheck(String loginId) {
		return apDao.doIdDupCheck(loginId);
	}

	public void setAuthStatusValid(String authKey) {
		apDao.setAuthStatusValid(authKey);
		
	}

	

	public List<Ap> getApListByRecruitId(Map<String, Object> param) {
		
		List<Application> applications = applicationService.getListByRecruitId(param);
		
		List<Integer> memberIds = applications.stream().map(application -> application.getMemberId()).collect(Collectors.toList());

		List<Ap> aps = new ArrayList<>();
		
		for ( int memberId : memberIds ) {
			Ap ap = apDao.getApById(memberId);
			
			GenFile genFile = genFileService.getGenFile("ap", ap.getId(), "common", "attachment", 1);
			
			if ( genFile != null ) {
				String imgUrl = genFile.getForPrintUrl();
				ap.setExtra__thumbImg(imgUrl);
			}
			
			aps.add(ap);
		}
		
		return aps;
	}

	public void doLike(String relTypeCode, int relId, String memberTypeCode, int memberId) {
		apDao.doLike(relTypeCode, relId, memberTypeCode, memberId);
	}
	public int isDupLike(int relId, int memberId) {
		return apDao.isDupLike(relId, memberId);
	}

	public void deleteLike(int relId, int memberId) {
		apDao.deleteLike(relId, memberId);
		
	}

	public List<Integer> likeListsByMemberId(int memberId) {
		return apDao.likeListsByMemberId(memberId);
	}

	public List<Recruit> getLikedRecruits(List<Integer> likedRecruitIds) {
		List<Recruit> LikedRecruits = new ArrayList<>();
		
		for ( int likedRecruitId :  likedRecruitIds) {
			Recruit recruit = recruitService.getRecruitForPrintById(likedRecruitId);
			
			LikedRecruits.add(recruit);
		}
		
		return LikedRecruits;
	}
	

}
