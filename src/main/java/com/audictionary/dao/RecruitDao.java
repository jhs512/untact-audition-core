package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecruitDao {

	int doWrite(Map<String, Object> param);

	

}
