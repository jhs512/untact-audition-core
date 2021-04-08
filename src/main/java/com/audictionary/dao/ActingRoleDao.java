package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.ActingRole;

@Mapper
public interface ActingRoleDao {

	ActingRole getActingRoleByRecruitmentId(int rmId);

	int doWriteActingRole(Map<String, Object> param);

	void doModify(Map<String, Object> param);

	List<ActingRole> getActingRolesForPrint(Map<String,Object> param);

}
