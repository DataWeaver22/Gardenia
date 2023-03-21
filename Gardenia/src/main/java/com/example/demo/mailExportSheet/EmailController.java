package com.example.demo.mailExportSheet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.poi.poifs.nio.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.EmailSenderService.EmailSenderService;
import com.example.demo.Export.CountryExportExcel;
import com.example.demo.entity.Country;
import com.example.demo.repository.CountryRepository;

@RestController
@RequestMapping("/mail/test")
public class EmailController {

//	@Autowired
//	private EmailSenderService emailSenderService;
//	
//	CountryRepository countryRepository;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_MIS','ROLE_USER','ROLE_RSM')")
	private ResponseEntity<String> mailTest(){
		String fileNameString = "Test";
		//List<Country> countries = countryRepository.findAll();
		ByteArrayInputStream in = EmailTest.countriesToExcel();
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setFrom("bhavikdesai1717@gmail.com");
			mimeMessageHelper.setTo("itdeveloper.business17@gmail.com");
			mimeMessageHelper.setSubject("Test");
			mimeMessageHelper.setText("Test");
			
			//FileSystemResource fileSystemResource = new FileSystemResource(new File(attachment));
			
//			try {
//				mimeMessageHelper.addAttachment("Test",  new ByteArrayResource(IOUtils.toByteArray(in)));
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		javaMailSender.send(mimeMessage);
		
		return ResponseEntity.ok("Mail Sent");
	}
	
}
