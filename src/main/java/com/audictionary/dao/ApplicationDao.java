package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.audictionary.dto.Application;

@Mapper
public interface ApplicationDao {

	List<Application> getListForPrint(Map<String, Object> param);

}
