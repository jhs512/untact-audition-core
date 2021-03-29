package com.audictionary.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	
	// CORS 허용
		@Override
		public void addCorsMappings(CorsRegistry registry) {
			registry.addMapping("/**");
		}
	
	// beforeActionInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("beforeActionInterceptor")
	HandlerInterceptor beforeActionInterceptor;
	
	// needAdminInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needAdminInterceptor")
	HandlerInterceptor needAdminInterceptor;

	// needLoginInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLoginInterceptor")
	HandlerInterceptor needLoginInterceptor;

	// needLogoutInterceptor 인터셉터 불러오기
	@Autowired
	@Qualifier("needLogoutInterceptor")
	HandlerInterceptor needLogoutInterceptor;

	// 이 함수는 인터셉터를 적용하는 역할을 합니다.
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// beforeActionInterceptor 인터셉터가 모든 액션 실행전에 실행되도록 처리
		registry.addInterceptor(beforeActionInterceptor).addPathPatterns("/**").excludePathPatterns("/resource/**");
		
		// 어드민 필요
		registry.addInterceptor(needAdminInterceptor)
			.addPathPatterns("/adm/**")
			.excludePathPatterns("/adm/member/login")
			.excludePathPatterns("/adm/member/doLogin");

		// 로그인 필요
		registry.addInterceptor(needLoginInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/")
			.excludePathPatterns("/adm/**")
			.excludePathPatterns("/resource/**")
			.excludePathPatterns("/usr/home/main")
			.excludePathPatterns("/usr/pd/doLogin")
			.excludePathPatterns("/usr/pd/doJoin")
			.excludePathPatterns("/usr/pd/emailCert")
			.excludePathPatterns("/usr/pd/cert")
			.excludePathPatterns("/usr/ap/getAuthKey")
			.excludePathPatterns("/usr/pd/doFindLoginId")
			.excludePathPatterns("/usr/pd/doFindLoginPw")
			.excludePathPatterns("/usr/ap/doLogin")
			.excludePathPatterns("/usr/ap/doJoin")
			.excludePathPatterns("/usr/recruit/list")
			.excludePathPatterns("/usr/file/test*")
			.excludePathPatterns("/usr/file/doTest*")
			.excludePathPatterns("/common/**")
			.excludePathPatterns("/test/**")
			.excludePathPatterns("/error");
		 
		// 로그인 상태에서 접속할 수 없는 URI 전부 기술
		registry.addInterceptor(needLogoutInterceptor)
		    .addPathPatterns("/adm/member/login")
		    .addPathPatterns("/adm/member/doLogin")
			.addPathPatterns("/usr/member/login")
			.addPathPatterns("/usr/member/doLogin")
			.addPathPatterns("/usr/member/join")
			.addPathPatterns("/usr/member/doJoin");
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/gen/**").addResourceLocations("file:///" + genFileDirPath + "/")
				.setCachePeriod(20);
	}
}