package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Ap;

@Mapper
public interface ApDao {

	void doJoin(@Param("param") Map<String, Object> param);

	Ap getApByLoginId(@Param("loginId") String loginId);
}
