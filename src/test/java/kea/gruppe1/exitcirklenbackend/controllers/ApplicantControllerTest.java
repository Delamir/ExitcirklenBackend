package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.DTO.ApplicantDTO;
import kea.gruppe1.exitcirklenbackend.services.EmailService;
import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantStatus;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class ApplicantControllerTest {


    @InjectMocks
    ApplicantController applicantController;

    @Mock
    EmailService emailService;

    @Mock
    ApplicantRepository applicantRepository;

    @Test
    void visitationRequest() {

        Applicant applicant = new Applicant();
        applicant.setStatus(ApplicantStatus.IKKE_VISITERET);

        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setApplicant(applicant);
        applicantDTO.setTime(LocalDateTime.now());

        HttpStatus response = applicantController.visitationRequest(applicantDTO);

        assertEquals(HttpStatus.OK, response);
        assertEquals(ApplicantStatus.I_PROCESS, applicant.getStatus());
    }

    @Test
    void cancelVisitation() {

        Applicant applicant = new Applicant();
        applicant.setStatus(ApplicantStatus.I_PROCESS);

        ApplicantDTO applicantDTO = new ApplicantDTO();
        applicantDTO.setApplicant(applicant);
        applicantDTO.setTime(LocalDateTime.now());

        HttpStatus response = applicantController.cancelVisitation(applicantDTO);

        assertEquals(HttpStatus.OK, response);
        assertEquals(ApplicantStatus.IKKE_VISITERET, applicant.getStatus());
    }

    @Test
    void confirmVisitation() {
        Applicant applicant = new Applicant();
        applicant.setStatus(ApplicantStatus.I_PROCESS);

        HttpStatus response = applicantController.confirmVisitation(applicant);

        assertEquals(HttpStatus.OK, response);
        assertEquals(ApplicantStatus.VISITERET, applicant.getStatus());
    }
}