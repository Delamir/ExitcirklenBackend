package kea.gruppe1.exitcirklenbackend.controllers;


import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import kea.gruppe1.exitcirklenbackend.repositories.CityRepository;
import kea.gruppe1.exitcirklenbackend.repositories.SurveyResultRepository;
import kea.gruppe1.exitcirklenbackend.services.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApplicantController.class)
@ActiveProfiles("test")
public class AuthTests {

    @Autowired
    public MockMvc mockMvc;

    @MockBean
    EmailService emailService;

    @MockBean
    ApplicantRepository applicantRepository;


    @MockBean
    SurveyResultRepository surveyResultRepository;

    @MockBean
    CityRepository cityRepository;


    @Test
    public void noUserIsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/applicants"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "VISITATOR")
    public void userHasAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/applicants"))
                .andExpect(status().isOk());
    }









}
