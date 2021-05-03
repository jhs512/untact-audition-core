package com.audictionary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.audictionary.dto.Ap;

@Mapper
public interface ApDao {

	void doJoin(Map<String, Object> param);

	Ap getApByLoginId(@Param("loginId") String loginId);

	void doModify(Map<String, Object> param);

	Ap getApByAuthKey(String authKey);

	Ap getApById(int loginedMemberId);

	Ap isAdmin(int loginedMemberId);

	Ap doIdDupCheck(String loginId);

	void setAuthStatusValid(String authKey);

	List<Integer> getListByLikedApplication(Map<String, Object> param);

	Ap getListByMemberIds(int memberId);

	List<Integer> getListByLikedAp(Map<String, Object> param);

	void doLike(String relTypeCode, int relId, String memberTypeCode, int memberId);

	int isDupLike(int relId, int memberId);

	void deleteLike(int relId, int memberId);

	List<Integer> likeListsByMemberId(int memberId);

	void doDeleteMemberById(int memberId);

}
