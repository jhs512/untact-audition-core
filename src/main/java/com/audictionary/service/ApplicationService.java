package com.audictionary.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApplicationDao;
import com.audictionary.dto.Application;
import com.audictionary.dto.Attr;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Recruit;
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
	AttrService attrService;
	@Autowired
	RecruitService recruitService;

	public List<Application> getListForPrint(Map<String, Object> param) {

		List<Application> applications = applicationDao.getListByRecruitId(param);
		
		List<Integer> applicationIds = applications.stream().map(application -> application.getId())
				.collect(Collectors.toList());

		for (Application application : applications) {
			
			param.put("relTypeCode", "application");
			param.put("applicationId", application.getId());
			param.put("memberTypeCode", "pd");
			
			int likeCount = getLike(param);
			
			if(likeCount > 0) {
				application.getExtraNotNull().put("isLiked", "solid");
			} else {
				application.getExtraNotNull().put("isLiked", "outline");
			}
			
			List<GenFile> genFiles = new ArrayList<>();
			List<String> values = new ArrayList<>();
			
			values = attrService.getValues("application", application.getId(), "genFile", "profile");
			
			for (String value : values) {
				GenFile genFile = genFileService.getGenFile(Integer.parseInt(value));
				genFiles.add(genFile);
			}
			
			Map<Integer, Map<String, GenFile>> profileFilesMap = new LinkedHashMap<>();

			for (GenFile genFile : genFiles) {
				
				if (profileFilesMap.containsKey(genFile.getRelId()) == false) {
					profileFilesMap.put(genFile.getRelId(), new LinkedHashMap<>());
				}

				profileFilesMap.get(genFile.getRelId()).put(genFile.getFileNo() + "", genFile);
			}
			
			Map<String, GenFile> mapByFileNo = profileFilesMap.get(application.getMemberId());
			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__profile__attachment", mapByFileNo);
			}

		}
		
		Map<Integer, Map<String, GenFile>> photoFilesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application",
				applicationIds, "photo", "attachment");

		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = photoFilesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__photo__attachment", mapByFileNo);
			}

		}

		Map<Integer, Map<String, GenFile>> videoFilesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application",
				applicationIds, "video", "attachment");

		for (Application application : applications) {
			Map<String, GenFile> mapByFileNo = videoFilesMap.get(application.getId());

			if (mapByFileNo != null) {
				application.getExtraNotNull().put("file__video__attachment", mapByFileNo);
			}

		}

		return applications;
	}

	public List<Application> getListByRecruitId(Map<String, Object> param) {
		List<Application> applications = applicationDao.getListByRecruitId(param);

		List<Integer> applicationIds = applications.stream().map(recruit -> recruit.getId())
				.collect(Collectors.toList());

		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("application",
				applicationIds, "common", "attachment");

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
		int applicationId = Integer.parseInt((String) param.get("applicationId"));
		setPassStatus(applicationId, 1);
	}

	public void doFail(Map<String, Object> param) {
		int applicationId = Integer.parseInt((String) param.get("applicationId"));
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
		Application application = applicationDao.getApplicationById(param); 
		
		int applicationId =  Integer.parseInt((String)param.get("id"));
		
		GenFile photo = genFileService.getGenFile("application", applicationId, "photo", "attachment", 1);
		
		if (photo != null) {
			application.getExtraNotNull().put("file__photo__attachment", photo);
		}
		
		GenFile video = genFileService.getGenFile("application", applicationId, "video", "attachment", 1);
		
		if (video != null) {
			application.getExtraNotNull().put("file__video__attachment", video);
		}
		
		return application;
	}

	public void getProfileImgFileIdsStr(Map<String, Object> param, int applicationId) {
		String profileImgIdsStr = (String) param.get("profileImgIdsStr"); 

		List<Map<String, Object>> profileImgIdsStrs = new ArrayList<>();

		if (profileImgIdsStr != null && profileImgIdsStr.length() > 0) {

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

		for (int i = 0; i < profileImgIdsStrs.size(); i++) {
			String value = String.valueOf(profileImgIdsStrs.get(i));
			attrService.setValue("application", applicationId, "genFile", "profile", value, "");
		}

	}

	public List<Application> getApplications(int memberId) {
		return applicationDao.getListForPrintByMemberId(memberId);
	}

	public List<Application> getApplicationsAndRecruit(int memberId) {

		List<Application> applications = applicationDao.getListForPrintByMemberId(memberId);
		;

		for (Application application : applications) {
			int recruitId = application.getRecruitId();
			Recruit recruit = recruitService.getRecruitForPrintById(recruitId);

			application.getExtraNotNull().put("Extra__aw_title", recruit.getExtra__aw_title());
			application.getExtraNotNull().put("Extra__ar_name", recruit.getExtra__ar_name());
			application.getExtraNotNull().put("Extra__deadline", recruit.getDeadline());
		}

		return applications;

	}
}
