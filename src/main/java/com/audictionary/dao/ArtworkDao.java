package com.audictionary.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Artwork;

@Mapper
public interface ArtworkDao {

	Artwork getArtworkByRecruitmentId(int rmId);

	void doModify(Map<String, Object> param);

	int doWriteArtwork(Map<String, Object> param);

	void deleteByPdId(int loginedMemberId);

	void doWriteArtworkForRecruitment(Map<String, Object> param);

	void doWriteArtWorkForPdProfile(Map<String, Object> param);

}
