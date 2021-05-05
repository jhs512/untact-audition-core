package com.audictionary.interceptor;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.audictionary.dto.Ap;
import com.audictionary.dto.Pd;
import com.audictionary.service.ApService;
import com.audictionary.service.PdService;

@Component("beforeActionInterceptor") // 컴포넌트 이름 설정
public class BeforeActionInterceptor implements HandlerInterceptor {
	@Autowired
	private PdService pdService;
	@Autowired
	private ApService apService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		HttpSession session = request.getSession();
		
		int loginedMemberId = 0;
		Pd loginedPd = null;
		Ap loginedAp = null;
		
		
		String authKey = request.getParameter("authKey");
		
		if ( authKey != null && authKey.length() > 0 ) {
			if ( authKey.contains("ap__authKey1__") ) {

				if (authKey != null && authKey.length() > 0) {
					
					loginedAp = apService.getApByAuthKey(authKey);
					
					if (loginedAp == null) {
						request.setAttribute("authKeyStatus", "invalid");
					} else {
						request.setAttribute("authKeyStatus", "valid");
						loginedMemberId = loginedAp.getId();
					}
				} else {
					request.setAttribute("authKeyStatus", "none");

					if (session.getAttribute("loginedMemberId") != null) {
						loginedMemberId = (int) session.getAttribute("loginedMemberId");
						loginedAp = apService.getApById(loginedMemberId);
					}
				}
				
			} else if (authKey.contains("pd__authKey1__")) {
				if (authKey != null && authKey.length() > 0) {
					
					loginedPd = pdService.getMemberByAuthKey(authKey);
					
					if (loginedPd == null) {
						request.setAttribute("authKeyStatus", "invalid");
					} else {
						request.setAttribute("authKeyStatus", "valid");
						loginedMemberId = loginedPd.getId();
					}
				} else {
					request.setAttribute("authKeyStatus", "none");

					if (session.getAttribute("loginedMemberId") != null) {
						loginedMemberId = (int) session.getAttribute("loginedMemberId");
						loginedPd = pdService.getMemberById(loginedMemberId);
					}
				}
			}
		}
		
		
		// 로그인 여부에 관련된 정보를 request에 담는다.
		boolean isLogined = false;
		boolean isAdmin = false;
		
		if (loginedPd != null) {
			isLogined = true;
			isAdmin = pdService.isAdmin(loginedMemberId);
		}
		if (loginedAp != null) {
			isLogined = true;
			isAdmin = apService.isAdmin(loginedMemberId);
		}
		
		request.setAttribute("loginedMemberId", loginedMemberId);
		request.setAttribute("isLogined", isLogined);
		request.setAttribute("isAdmin", isAdmin);
		
		if( loginedPd != null ) {
			request.setAttribute("loginedMember", loginedPd);	
		} else if ( loginedAp != null ) {
			request.setAttribute("loginedMember", loginedAp);
		}
		
		
		

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}
}
