package kea.gruppe1.exitcirklenbackend.email;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.models.Email;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.mail.MessagingException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

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
        group.setPrice("250");
        ArrayList<Applicant> applicants = new ArrayList<>();
        applicants.add(applicant);
        Assertions.assertDoesNotThrow(() -> emailSenderService.sendInvitations(group, applicants ));
    }
}