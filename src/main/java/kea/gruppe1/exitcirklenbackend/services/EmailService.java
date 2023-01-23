package kea.gruppe1.exitcirklenbackend.services;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
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

    private final boolean doSendMessage = true;

    /**
     * Helps to configure an email as a html message
     * @param email the email object that is configured
     */
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

    /**
     * Creates an email template with data from an applicant
     * @param applicant the applicant object with wanted data
     * @return an email object
     */
    public Email createEmail(Applicant applicant) {
        Email email = new Email();
        email.setTo(applicant.getEmail());
        Map<String, Object> properties = new HashMap<>();
        properties.put("applicant", applicant);
        email.setProperties(properties);

        return email;
    }

    /**
     * Sends an email invitation to an applicant group
     * @param group the specific applicant group
     * @param applicants the applicants tha needs to receive the email
     */
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

    /**
     * Sends a welcome email to a specific applicant
     * @param applicant the specified applicant
     */
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

    /**
     * Sends an email with a visitation offer
     * @param applicant the receiving applicant
     * @param time the time of the visitation
     */
    public void sendVisitationOfferEmail(Applicant applicant, LocalDateTime time) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Visitations tilbud");
        email.setTemplate("visitation-offer-email");
        Map<String, Object> properties = email.getProperties();
        properties.put("time", time);

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email that cancels a visitation
     * @param applicant the receiving applicant
     * @param reason the reason for the cancellation
     */
    public void sendCancelVisitationEmail(Applicant applicant, String reason) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Aflysning af visistering");
        email.setTemplate("cancel-visitation-email");

        Map<String, Object> properties = email.getProperties();
        properties.put("reason", reason);

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a confirmation email on a visitation
     * @param applicant the receiving applicant
     */
    public void sendConfirmationVisitationEmail(Applicant applicant) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Bekræftelse af visistering");
        email.setTemplate("confirmation-visitation-email");

        try {
            sendHtmlMessage(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a confirmation that an applicant is now on a waiting list
     * @param applicant the receiving applicant
     */
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

    /**
     * Sends a welcome mail to an applicant group
     * @param applicant the applicant that has joined the group
     * @param group the group of which the applicant has joined
     */
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

    /**
     * Sends an email with a newsletter
     */
    //TODO
    public void sendNewsletter() {
        Email email = new Email();
        email.setSubject("");
    }
}
