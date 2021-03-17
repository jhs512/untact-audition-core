package com.audictionary.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ApDao;
import com.audictionary.dto.Ap;

@Service
public class ApService {
	
	@Autowired
	private ApDao apDao;

	public void doJoin(Map<String, Object> param) {
		apDao.doJoin(param);
	}

	public Ap getApByLoginId(String loginId) {
		return apDao.getApByLoginId(loginId);
	}

}
