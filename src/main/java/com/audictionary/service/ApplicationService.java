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
		
		List<Application> applications =  applicationDao.getListForPrint(param);
		
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

}