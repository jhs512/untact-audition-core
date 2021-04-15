package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Ap;

@Mapper
public interface ApDao {

	void doJoin(Map<String, Object> param);

	Ap getApByLoginId(@Param("loginId") String loginId);

	void doModify(Map<String, Object> param);

	Ap getApByAuthKey(String authKey);

	Ap getApById(int loginedMemberId);

	Ap isAdmin(int loginedMemberId);

	Ap doIdDupCheck(String loginId);
}
