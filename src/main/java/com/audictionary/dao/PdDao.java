package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.audictionary.dto.Pd;

@Mapper
public interface PdDao {

	int doJoin(Map<String, Object> param);

	Pd getMemberById(int loginedMemberId);

	Pd isAdmin(int loginedMemberId);

}
