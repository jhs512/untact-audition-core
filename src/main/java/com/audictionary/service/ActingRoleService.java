package com.audictionary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ActingRoleDao;
import com.audictionary.dto.ActingRole;

@Service
public class ActingRoleService {
	@Autowired
	private ActingRoleDao actingRoleDao;

	public ActingRole getActingRoleByRecruitmentId(int recruitmentId) {
		return actingRoleDao.getActingRoleByRecruitmentId(recruitmentId);
	}

}
