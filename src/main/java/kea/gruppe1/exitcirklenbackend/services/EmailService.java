package kea.gruppe1.exitcirklenbackend.services;


import com.azure.identity.ClientSecretCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.models.*;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.UserCollectionPage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmailService {


    @Autowired
    private GraphService graphService;
    @Autowired
    private SpringTemplateEngine templateEngine;

    private final boolean doSendMessage = true;



    /**
     * Helps to configure an email as a html message
     *
     * @param email the email object that is configured
     */
    public void sendHtmlMessage(Email email, OAuth2AuthorizedClient graph, boolean sendFromCurrentUser) throws MessagingException {

        Context context = new Context();
        context.setVariables(email.getProperties());

        Message message = new Message();
        message.subject = email.getSubject();
        ItemBody body = new ItemBody();
        body.contentType = BodyType.HTML;
        body.content = templateEngine.process(email.getTemplate(), context);
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = email.getTo();
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        LinkedList<InternetMessageHeader> internetMessageHeadersList = new LinkedList<InternetMessageHeader>();
        InternetMessageHeader internetMessageHeaders = new InternetMessageHeader();
        internetMessageHeaders.name = "x-custom-header-group-name";
        internetMessageHeaders.value = "Exitcirklen";
        internetMessageHeadersList.add(internetMessageHeaders);
        //InternetMessageHeader internetMessageHeaders1 = new InternetMessageHeader();
        //internetMessageHeaders1.name = "x-custom-header-group-id";
        //internetMessageHeaders1.value = "NV001";
        //internetMessageHeadersList.add(internetMessageHeaders1);
        message.internetMessageHeaders = internetMessageHeadersList;


        log.info("Sending email: {} with html body: {}", email, templateEngine.process(email.getTemplate(), context));
        if(!doSendMessage)
            return;

        if (sendFromCurrentUser) {
            GraphServiceClient graphServiceClient = graphService.getGraphServiceClient(graph);
            graphServiceClient.me()
                    .sendMail(UserSendMailParameterSet
                            .newBuilder()
                            .withMessage(message)
                            .withSaveToSentItems(true)
                            .build())
                    .buildRequest()
                    .post();
        }else{
            GraphServiceClient graphServiceClient = graphService.getAppGraphServiceClient();
            graphServiceClient.users("0de2025a-b329-42af-9305-970e89fefeb2")
                    .sendMail(UserSendMailParameterSet
                            .newBuilder()
                            .withMessage(message)
                            .withSaveToSentItems(true)
                            .build())
                    .buildRequest()
                    .post();

        }

    }

    /**
     * Creates an email template with data from an applicant
     *
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
     *
     * @param group      the specific applicant group
     * @param applicants the applicants tha needs to receive the email
     */
    public void sendInvitations(ApplicantGroup group, List<Applicant> applicants, OAuth2AuthorizedClient graph) {
        for (Applicant applicant : applicants) {
            Email email = createEmail(applicant);
            email.setSubject("Exitcirklen | invitation til gruppeforløb");
            email.setTemplate("invite-email.html");
            Map<String, Object> properties = email.getProperties();
            properties.put("group", group);
            email.setProperties(properties);
            try {
                sendHtmlMessage(email, graph, false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends a welcome email to a specific applicant
     *
     * @param applicant the specified applicant
     */
    public void sendWelcomeEmail(Applicant applicant, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Vellkommen til Exitcirklen");
        email.setTemplate("welcome-email");

        try {
            sendHtmlMessage(email, graph, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email with a visitation offer
     *
     * @param applicant the receiving applicant
     * @param time      the time of the visitation
     */
    public void sendVisitationOfferEmail(Applicant applicant, LocalDateTime time, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Visitations tilbud");
        email.setTemplate("visitation-offer-email");
        Map<String, Object> properties = email.getProperties();
        properties.put("time", time);

        try {
            sendHtmlMessage(email, graph, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an email that cancels a visitation
     *
     * @param applicant the receiving applicant
     * @param reason    the reason for the cancellation
     */
    public void sendCancelVisitationEmail(Applicant applicant, String reason, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Aflysning af visistering");
        email.setTemplate("cancel-visitation-email");

        Map<String, Object> properties = email.getProperties();
        properties.put("reason", reason);

        try {
            sendHtmlMessage(email, graph, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a confirmation email on a visitation
     *
     * @param applicant the receiving applicant
     */
    public void sendConfirmationVisitationEmail(Applicant applicant, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Bekræftelse af visistering");
        email.setTemplate("confirmation-visitation-email");

        try {
            sendHtmlMessage(email, graph, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a confirmation that an applicant is now on a waiting list
     *
     * @param applicant the receiving applicant
     */
    public void sendWaitinglistConfirmationEmail(Applicant applicant, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Exitcirklen | Venteliste bekræftelse");
        email.setTemplate("waitinglist-confirmation-email");

        try {
            sendHtmlMessage(email, graph, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a welcome mail to an applicant group
     *
     * @param applicant the applicant that has joined the group
     * @param group     the group of which the applicant has joined
     */
    public void sendGroupWelcomeEmail(Applicant applicant, ApplicantGroup group, OAuth2AuthorizedClient graph) {
        Email email = createEmail(applicant);
        email.setSubject("Velkomst");
        email.setTemplate("group-welcome-email");
        Map<String, Object> properties = email.getProperties();
        properties.put("group", group);
        email.setProperties(properties);

        try {
            sendHtmlMessage(email, graph, false);
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
