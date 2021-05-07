package com.audictionary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.audictionary.util.Util;



@Component
@Order(0)
class MyApplicationListener implements ApplicationListener<ApplicationReadyEvent> {
	@Value("${custom.aligo.userId}")
	private String aligoUserId;

	@Value("${custom.aligo.apiKey}")
	private String aligoApiKey;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		Util.initAligo(aligoUserId, aligoApiKey);
	}
}
