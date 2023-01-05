package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.models.Applicant;
import kea.gruppe1.exitcirklenbackend.models.ApplicantGroup;
import kea.gruppe1.exitcirklenbackend.repositories.ApplicantGroupRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicantGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    ApplicantGroupController applicantGroupController;

    @Mock
    ApplicantGroupRepository applicantGroupRepository;

    @Test
    @WithMockUser(authorities = "ADMINISTRATOR")
    void crudTest() throws Exception {
        String body = "{ \"name\": \"Gruppe 1\", \"address\": \"Andedam 1\", \"groupsize\": 12, " +
                "\"price\": 300, \"discount\": \"true\", \"tags\": \"Mand\", \"description\": \"En test gruppe\" }";
        mockMvc.perform(post("/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Gruppe 1"))
                .andExpect(jsonPath("$.price").value("300"));

        body = "{ \"name\": \"Gruppe 2\"}";
        mockMvc.perform(patch("/groups/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gruppe 2"));

        mockMvc.perform(delete("/groups/1"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/groups/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, result.getResponse().getContentAsString().length());
    }

    @Test
    void availableCount() {
        ApplicantGroup applicantGroup = new ApplicantGroup();
        applicantGroup.setId(1L);
        applicantGroup.setGroupSize(24);
        when(applicantGroupRepository.findById(1L)).thenReturn(Optional.of(applicantGroup));
        assertEquals(24, applicantGroupController.availableCount(1L));

        Applicant applicant = new Applicant();
        applicantGroup.addToInviteList(applicant);
        assertEquals(23, applicantGroupController.availableCount(1L));

        applicantGroup.removeFromInviteList(applicant);
        assertEquals(24, applicantGroupController.availableCount(1L));
    }
}
