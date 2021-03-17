package com.audictionary.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApDao;
import com.audictionary.dto.Ap;
import com.audictionary.dto.ResultData;
import com.audictionary.util.Util;

@Service
public class ApService {
	
	@Autowired
	private ApDao apDao;

	public ResultData doJoin(Map<String, Object> param) {
		apDao.doJoin(param);
		
		int id = Util.getAsInt(param.get("id"), 0);
		
		return new ResultData("S-1", "회원가입성공", "id", id);
	}

	public Ap getApByLoginId(String loginId) {
		return apDao.getApByLoginId(loginId);
	}

	public ResultData doModify(Map<String, Object> param) {
		
		apDao.doModify(param);

		return new ResultData("S-1", "회원정보가 수정되었습니다.");
	}

}
