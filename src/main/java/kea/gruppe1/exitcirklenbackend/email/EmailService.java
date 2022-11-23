package kea.gruppe1.exitcirklenbackend.email;


import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    private final boolean doSendMessage = false;


    public void sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendHtmlMessage(Email email) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(email.getProperties());
        //helper.setFrom(email.getFrom());
        helper.setTo(email.getTo());
        helper.setSubject(email.getSubject());
        String html = templateEngine.process(email.getTemplate(), context);
        helper.setText(html, true);

        log.info("Sending email: {} with html body: {}", email, html);
        if(doSendMessage)
            emailSender.send(message);
    }

    public Email createEmail(Applicant applicant) {
        Email email = new Email();
        email.setTo(applicant.getEmail());
        Map<String, Object> properties = new HashMap<>();
        properties.put("applicant", applicant);
        email.setProperties(properties);

        return email;
    }


    public void sendInvitations(ApplicantGroup group, List<Applicant> applicants) {
        for (Applicant applicant : applicants) {
            Email email = createEmail(applicant);
            email.setSubject("Exitcirklen | invitation til gruppeforløb");
            email.setTemplate("invite-email.html");
            Map<String, Object> properties = email.getProperties();
            properties.put("group", group);
            email.setProperties(properties);
            try {
                sendHtmlMessage(email);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendWelcomeEmail(Applicant applicant) {
        Email email = createEmail(applicant);
        email.setSubject("Vellkommen til Exitcirklen");
        email.setTemplate("welcome-email");

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendVisitationOfferEmail(Applicant applicant) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Visitations tilbud");
        email.setTemplate("visitation-offer-email");

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendWaitinglistConfirmationEmail(Applicant applicant) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Venteliste bekræftelse");
        email.setTemplate("waitinglist-confirmation-email");

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendGroupWelcomeEmail(Applicant applicant, ApplicantGroup group) {
        Email email = createEmail(applicant);
        email.setSubject("Velkomst");
        email.setTemplate("group-welcome-email");
        Map<String, Object> properties = email.getProperties();
        properties.put("group", group);
        email.setProperties(properties);

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendNewsletter() {

    }


}
