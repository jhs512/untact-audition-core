package com.audictionary.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.audictionary.dto.api.KapiKakaoCom__v2_user_me__ResponseBody;
import com.audictionary.dto.api.KauthKakaoCom__oauth_token__ResponseBody;
import com.audictionary.util.Util;

@Service
public class KakaoService {
	@Value("${custom.kakao.apiKeyForPd}")
	private String kakaoApiKeyForPd;
	@Value("${custom.kakao.apiKeyForAp}")
	private String kakaoApiKeyForAp;

	@Autowired
	private RestTemplateBuilder restTemplateBuilder;

	public String getAccessTokenForKakaoLogin(String code) {
		RestTemplate restTemplate = restTemplateBuilder.build();

		Map<String, String> params = Util.getNewMapStringString();
		params.put("grant_type", "authorization_code");
		params.put("client_id", kakaoApiKeyForPd);
		params.put("redirect_uri", "https://pd.audictionary.com/usr/pd/kakaoLogin");
		params.put("code", code);
		
		KauthKakaoCom__oauth_token__ResponseBody respoonseBodyRs = Util
				.getHttpPostResponseBody(new ParameterizedTypeReference<KauthKakaoCom__oauth_token__ResponseBody>() {
				}, restTemplate, "https://kauth.kakao.com/oauth/token", params, null);

		return respoonseBodyRs.access_token;
	}
	
	public String getAccessTokenForKakaoLoginAp(String code) {
		RestTemplate restTemplate = restTemplateBuilder.build();

		Map<String, String> params = Util.getNewMapStringString();
		params.put("grant_type", "authorization_code");
		params.put("client_id", kakaoApiKeyForAp);
		params.put("redirect_uri", "https://ap.audictionary.com/member/kakaoLogin");
		params.put("code", code);
		
		KauthKakaoCom__oauth_token__ResponseBody respoonseBodyRs = Util
				.getHttpPostResponseBody(new ParameterizedTypeReference<KauthKakaoCom__oauth_token__ResponseBody>() {
				}, restTemplate, "https://kauth.kakao.com/oauth/token", params, null);

		return respoonseBodyRs.access_token;
	}

	public KapiKakaoCom__v2_user_me__ResponseBody getPdByKakaoAccessToken(String accessToken) {
		RestTemplate restTemplate = restTemplateBuilder.build();

		Map<String, String> headerParams = new HashMap<>();
		headerParams.put("Authorization", "Bearer " + accessToken);

		KapiKakaoCom__v2_user_me__ResponseBody pd = Util.getHttpPostResponseBody(new ParameterizedTypeReference<KapiKakaoCom__v2_user_me__ResponseBody>() {
		}, restTemplate, "https://kapi.kakao.com/v2/user/me", null, headerParams);

		return pd;
	}

}
