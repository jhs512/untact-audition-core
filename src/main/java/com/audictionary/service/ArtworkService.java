package com.audictionary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ArtworkDao;
import com.audictionary.dto.Artwork;

@Service
public class ArtworkService {
	@Autowired
	private ArtworkDao artworkDao;

	public Artwork getArtworkByRecruitmentId(int recruitmentId) {
		return artworkDao.getArtworkByRecruitmentId(recruitmentId);
	}

}
