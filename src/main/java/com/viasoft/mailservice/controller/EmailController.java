package com.viasoft.mailservice.controller;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.service.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendEmail(@Valid @RequestBody EmailRequestDTO requestDTO) {
        emailService.handleEmail(requestDTO);
    }
}