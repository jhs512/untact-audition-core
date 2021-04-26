package com.audictionary.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApDao;
import com.audictionary.dto.Ap;
import com.audictionary.dto.Application;
import com.audictionary.dto.Attr;
import com.audictionary.dto.GenFile;
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

	public void doSelect(Map<String, Object> param) {
		String relTypeCode = "application";
		int relId = Integer.parseInt((String)param.get("applicationId"));
		String typeCode = "ap";
		String type2Code = "1";
		String memberId = (String)param.get("memberId");
		
		attrService.setValue(relTypeCode, relId, typeCode, type2Code, memberId, "");
		
	}

	public List<Ap> getApsApplying(Map<String, Object> param) {
		
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

	public Map<String,List> getApsAccepted(Map<String, Object> param) {
		
		int recruitId = Integer.parseInt((String)param.get("id"));
		
		List<Attr> attrs1 = attrService.getAttrs("application", recruitId, "ap", "1");
		List<Attr> attrs2 = attrService.getAttrs("application", recruitId, "ap", "2");
		List<Attr> attrs3 = attrService.getAttrs("application", recruitId, "ap", "3");
		
		List<Ap> apListAccepted1 = new ArrayList<>();
		List<Ap> apListAccepted2 = new ArrayList<>();
		List<Ap> apListAccepted3 = new ArrayList<>();
		
		for(Attr attr : attrs1) {
			Ap ap = apDao.getApById(Integer.parseInt(attr.getValue()));
			
			GenFile genFile = genFileService.getGenFile("ap", ap.getId(), "common", "attachment", 1);
			
			if ( genFile != null ) {
				String imgUrl = genFile.getForPrintUrl();
				ap.setExtra__thumbImg(imgUrl);
			}
			
			apListAccepted1.add(ap);
		}
		
		for(Attr attr : attrs2) {
			Ap ap = apDao.getApById(Integer.parseInt(attr.getValue()));
			
			GenFile genFile = genFileService.getGenFile("ap", ap.getId(), "common", "attachment", 1);
			
			if ( genFile != null ) {
				String imgUrl = genFile.getForPrintUrl();
				ap.setExtra__thumbImg(imgUrl);
			}
			
			apListAccepted2.add(ap);
		}
		
		for(Attr attr : attrs3) {
			Ap ap = apDao.getApById(Integer.parseInt(attr.getValue()));
			
			GenFile genFile = genFileService.getGenFile("ap", ap.getId(), "common", "attachment", 1);
			
			if ( genFile != null ) {
				String imgUrl = genFile.getForPrintUrl();
				ap.setExtra__thumbImg(imgUrl);
			}
			
			apListAccepted3.add(ap);
		}
		
		Map<String,List> apListAccepted = new HashMap<>();
		
		apListAccepted.put("apListAccepted1", apListAccepted1);
		apListAccepted.put("apListAccepted2", apListAccepted2);
		apListAccepted.put("apListAccepted3", apListAccepted3);
		
		return apListAccepted;
		
	}
	

}
