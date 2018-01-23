package com.bt.om.util;

import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.apache.http.Consts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.bt.om.vo.common.MailVo;

public class MailUtil {

    private static final Logger logger         = LoggerFactory.getLogger(MailUtil.class);

    private static final String DEFAULT_ENCODE = Consts.UTF_8.name();

    private static Properties props            = new Properties();
    
    static {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.timeout", "25000");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }
    
    /**
     * send email
     */
    public static void sendEmail(MailVo vo) {
        try {
            JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
            senderImpl.setHost(vo.getHost());
            MimeMessage mailMessage = senderImpl.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, DEFAULT_ENCODE);
            messageHelper.setFrom(vo.getFrom());
            messageHelper.setTo(vo.getTo());
            messageHelper.setSubject(vo.getSubject());
            messageHelper.setText(vo.getContent(), true);
            MyAuthenticator auth = new MyAuthenticator(vo.getAccount(), vo.getPassword());
            Session session = Session.getDefaultInstance(props, auth);
            senderImpl.setSession(session);
            senderImpl.send(mailMessage);
            logger.info(MessageFormat.format("Send monitor email success, email text:{0},", vo));
        } catch (MessagingException e) {
            logger.error(MessageFormat.format("Send monitor email failure, email text:{0},", vo), e);
        }

    }

}
