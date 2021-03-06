package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Pd;

@Mapper
public interface PdDao {

	int doJoin(Map<String, Object> param);

	Pd getMemberById(int loginedMemberId);

	Pd isAdmin(int loginedMemberId);

	Pd getMemberByEmail(String email);

	void doModify(Map<String, Object> param);

	Pd getMemberByAuthKey(@Param("authKey") String authKey);

	void doDeleteMemberById(int id);

	Pd doFindLoginId(Map<String, Object> param);

	void setTempPw(String email, String tempPw);
}
