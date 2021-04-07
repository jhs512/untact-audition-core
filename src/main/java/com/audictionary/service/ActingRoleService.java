package com.audictionary.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ActingRoleDao;
import com.audictionary.dto.ActingRole;

@Service
public class ActingRoleService {
	@Autowired
	private ActingRoleDao actingRoleDao;

	public ActingRole getActingRoleByRecruitmentId(int rmId) {
		return actingRoleDao.getActingRoleByRecruitmentId(rmId);
	}

	public int doWriteActingRole(Map<String, Object> param) {
		return actingRoleDao.doWriteActingRole(param);	
	}

	public void doModify(Map<String, Object> param) {
		actingRoleDao.doModify(param);
	}

	public List<ActingRole> getActingRolesForPrint(int limit) {
		return actingRoleDao.getActingRolesForPrint(limit);
	}

}
