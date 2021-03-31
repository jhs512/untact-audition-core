package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Recruit;

@Mapper
public interface RecruitDao {

	int doWrite(Map<String, Object> param);

	List<Recruit> getListForPrint(@Param("limit")int limit);

	Recruit getRecruitById(@Param("id") int id);

	void doModify(Map<String, Object> param);

	

	

}
