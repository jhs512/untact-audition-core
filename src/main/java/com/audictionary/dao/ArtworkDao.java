package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Artwork;

@Mapper
public interface ArtworkDao {

	Artwork getArtworkByRecruitmentId(@Param("recruitmentId")int recruitmentId);

	void doModify(Map<String, Object> param);

	int doWriteArtwork(Map<String, Object> param);

}
