package com.audictionary.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.audictionary.dao.PdDao;
import com.audictionary.dto.GenFile;
import com.audictionary.dto.Pd;
import com.audictionary.dto.api.KapiKakaoCom__v2_user_me__ResponseBody;
import com.audictionary.util.Util;

@Service
public class PdService {
	@Autowired
	private PdDao pdDao;
	@Autowired
	private GenFileService genFileService;

	public int doJoin(Map<String, Object> param) {
		param.put("loginProviderTypeCode", "pd");
		
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

	public void doModifyPw(Map<String, Object> param) {
		pdDao.doModifyPw(param);
	}

	public Pd getMemberByOnLoginProviderMemberId(String loginProviderTypeCode, int onLoginProviderMemberId) {
		return pdDao.getMemberByOnLoginProviderMemberId(loginProviderTypeCode, onLoginProviderMemberId);
	}

	public void updateMember(Pd pd, KapiKakaoCom__v2_user_me__ResponseBody kakaoUser) {
		Map<String, Object> param = new HashMap<>();
		
		param.put("id", pd.getId());
		param.put("email", kakaoUser.getKakao_account().email);
		param.put("gender", kakaoUser.getKakao_account().gender);
		
		pdDao.doModify(param);
	}

	public void doJoinByKakao(KapiKakaoCom__v2_user_me__ResponseBody kakaoUser) {
		String loginProviderTypeCode = "kakao";
		int onLoginProviderMemberId = kakaoUser.id;
		
		Map<String, Object> param = Util.mapOf("loginProviderTypeCode", loginProviderTypeCode);
		param.put("onLoginProviderMemberId", onLoginProviderMemberId);

		String loginId = loginProviderTypeCode + "___" + onLoginProviderMemberId;

		param.put("loginId", loginId);
		param.put("loginPw", Util.getUUIDStr());

		param.put("email", kakaoUser.kakao_account.email);
		
		pdDao.doJoin(param);
	}

	public boolean isNeedToModify(Pd pd, Map<String,Object> param) {
		if( pd.getName() != null) {
			if (pd.getName().equals(param.get("name"))) {
				return true;
			}
		}
		if( pd.getName() != null) {
			if (pd.getAddress().equals(param.get("address"))) {
				return true;
			}
		}
		if( pd.getName() != null) {
			if (pd.getLoginPw().equals(param.get("loginPw"))) {
				return true;
			}
		}
		if( pd.getName() != null) {
			if (pd.getCellPhoneNo().equals(param.get("cellPhoneNo"))) {
				return true;
			}
		}
		if( pd.getName() != null) {
			if (pd.getJobPosition().equals(param.get("jobPosition"))) {
				return true;
			}
		}
		if( pd.getName() != null) {
			if (pd.getCorpName().equals(param.get("corpName"))) {
				return true;
			}
		}
		
		return false;
	}
}
