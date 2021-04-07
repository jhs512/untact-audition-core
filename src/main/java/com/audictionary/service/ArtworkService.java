package com.audictionary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.ArtworkDao;
import com.audictionary.dto.Artwork;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ArtworkService {
	@Autowired
	private ArtworkDao artworkDao;

	public Artwork getArtworkByRecruitmentId(int rmId) {
		return artworkDao.getArtworkByRecruitmentId(rmId);
	}

	public void doModify(Map<String, Object> param) {
		artworkDao.doModify(param);
	}
	
	public int doWriteArtwork(Map<String, Object> param) {
		return artworkDao.doWriteArtwork(param);
	}

	public void deleteByPdId(int loginedMemberId) {
		artworkDao.deleteByPdId(loginedMemberId);
	}

	public void doWriteArtworkForRecruitment(Map<String, Object> param) {
		artworkDao.doWriteArtworkForRecruitment(param);
		
	}

	public void doWriteArtWorkForPdProfile(Map<String, Object> param) {
		String artworkEl = (String)param.get("artwork");
		
		List<Map<String,Object>> artworks = new ArrayList<>();
		
		if ( artworkEl != null && artworkEl.length() > 0 ) {
			
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);    
			try {
				artworks = objectMapper.readValue(artworkEl, ArrayList.class);
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("search:"+artworks.get(0));
		
		for( int i = 0 ; i < artworks.size(); i++ ) {
			artworks.get(i).put("loginedMemberId", param.get("loginedMemberId"));
			artworkDao.doWriteArtWorkForPdProfile(artworks.get(i));
		}
	}

	public List<Artwork> getArtworksByPdId(int loginedMemberId) {
		return artworkDao.getArtworksByPdId(loginedMemberId);
	}

	public List<Artwork> getArtworksForPrint(int limit) {
		return artworkDao.getArtworksForPrint(limit);
	}

}
