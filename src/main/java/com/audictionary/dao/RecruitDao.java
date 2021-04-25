package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Recruit;

@Mapper
public interface RecruitDao {

	int doWrite(Map<String, Object> param);

	List<Recruit> getListForPrint(Map<String,Object> param);

	Recruit getRecruitById(@Param("id") int id);

	void doModify(Map<String, Object> param);

	int getRecruitsCount();

	Recruit getRecruitByLimit(int limitStart);

	List<Recruit> getListForPrintByFilter(Map<String, Object> param);

	List<Recruit> getListForPrintByKeyword(Map<String, Object> param);

	Recruit getRecruitByArtworkId(int id);

	Recruit getRecruitForPrintById(int id);

	List<Recruit> getListForPrintByMemberId(Map<String, Object> param);

	

	

}
