package com.viasoft.mailservice.dto.adapter;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmailOciDTO {
    @Size(max = 40)
    private String recipientEmail;

    @Size(max = 50)
    private String recipientName;

    @Size(max = 40)
    private String senderEmail;

    @Size(max = 100)
    private String subject;

    @Size(max = 250)
    private String body;
}
