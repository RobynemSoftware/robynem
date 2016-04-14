package com.robynem.mit.web.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created by robyn_000 on 12/04/2016.
 */
public class SmtpHelper {

    private String from;

    private String fromName;

    private String to;

    private String subject;

    private String contentFileName;

    private Map<String, String> parameters;

    private JavaMailSender javaMailSender;

    private Locale locale;


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContentFileName() {
        return contentFileName;
    }

    public void setContentFileName(String contentFileName) {
        this.contentFileName = contentFileName;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void send() {
        final SmtpHelper $this = this;

        // Sends email async
        Runnable task = () -> {
            this.javaMailSender.send(new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws MessagingException {
                    try {
                        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                        message.setFrom($this.from, $this.fromName);
                        message.setTo($this.to);
                        message.setSubject($this.subject);
                        message.setText($this.getText(), true);
                    } catch (Exception e) {
                        throw new MessagingException(e.getMessage(), e);
                    }

                }
            });
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    private String getText() throws Exception{
        String text = "";

        ClassPathResource classPathResource = new ClassPathResource(String.format("emails/%s", this.contentFileName));

        StringWriter writer = new StringWriter();
        IOUtils.copy(classPathResource.getInputStream(), writer, "UTF-8");

        text = writer.toString();

        if (this.parameters != null) {

            Iterator<String> iter = this.parameters.keySet().iterator();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = this.parameters.get(key);

                text = StringUtils.replace(text, key, value);
            }
        }

        return text;
    }
}
