package com.viasoft.mailservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.exception.ConfigurationException;
import com.viasoft.mailservice.service.EmailService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmailController.class)
public class EmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EmailService emailService;

    private EmailRequestDTO validRequestDTO;

    @BeforeEach
    void setUp() {
        validRequestDTO = new EmailRequestDTO();
        validRequestDTO.setRecipientEmail("recipient@example.com");
        validRequestDTO.setRecipientName("Recipient Name");
        validRequestDTO.setSenderEmail("sender@example.com");
        validRequestDTO.setSubject("Test");
        validRequestDTO.setContent("Body Email");
    }

    @Test
    void deveRetornarStatus204QuandoSucesso() throws Exception {
        doNothing().when(emailService).handleEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarStatus400QuandoValidacaoDeEntradaFalha() throws Exception {
        EmailRequestDTO invalidRequestDTO = new EmailRequestDTO();
        invalidRequestDTO.setRecipientEmail("email-invalid");
        invalidRequestDTO.setRecipientName("Name");
        invalidRequestDTO.setSenderEmail("email@test.com");
        invalidRequestDTO.setSubject("Subject");
        invalidRequestDTO.setContent("Email Content");

        mockMvc.perform(post("/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.recipientEmail").exists());
    }

    @Test
    void deveRetornarStatus400QuandoValidacaoDeAdaptacaoFalha() throws Exception {
        doThrow(new ConstraintViolationException(Collections.emptySet()))
                .when(emailService).handleEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation error"));
    }

    @Test
    void deveRetornarStatus500QuandoErroDeConfiguracao() throws Exception {
        doThrow(new ConfigurationException("Invalid configuration: 'mail.integracao'"))
                .when(emailService).handleEmail(any(EmailRequestDTO.class));

        mockMvc.perform(post("/email/send")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequestDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Invalid configuration: 'mail.integracao'"));
    }
}