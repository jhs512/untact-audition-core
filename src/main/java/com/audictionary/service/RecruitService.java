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

	public List<Recruit> getListForPrint() {
		List<Recruit> recruits =  recruitDao.getListForPrint();
		
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

	public int doWriteArtwork(Map<String, Object> param) {
		return recruitDao.doWriteArtwork(param);
		
	}

	public int doWriteActingRole(Map<String, Object> param) {
		return recruitDao.doWriteActingRole(param);
		
	}

}
