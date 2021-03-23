package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Recruit;

@Mapper
public interface RecruitDao {

	int doWrite(Map<String, Object> param);

	List<Recruit> getListForPrint();

	int doWriteArtwork(Map<String, Object> param);

	int doWriteActingRole(Map<String, Object> param);

	Recruit getRecruitById(@Param("id") int id);

	

}
