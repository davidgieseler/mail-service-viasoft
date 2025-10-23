package com.viasoft.mailservice.mapper;

import com.viasoft.mailservice.dto.adapter.EmailAwsDTO;
import com.viasoft.mailservice.dto.adapter.EmailOciDTO;
import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailMapperTest {

    private EmailMapper emailMapper;
    private EmailRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        emailMapper = Mappers.getMapper(EmailMapper.class);

        requestDTO = new EmailRequestDTO();
        requestDTO.setRecipientEmail("recipient@example.com");
        requestDTO.setRecipientName("Name Recipient");
        requestDTO.setSenderEmail("sender@example.com");
        requestDTO.setSubject("Test");
        requestDTO.setContent("Body email");
    }

    @Test
    void deveMapearCorretamenteParaAwsDto() {
        EmailAwsDTO awsDTO = emailMapper.toAwsDTO(requestDTO);

        assertThat(awsDTO).isNotNull();
        assertThat(awsDTO.getRecipient()).isEqualTo(requestDTO.getRecipientEmail());
        assertThat(awsDTO.getRecipientName()).isEqualTo(requestDTO.getRecipientName());
        assertThat(awsDTO.getSender()).isEqualTo(requestDTO.getSenderEmail());
        assertThat(awsDTO.getSubject()).isEqualTo(requestDTO.getSubject());
        assertThat(awsDTO.getContent()).isEqualTo(requestDTO.getContent());
    }

    @Test
    void deveMapearCorretamenteParaOciDto() {
        EmailOciDTO ociDTO = emailMapper.toOciDTO(requestDTO);

        assertThat(ociDTO).isNotNull();
        assertThat(ociDTO.getRecipientEmail()).isEqualTo(requestDTO.getRecipientEmail());
        assertThat(ociDTO.getRecipientName()).isEqualTo(requestDTO.getRecipientName());
        assertThat(ociDTO.getSenderEmail()).isEqualTo(requestDTO.getSenderEmail());
        assertThat(ociDTO.getSubject()).isEqualTo(requestDTO.getSubject());
        assertThat(ociDTO.getBody()).isEqualTo(requestDTO.getContent());
    }
}