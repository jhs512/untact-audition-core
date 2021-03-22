package com.audictionary.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.PdDao;
import com.audictionary.dto.Pd;

@Service
public class PdService {
	@Autowired
	private PdDao pdDao;

	public int doJoin(Map<String, Object> param) {
		return pdDao.doJoin(param);
	}

	public Pd getMemberById(int loginedMemberId) {
		return pdDao.getMemberById(loginedMemberId);
	}

	public boolean isAdmin(int loginedMemberId) {
		Pd pd = pdDao.isAdmin(loginedMemberId);
		
		if ( pd != null ) {
			return true;
		}
		else {
			return false;
		}
	}

	public Pd getMemberByEmail(Map<String, Object> param) {
		return pdDao.getMemberByEmail(param);
		
	}

	public void doModify(Map<String, Object> param) {
		pdDao.doModify(param);
	}

	public Pd getMemberByAuthKey(String authKey) {
		return pdDao.getMemberByAuthKey(authKey);
	}

	public void doDeleteMemberById(int id) {
		pdDao.doDeleteMemberById(id);
	}

	public Pd doFindLoginId(Map<String, Object> param) {
		return pdDao.doFindLoginId(param);
	}

	
	public void setTempPw(String email, String tempPw) {
		pdDao.setTempPw(email,tempPw);	
	}
}
