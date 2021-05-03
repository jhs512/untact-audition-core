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
		Ap ap = apDao.getApById(loginedMemberId);
		
		GenFile profileFile = genFileService.getGenFile("application", ap.getId(), "profile", "attachment", 1);
		GenFile videoFile = genFileService.getGenFile("application", ap.getId(), "video", "attachment", 1);
		
		if(profileFile != null) {
			ap.getExtraNotNull().put("file__profile__attachment", profileFile);
		}
		if(videoFile != null) {
			ap.getExtraNotNull().put("file__video__attachment", videoFile);
		}
		
		return ap;
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

	

	public Map<String, List<Ap>> getApListGroupByPassStatusByRecruitId(Map<String, Object> param) {
		
		List<Application> applications = applicationService.getListByRecruitId(param);
		
		Map<String, List<Ap>> apList = new HashMap<>();
		
		List<Ap> apListAll = new ArrayList<>();
		List<Ap> apList1Pass = new ArrayList<>();
		List<Ap> apList2Pass = new ArrayList<>();
		List<Ap> apList3Pass = new ArrayList<>();
		
		for (Application application : applications) {
			
			Map<String, GenFile> mapByFileNo = new HashMap<>();
			
			GenFile genFile = genFileService.getGenFile("application", application.getRecruitId(), "profile", "attachment", 1);
			
			mapByFileNo.put("1", genFile);
					
			Ap ap = getApById(application.getMemberId());
			
			if (mapByFileNo != null) {
				ap.getExtraNotNull().put("file__profile__attachment", mapByFileNo);
			}
			
			apListAll.add(ap);			
			
			if(application.getPassStatus() == 1) {
				apList1Pass.add(ap);
			}
			else if (application.getPassStatus() == 2) {
				apList2Pass.add(ap);
			}
			else if (application.getPassStatus() == 3) {
				apList3Pass.add(ap);
			}
			
		}
		
		apList.put("apListAll", apListAll);
		apList.put("apList1Pass", apList1Pass);
		apList.put("apList2Pass", apList2Pass);
		apList.put("apList3Pass", apList3Pass);
		
		return apList;
	}

	public List<Ap> getListByLikedApplication(Map<String, Object> param) {
		List<Integer> memberIds = apDao.getListByLikedApplication(param);
		
		List<Ap> aps = new ArrayList<>();
		
		for (int memberId : memberIds) {
			Ap ap = apDao.getListByMemberIds(memberId);
			aps.add(ap);
		}
		
		List<Integer> apIds = aps.stream().map(ap -> ap.getId()).collect(Collectors.toList());
		
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("ap", apIds, "profile", "attachment");
		
		for (Ap ap : aps) {
			Map<String, GenFile> mapByFileNo = filesMap.get(ap.getId());

			if (mapByFileNo != null) {
				ap.getExtraNotNull().put("file__profile__attachment", mapByFileNo);
			}
		}
		
		return aps;
	}


	public List<Application> getListByLikedAp(Map<String, Object> param) {
		List<Integer> likedApplicationIds = apDao.getListByLikedAp(param);
		
		List<Application> applications = new ArrayList<>();
		
		for(int applicationId: likedApplicationIds) {
			Application application = applicationService.getListForPdInfo(applicationId);
			applications.add(application);
		}
		
		List<Integer> applicationIds = applications.stream().map(application -> application.getMemberId()).collect(Collectors.toList());
		
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("ap", applicationIds, "profile", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = filesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__profile__attachment", mapByFileNo);
			}
		}
		
		return applications;
		
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

	public void doDeleteMemberById(int memberId) {
		apDao.doDeleteMemberById(memberId);
		
	}
	

}
