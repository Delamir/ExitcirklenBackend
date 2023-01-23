package kea.gruppe1.exitcirklenbackend.email;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.services.EmailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

import java.time.LocalDateTime;
import java.util.ArrayList;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailSenderService;

    @Test
    public void sendHtmlMessageTest() throws MessagingException {
        Applicant applicant = new Applicant();
        applicant.setName("test");
        applicant.setEmail("td_christian@hotmail.com");
        ApplicantGroup group = new ApplicantGroup();
        group.setAddress("gade 1, 1000 by, land");
        group.setStartDate(LocalDateTime.now());
        group.setPrice(250);
        ArrayList<Applicant> applicants = new ArrayList<>();
        applicants.add(applicant);
        Assertions.assertDoesNotThrow(() -> emailSenderService.sendInvitations(group, applicants ));
    }

    @Test
    void sendWelcomeEmail() {
        Applicant applicant = new Applicant();
        applicant.setName("test");
        applicant.setEmail("td_christian@hotmail.com");
        Assertions.assertDoesNotThrow(() -> emailSenderService.sendWelcomeEmail(applicant ));

    }

    @Test
    void sendGroupWelcomeEmail() {
        Applicant applicant = new Applicant();
        applicant.setName("Sugesen");
        applicant.setEmail("td_christian@hotmail.com");
        ApplicantGroup group = new ApplicantGroup();
        group.setDescription("Hej, jeg er Patrick og kommer til at være din beste ven efter mit mirakel gruppeforløb der kan kurere kræft, hoste, homosexualitet, pest, kolera, covid-19, covid-20, gnavenhed og alt andet. Så du er super heldig og vær klar til at komme ind i nirvana, og husk hvis man subscriber til min patreon så bliver du højere, flottere og alle vil kunne lige dig");
        group.setStartDate(LocalDateTime.now());
        group.setAddress("kiosken på nørregade");

        Assertions.assertDoesNotThrow(() -> emailSenderService.sendGroupWelcomeEmail(applicant,group ));

    }

    @Test
    void sendVisitationOfferEmail() {
        Applicant applicant = new Applicant();
        applicant.setName("Sugesen");
        applicant.setEmail("td_christian@hotmail.com");

        LocalDateTime time = LocalDateTime.now();


        Assertions.assertDoesNotThrow(() -> emailSenderService.sendVisitationOfferEmail(applicant, time));
    }
}