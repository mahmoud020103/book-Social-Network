package com.mahmoud.book.email;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine springTemplateEngine;
	@Async
	public void sendEmail(
			String to,
			String username,
			EmailTemplateName emailTemplate,
			String confimationUrl,
			String activationCode,
			String subject
			) throws MessagingException {
		String templateName;
		if (emailTemplate==null) {
			templateName="config-email";
		}else {
			templateName=emailTemplate.name();
		}
		MimeMessage mimeMessage=javaMailSender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,
				MimeMessageHelper.MULTIPART_MODE_MIXED, // ضيف اسم الكلاس قبل المتغير
			    StandardCharsets.UTF_8.name());
		Map<String,Object> properties=new HashMap<>();
		properties.put("username",username);
		properties.put("confimationUrl",confimationUrl);
		properties.put("activationCode",activationCode);
		Context context=new Context();
		context.setVariables(properties);
		
		helper.setFrom("contact@mahmoud.com");
		helper.setTo(to);
		helper.setSubject(subject);
		
		String template=springTemplateEngine.process(templateName, context);
		helper.setText(template,true);
		javaMailSender.send(mimeMessage);
				

	}
}
