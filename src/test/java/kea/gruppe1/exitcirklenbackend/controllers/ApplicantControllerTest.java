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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ApplicantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    ApplicantController applicantController;

    @Mock
    EmailService emailService;

    @Mock
    ApplicantRepository applicantRepository;

    @Test
    @WithMockUser(authorities = "ADMINISTRATOR")
    void crudTest() throws Exception {
        String body = "{ \"name\": \"Kurt\", \"userType\": 1, \"age\": 22, \"email\": \"wqerrttwtr@dsdfsdf.dfdsf\"}";
        mockMvc.perform(post("/applicants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        mockMvc.perform(get("/applicants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Kurt"))
                .andExpect(jsonPath("$.age").value("22"));

        body = "{ \"name\": \"Ole\"}";
        mockMvc.perform(patch("/applicants/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/applicants/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ole"));

        mockMvc.perform(delete("/applicants/1"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/applicants/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, result.getResponse().getContentAsString().length());
    }

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