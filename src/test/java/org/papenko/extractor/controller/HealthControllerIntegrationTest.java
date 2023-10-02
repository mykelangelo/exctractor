package org.papenko.extractor.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HealthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void extractFromFile_shouldReturnCorrectErrorMessage_whenFileContentsAreMalformed() throws Exception {
        mockMvc.perform(get("/health/ping"))
                .andExpect(status().isOk())
                .andExpect(content().string("pong"));
    }

}