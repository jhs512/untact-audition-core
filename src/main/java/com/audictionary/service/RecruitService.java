package com.audictionary.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	public List<Recruit> getListForPrint(Map<String,Object> param) {
		
		int limit = Integer.parseInt((String)param.get("limit"));
		
		param.put("limit", limit);
		
		String[] filters;
		
		boolean isFiltered = false;
		
		
		if(param.get("filter") != null) {
			String filter = (String)param.get("filter");
			
			if(filter.length() > 0) {
				filters = filter.split(",");
				
				for(int i = 0 ; i < filters.length; i ++) {
					if(filters[i].equals("영유아 캐스팅")) {
						param.put("age1", "영유아");
						isFiltered = true;
					}
					if(filters[i].equals("10대 캐스팅")) {
						param.put("age10", "10대");
						isFiltered = true;
					}
					if(filters[i].equals("20대 캐스팅")) {
						param.put("age20", "20대");
						isFiltered = true;
					}
					if(filters[i].equals("30대 캐스팅")) {
						param.put("age30", "30대");
						isFiltered = true;
					}
					if(filters[i].equals("40-50대 캐스팅")) {
						param.put("age4050", "40-50대");
						isFiltered = true;
					}
					if(filters[i].equals("60대 이상 캐스팅")) {
						param.put("age60", "60대 이상");
						isFiltered = true;
					}
					if(filters[i].equals("남자 캐스팅")) {
						param.put("genderMale", "남자");
						isFiltered = true;
					}
					if(filters[i].equals("여자 캐스팅")) {
						param.put("genderFemale", "여자");
						isFiltered = true;
					}
					if(filters[i].equals("성별 무관 캐스팅")) {
						param.put("genderNone", "상관없음");
						isFiltered = true;
					}
					if(filters[i].equals("영화")) {
						param.put("genreMv", "영화");
						isFiltered = true;
					}
					if(filters[i].equals("드라마")) {
						param.put("genreDrama", "드라마");
						isFiltered = true;
					}
					if(filters[i].equals("연극")) {
						param.put("genreTheater", "연극");
						isFiltered = true;
					}
					if(filters[i].equals("독립영화")) {
						param.put("genreIndieMv", "독립영화");
						isFiltered = true;
					}
					if(filters[i].equals("현재 진행중인 공고")) {
						param.put("notExpired", "notExpired");
						isFiltered = true;
					}
					
				}
			}
			
		}
		
		param.put("isFiltered", isFiltered);
		
		List<Recruit> recruits =  recruitDao.getListForPrint(param);
		
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

	public int getRecruitsCount() {
		return recruitDao.getRecruitsCount();
		
	}

	public Recruit getRecruitByLimit(int limitStart) {
		Recruit recruit = recruitDao.getRecruitByLimit(limitStart);
		List<GenFile> genFiles = genFileService.getGenFiles("recruit", recruit.getId(), "common", "attachment");
		
		if( !genFiles.isEmpty() ) {
			recruit.getExtraNotNull().put("file__common__attachment", genFiles);	
		}
		return recruit;
	}


}
