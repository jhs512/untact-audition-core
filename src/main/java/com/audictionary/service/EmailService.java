package com.audictionary.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.audictionary.dto.Ap;
import com.audictionary.dto.Attr;
import com.audictionary.dto.ResultData;
import com.audictionary.util.Util;



@Service
public class EmailService {
	
	private String domainUrl = "pd.audictionary.com";
	
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private AttrService attrService;
	@Autowired
	private ApService apService;
	
	@Async
	public ResultData sendMailForCert(String email) throws MessagingException {
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
		 
		 StringBuilder str = new StringBuilder();
		 str.append("<a href=\"http://" + domainUrl + ":5555/usr/pd/emailCert?email="+email+"&key="+emailCertKey+"\">인증</a>");
		 helper.setText(str.toString(),true);
		 
		 try {
			 mailSender.send(msg);
			 Attr attr = attrService.get("pd", 0, "emailCertKey", email);
			 if( attr != null ) {
				 attrService.remove("pd", 0, "emailCertKey", email);
			 }
			 attrService.setValue("pd", 0, "emailCertKey", email, emailCertKey, null);
			 return new ResultData("S-1", "인증 메일이 발송되었습니다.");
		 } catch (Exception e) {
			 return new ResultData("F-1", "인증 메일 발송이 실패했습니다.");
		 }
		 
	}
	
	@Async
	public void sendMailForCertAp(String email) throws MessagingException {
		 MimeMessage msg = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		 
		 helper.setTo(email);
		 helper.setFrom("cdbitmana@gmail.com");
		 helper.setSubject("Audictionary 회원가입 인증메일입니다.");
		 
		 Ap ap = apService.getApByLoginId(email);
		 String emailCertKey = ap.getAuthKey();
		 
		 StringBuilder str = new StringBuilder();
		 str.append("<a href=\"http://ap.audictionary.com/member/emailCert?email="+email+"&emailCertKey="+emailCertKey+"\">인증</a>");
		 helper.setText(str.toString(),true);
		 mailSender.send(msg);
	}

	@Async
	public ResultData sendMailForFindLoginPw(String email, int id) throws MessagingException {
		 MimeMessage msg = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		 
		 helper.setTo(email);
		 helper.setFrom("cdbitmana@gmail.com");
		 helper.setSubject("Audictionary 비밀번호 재설정 확인 메일");
		 
		 String emailCertKey = Util.getTempPassword(50);
		 
		 String value = attrService.getValue("pd", id, "emailCertKey", email);
		 
		 if(value != null) {
			 attrService.remove("pd", id, "emailCertKey", email);
		 }
		 
		 attrService.setValue("pd", id, "emailCertKey", email, emailCertKey, null);
		 
		 StringBuilder str = new StringBuilder();
		 str.append("<a href=\"http://" + domainUrl + ":5555/usr/pd/modifyPw?email="+email+"&key="+emailCertKey+"\">비밀번호 재설정하러 가기</a>");

		 helper.setText(str.toString(),true);
		 
		 try {
			 mailSender.send(msg);
			 Attr attr = attrService.get("pd", id, "emailCertKey", email);
			 if( attr != null ) {
				 attrService.remove("pd", id, "emailCertKey", email);
			 }
			 attrService.setValue("pd", id, "emailCertKey", email, emailCertKey, null);
			 return new ResultData("S-1", "이메일이 발송되었습니다.");
		 } catch (Exception e) {
			 return new ResultData("F-1", "이메일 발송에 실패했습니다.");
		 }
		 
		
	}
	
	@Async
	public void sendMailForFindLoginPwByAp(String email, int id) throws MessagingException {
		 MimeMessage msg = mailSender.createMimeMessage();
		 MimeMessageHelper helper = new MimeMessageHelper(msg,true);
		 
		 helper.setTo(email);
		 helper.setFrom("cdbitmana@gmail.com");
		 helper.setSubject("Audictionary 비밀번호 재설정 확인 메일");
		 
		 String emailCertKey = Util.getTempPassword(50);
		 
		 String value = attrService.getValue("ap", id, "emailCertKey", email);

		 if(value != null) {
			 attrService.remove("ap", id, "emailCertKey", email);
		 }
		 
		 attrService.setValue("ap", id, "emailCertKey", email, emailCertKey, null);
		 
		 StringBuilder str = new StringBuilder();
		 str.append("<a href=\"http://ap.audictionary.com/member/modifyPw?email="+email+"&emailCertKey="+emailCertKey+"\">비밀번호 재설정하러 가기</a>");

		 helper.setText(str.toString(),true);
		 mailSender.send(msg);
		
	}
}
