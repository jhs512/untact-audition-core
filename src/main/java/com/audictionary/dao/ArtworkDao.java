package com.audictionary.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Artwork;

@Mapper
public interface ArtworkDao {

	Artwork getArtworkByRecruitmentId(@Param("recruitmentId")int recruitmentId);

}
