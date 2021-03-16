package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApDao {

	void doJoin(Map<String, Object> param);
	

}
