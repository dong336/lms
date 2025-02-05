package com.system.lms.fo.client;

import com.system.lms.fo.common.Env;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {

    private final Env env;

    public void sendMessage(String to, String subject, String body) {
        // SMTP 서버 설정 (Gmail 예시)
        String host = "smtp.gmail.com";
        String port = "587";
        String username = env.googleEmailSender;  // 보내는 이메일
        String password = env.googleAppPassword;  // 실제로는 Gmail 앱 비밀번호를 사용해야 합니다

        // 메일 전송 설정
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);  // SMTP 서버
        properties.put("mail.smtp.port", port);  // 포트 (587)
        properties.put("mail.smtp.auth", "true");  // 인증 필요
        properties.put("mail.smtp.starttls.enable", "true");  // TLS 활성화

        try {
            // 인증 정보와 함께 Session 객체를 생성
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // MimeMessage 객체를 생성하여 이메일 내용 작성
            MimeMessage message = new MimeMessage(session);

            String senderAlias = "LMS System";
            message.setFrom(new InternetAddress(username, senderAlias));  // 발신자 이메일

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));  // 수신자 이메일
            message.setSubject(subject);  // 제목
            message.setText(body);  // 본문

            // 이메일 전송
            Transport.send(message);  // 이메일 전송

//          System.out.println("이메일이 성공적으로 전송되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        // SMTP 서버 설정 (Gmail 예시)
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "dong094724@gmail.com";  // 보내는 이메일
        String password = "lccl tnps nonf yhpl";    // 실제로는 Gmail 앱 비밀번호를 사용해야 합니다

        // 받는 사람, 제목, 본문 내용 설정
        String to = "sdfgh7221314@naver.com";  // 받는 이메일
        String subject = "테스트 이메일";
        String body = "이것은 매우 간단한 이메일 보내기 테스트입니다.";

        // 메일 전송 설정
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);  // SMTP 서버
        properties.put("mail.smtp.port", port);  // 포트 (587)
        properties.put("mail.smtp.auth", "true");  // 인증 필요
        properties.put("mail.smtp.starttls.enable", "true");  // TLS 활성화

        try {
            // 인증 정보와 함께 Session 객체를 생성
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // MimeMessage 객체를 생성하여 이메일 내용 작성
            MimeMessage message = new MimeMessage(session);

            String senderAlias = "Test Company";
            message.setFrom(new InternetAddress(username, senderAlias));  // 발신자 이메일

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));  // 수신자 이메일
            message.setSubject(subject);  // 제목
            message.setText(body);  // 본문

            // 이메일 전송
            Transport.send(message);  // 이메일 전송

            System.out.println("이메일이 성공적으로 전송되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
