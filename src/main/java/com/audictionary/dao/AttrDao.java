package com.audictionary.dao;

import org.apache.ibatis.annotations.Mapper;

import com.audictionary.dto.Attr;

@Mapper
public interface AttrDao {

	Attr get(String relTypeCode, int relId, String typeCode, String type2Code);

	String getValue(String relTypeCode, int relId, String typeCode, String type2Code);

	void remove(String relTypeCode, int relId, String typeCode, String type2Code);

	int setValue(String relTypeCode, int relId, String typeCode, String type2Code, String value, String expireDate);

}
