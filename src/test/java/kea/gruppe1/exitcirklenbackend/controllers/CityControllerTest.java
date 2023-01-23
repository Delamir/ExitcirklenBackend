package kea.gruppe1.exitcirklenbackend.controllers;

import kea.gruppe1.exitcirklenbackend.repositories.CityRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    CityController cityController;

    @Mock
    CityRepository cityRepository;

    @Test
    @WithMockUser(authorities = "ADMINISTRATOR")
    void crudTest() throws Exception {
        String body = "{ \"name\": \"Andeby\", \"userType\": 1, \"address\": \"Andedam 31\", \"email\": \"wqerrttwtr@dsdfsdf.dfdsf\"}";
        mockMvc.perform(post("/cities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        mockMvc.perform(get("/city/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Andeby"))
                .andExpect(jsonPath("$.address").value("Andedam 31"));

        body = "{ \"name\": \"Andeby\"}";
        mockMvc.perform(patch("/cities/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());

        mockMvc.perform(get("/city/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Andeby"));

        mockMvc.perform(delete("/cities/1"))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get("/city/1"))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(0, result.getResponse().getContentAsString().length());

    }
}
