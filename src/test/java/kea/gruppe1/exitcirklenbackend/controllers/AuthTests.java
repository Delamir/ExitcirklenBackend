package kea.gruppe1.exitcirklenbackend.controllers;


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

  //  @Autowired
  //  private TestEntityManager testEntityManager;

    @Test
    public void noUserIsUnauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/applicants"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "VISITATOR")
    public void userHasLimitedAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/applicants"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/applicants/1"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(authorities = "ADMINISTRATOR")
    public void adminHasFullAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/applicants"))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.delete("/applicants/1"))
                .andExpect(status().isOk());

    }









}
