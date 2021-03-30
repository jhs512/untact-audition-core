package com.audictionary.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.audictionary.util.Util;



@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AttrService attrService;
	
	@Async
	public void sendMailForCert(String email) throws MessagingException {
		/* 간단한 텍스트로만 메일 보내기
		SimpleMailMessage simpleMessage = new SimpleMailMessage();
		simpleMessage.setFrom("cdbitmana@gmail.com"); // NAVER, DAUM, NATE일 경우 넣어줘야 함
		simpleMessage.setTo("cdbitmana@gmail.com");
		simpleMessage.setSubject("이메일 인증");
		simpleMessage.setText("인증번호: 123456");
		javaMailSender.send(simpleMessage);
		*/
		
		 MimeMessage msg = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		 
		 helper.setTo(email);
		 helper.setFrom("cdbitmana@gmail.com");
		 helper.setSubject("Audictionary 회원가입 인증메일입니다.");
		 
		 String emailCertKey = Util.getTempPassword(50);
		 
		 attrService.setValue("pd", 0, "emailCertKey", email, emailCertKey, null);
		 
		 StringBuilder str = new StringBuilder();
		 str.append("<a href=\"http://localhost:8100/usr/pd/emailCert?email="+email+"&key="+emailCertKey+"\">인증</a>");
		 helper.setText(str.toString(),true);
		 mailSender.send(msg);
	}
}
