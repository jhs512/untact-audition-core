package com.audictionary.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApplicationDao;
import com.audictionary.dto.Application;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Recruit;

@Service
public class ApplicationService {
	@Autowired
	ApplicationDao applicationDao;
	@Autowired
	GenFileService genFileService;

	public List<Application> getListForPrint(Map<String, Object> param) {
		
		List<Application> applications =  applicationDao.getListByRecruitId(param);
		
		List<Integer> applicationIds = applications.stream().map(application -> application.getId()).collect(Collectors.toList());
		
		Map<Integer, Map<String, GenFile>> profileFilesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application", applicationIds, "profile", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = profileFilesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__profile__attachment", mapByFileNo);
			}
			
		}
		
		Map<Integer, Map<String, GenFile>> photoFilesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application", applicationIds, "photo", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = photoFilesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__photo__attachment", mapByFileNo);
			}
			
		}
		
		Map<Integer, Map<String, GenFile>> videoFilesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application", applicationIds, "video", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = videoFilesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__video__attachment", mapByFileNo);
			}
			
		}		
		
		return applications;
	}

	public List<Application> getListByRecruitId(Map<String, Object> param) {
		List<Application> applications =  applicationDao.getListByRecruitId(param);
		
		List<Integer> applicationIds = applications.stream().map(recruit -> recruit.getId()).collect(Collectors.toList());
		
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application", applicationIds, "common", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = filesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__common__attachment", mapByFileNo);
			}
			
		}
		
		return applications;
	}

	public void setPassStatus(int applicationId, int status) {
		applicationDao.setPassStatus(applicationId, status);
	}

	
	public void doSelect(Map<String, Object> param) {
		int applicationId = Integer.parseInt((String)param.get("applicationId"));
		setPassStatus(applicationId, 1);	
	}

	public void doFail(Map<String, Object> param) {
		int applicationId = Integer.parseInt((String)param.get("applicationId"));
		setPassStatus(applicationId, -1);
	}

	public void doLike(Map<String, Object> param) {
		applicationDao.doLike(param);
	}

	public Application getApplicationByMemberId(int memberId) {
		return applicationDao.getApplicationByMemberId(memberId);
	}

	public Application getApplicationByRecruitId(int id) {
		return applicationDao.getApplicationByRecruitId(id);
	}

	public Application getListForPdInfo(int applicationId) {
		return applicationDao.getListForPdInfo(applicationId);
	}

	public int getLike(Map<String, Object> param) {
		return applicationDao.getLike(param);
	}

	public void cancelLike(Map<String, Object> param) {
		applicationDao.cancelLike(param);
	}

	public Application getApplicationById(Map<String, Object> param) {
		return applicationDao.getApplicationById(param);
	}
}
