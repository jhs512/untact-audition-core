package com.audictionary.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.ActingRole;

@Mapper
public interface ActingRoleDao {

	ActingRole getActingRoleByRecruitmentId(@Param("recruitmentId")int recruitmentId);

}
