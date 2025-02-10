package com.system.lms.fo.client;

import com.system.lms.fo.common.CommonVO;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import java.io.UnsupportedEncodingException;
import java.util.Hashtable;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final CommonVO env;

    // TODO 기다리는게 나을 수도...?
    @Async
    public void sendMessage(String to, String subject, String body) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = env.googleEmailSender;  // 보내는 이메일
        String password = env.googleAppPassword;  // 실제로는 Gmail 앱 비밀번호를 사용해야 합니다

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);  // SMTP 서버
        properties.put("mail.smtp.port", port);  // 포트 (587)
        properties.put("mail.smtp.auth", "true");  // 인증 필요
        properties.put("mail.smtp.starttls.enable", "true");  // TLS 활성화

        try {
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            MimeMessage message = new MimeMessage(session);

            String senderAlias = "LMS System";
            message.setFrom(new InternetAddress(username, senderAlias));  // 발신자 이메일

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));  // 수신자 이메일
            message.setSubject(subject);  // 제목
            message.setText(body);  // 본문

            Transport.send(message);  // 이메일 전송

//          System.out.println("이메일이 성공적으로 전송되었습니다.");
        } catch (MessagingException e) {
            String className = e.getStackTrace()[0].getClassName();
            String methodName = e.getStackTrace()[0].getMethodName();

            throw new RuntimeException(className + "." + methodName + "에서 예외 발생 : " + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isValidDomain(String domain) {
        try {
            // DNS에서 MX 레코드 조회
            Hashtable<String, String> env = new Hashtable<>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
            DirContext ictx = new InitialDirContext(env);
            Attributes attrs = ictx.getAttributes(domain, new String[]{"MX"});

            // MX 레코드가 존재하는지 확인
            return attrs.get("MX") != null;
        } catch (NamingException e) {
            return false;
        }
    }
}
