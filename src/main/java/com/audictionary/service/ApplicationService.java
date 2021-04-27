package com.audictionary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApplicationDao;
import com.audictionary.dto.Application;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Recruit;
import com.audictionary.util.Util;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ApplicationService {
	@Autowired
	ApplicationDao applicationDao;
	@Autowired
	GenFileService genFileService;
	@Autowired
	AttrService atterService;

	public List<Application> getListForPrint(Map<String, Object> param) {
		
		List<Application> applications =  applicationDao.getListByRecruitId(param);
		
		List<Integer> applicationIds = applications.stream().map(application -> application.getId()).collect(Collectors.toList());
		
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application", applicationIds, "common", "attachment");
		
		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = filesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__common__attachment", mapByFileNo);
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

	public void doWrite(Map<String, Object> param) {
		applicationDao.doWrite(param);
		
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
		param.put("relTypeCode", "application");
		applicationDao.doLike(param);
	}

	public void getProfileImgFileIdsStr(Map<String, Object> param, int applicationId) {
		String profileImgIdsStr = (String)param.get("profileImgIdsStr");
		
		List<Map<String,Object>> profileImgIdsStrs = new ArrayList<>();
		
		if ( profileImgIdsStr != null && profileImgIdsStr.length() > 0 ) {
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);    
			try {
				profileImgIdsStrs = objectMapper.readValue(profileImgIdsStr, ArrayList.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for( int i = 0 ; i < profileImgIdsStrs.size(); i++ ) {
			Map<String, Object> idsStr = profileImgIdsStrs.get(i);
			
			atterService.setValue("application", applicationId, "genFile", "profile",(String)idsStr.get("profileImgIdsStr") , "");
		}
		
	}
}
