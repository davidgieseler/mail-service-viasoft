package com.viasoft.mailservice.service;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;

public interface EmailService {
    void handleEmail(EmailRequestDTO requestDTO);
}
