package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.ActingRole;

@Mapper
public interface ActingRoleDao {

	ActingRole getActingRoleByRecruitmentId(@Param("recruitmentId")int recruitmentId);

	int doWriteActingRole(Map<String, Object> param);

	void doModify(Map<String, Object> param);

}
