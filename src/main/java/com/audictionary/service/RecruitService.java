package com.audictionary.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.RecruitDao;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Recruit;

@Service
public class RecruitService {
	@Autowired
	private RecruitDao recruitDao;
	@Autowired
	private GenFileService genFileService;

	public int doWrite(Map<String, Object> param) {
		return recruitDao.doWrite(param);	
	}

	public List<Recruit> getListForPrint(int limit) {
		
		
		List<Recruit> recruits =  recruitDao.getListForPrint(limit);
		
		List<Integer> recruitIds = recruits.stream().map(recruit -> recruit.getId()).collect(Collectors.toList());
		Map<Integer, Map<String, GenFile>> filesMap = genFileService.getFilesMapKeyRelIdAndFileNo("recruit", recruitIds, "common", "attachment");
		
		for (Recruit recruit : recruits) {
			Map<String, GenFile> mapByFileNo = filesMap.get(recruit.getId());

			if (mapByFileNo != null) {
				recruit.getExtraNotNull().put("file__common__attachment", mapByFileNo);
			}
		}
		
		return recruits;
	}



	public Recruit getRecruitById(int id) {
		Recruit recruit = recruitDao.getRecruitById(id);
		
		List<GenFile> genFiles = genFileService.getGenFiles("recruit", id, "common", "attachment");
		
		if( !genFiles.isEmpty() ) {
			recruit.getExtraNotNull().put("file__common__attachment", genFiles);	
		}
		
		return recruit;
		
	}

	public void doModify(Map<String, Object> param) {
		recruitDao.doModify(param);
	}


}
