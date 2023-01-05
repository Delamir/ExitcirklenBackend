package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.repositories.ApplicantRepository;
import kea.gruppe1.exitcirklenbackend.services.EmailService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    EmployeeController employeeController;

    @Mock
    EmailService emailService;

    @Mock
    ApplicantRepository applicantRepository;

    @Test
    @WithMockUser(authorities = "ADMINISTRATOR")
    void crudTest() throws Exception {
        String body = "{ \"name\": \"Kurt\", \"age\": 56," +
                "\"email\": \"wqerrttwtr@dsdfsdf.dfdsf\", \"phoneNumber\": \"44552211\", \"password\": \"x\" }";
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Kurt"))
                .andExpect(jsonPath("$.age").value("56"));

        body = "{ \"name\": \"Ole\"}";
        mockMvc.perform(patch("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ole"));

        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, result.getResponse().getContentAsString().length());

    }
}