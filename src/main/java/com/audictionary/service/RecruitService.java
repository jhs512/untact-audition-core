package com.audictionary.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.RecruitDao;
import com.audictionary.dto.ResultData;
import com.audictionary.util.Util;

@Service
public class RecruitService {
	@Autowired
	private RecruitDao recruitDao;
	@Autowired
	private GenFileService genFileService;

	public ResultData doWrite(Map<String, Object> param) {
		recruitDao.doWrite(param);
		
		int id = Util.getAsInt(param.get("id"),0);
		
		genFileService.changeInputFileRelIds(param, id);
		
		return new ResultData("S-1", "공고 등록 성공", "id", id);
		
	}

}
