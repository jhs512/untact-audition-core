package com.audictionary.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;



@Service
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	
	@Async
	public void sendMail(String email) throws MessagingException {
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
		 helper.setText("<a href=\"http://localhost:8100/usr/pd/cert?email="+email+"\">인증</a>", true);
		 mailSender.send(msg);
	}
}
