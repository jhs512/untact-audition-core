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
		
		for( int i = 0 ; i < artworks.size(); i++ ) {
			artworks.get(i).put("loginedMemberId", param.get("loginedMemberId"));
			artworkDao.doWriteArtWorkForPdProfile(artworks.get(i));
		}
	}

	public List<Artwork> getArtworksByPdId(int loginedMemberId) {
		return artworkDao.getArtworksByPdId(loginedMemberId);
	}

	public List<Artwork> getArtworksForPrint(Map<String,Object> param) {
		return artworkDao.getArtworksForPrint(param);
	}

	public List<Artwork> getArtworksForPrintByKeyword(Map<String, Object> param) {
		return artworkDao.getArtworksForPrintByKeyword(param);
	}

	public List<Artwork> getListForPrintByFilter(Map<String, Object> param) {
		String[] filters;
		
		boolean isFiltered = false;
		
		
		if(param.get("filter") != null) {
			String filter = (String)param.get("filter");
			
			if(filter.length() > 0) {
				filters = filter.split(",");
				
				for(int i = 0 ; i < filters.length; i ++) {
					if(filters[i].equals("영화")) {
						param.put("genreMv", "영화");
						isFiltered = true;
					}
					if(filters[i].equals("드라마")) {
						param.put("genreDrama", "드라마");
						isFiltered = true;
					}
					if(filters[i].equals("연극")) {
						param.put("genreTheater", "연극");
						isFiltered = true;
					}
					if(filters[i].equals("독립영화")) {
						param.put("genreIndieMv", "독립영화");
						isFiltered = true;
					}
					
					
				}
			}
			
		}
		
		param.put("isFiltered", isFiltered);
		return artworkDao.getArtworksForPrintByFilter(param);
	}



}
