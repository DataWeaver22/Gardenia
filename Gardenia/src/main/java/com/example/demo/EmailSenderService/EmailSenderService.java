package com.example.demo.EmailSenderService;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService{

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendEmailWithAttachment(String toEmail, String body, String subject, String attachment) throws MessagingException{
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
		mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
		mimeMessageHelper.setTo(toEmail);
		mimeMessageHelper.setSubject(subject);
		mimeMessageHelper.setText(body);
		
		FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
		
		mimeMessageHelper.addAttachment("Test",fileSystemResource);
		javaMailSender.send(mimeMessage);
		
		System.out.println("Mail sent successfully");
	}

}
