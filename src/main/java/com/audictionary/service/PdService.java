package com.audictionary.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.PdDao;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Pd;

@Service
public class PdService {
	@Autowired
	private PdDao pdDao;
	@Autowired
	private GenFileService genFileService;

	public int doJoin(Map<String, Object> param) {
		return pdDao.doJoin(param);
	}

	public Pd getMemberById(int memberId) {
		Pd pd = pdDao.getMemberById(memberId);
		
		if ( pd != null ) {
			
			List<GenFile> genFiles = genFileService.getGenFiles("pd", memberId, "common", "attachment");
			
			if( !genFiles.isEmpty() ) {
				pd.getExtraNotNull().put("file__common__attachment", genFiles);	
			}
			
			pd = updateForPrint(pd);
			
		}
		
		return pd;
	}
	
	public Pd updateForPrint(Pd pd) {

		GenFile genfile = genFileService.getGenFile("pd", pd.getId(), "common", "attachment", 1);
		if(genfile != null) {
			String imgUrl = genfile.getForPrintUrl();
			pd.setExtra__thumbImg(imgUrl);
		}
		
		return pd;
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

	public Pd getMemberByEmail(String email) {
		return pdDao.getMemberByEmail(email);
		
	}

	public void doModify(Map<String, Object> param) {
		pdDao.doModify(param);
	}

	public Pd getMemberByAuthKey(String authKey) {
		return pdDao.getMemberByAuthKey(authKey);
	}

	public void doDeleteMemberById(Map<String, Object> param) {
		pdDao.doDeleteMemberById(param);
	}

	public Pd doFindLoginId(Map<String, Object> param) {
		return pdDao.doFindLoginId(param);
	}

	
	public void setTempPw(String email, String tempPw) {
		pdDao.setTempPw(email,tempPw);	
	}
}
