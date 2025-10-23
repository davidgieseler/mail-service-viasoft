package com.viasoft.mailservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailRequestDTO {
    @NotBlank
    @Email
    private String recipientEmail;

    @NotBlank
    private String recipientName;

    @NotBlank
    @Email
    private String senderEmail;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
