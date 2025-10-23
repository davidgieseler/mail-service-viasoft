package com.viasoft.mailservice.mapper;

import com.viasoft.mailservice.dto.adapter.EmailAwsDTO;
import com.viasoft.mailservice.dto.adapter.EmailOciDTO;
import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Mapping(source = "recipientEmail", target = "recipient")
    @Mapping(source = "recipientName", target = "recipientName")
    @Mapping(source = "senderEmail", target = "sender")
    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "content", target = "content")
    EmailAwsDTO toAwsDTO(EmailRequestDTO requestDTO);

    @Mapping(source = "recipientEmail", target = "recipientEmail")
    @Mapping(source = "recipientName", target = "recipientName")
    @Mapping(source = "senderEmail", target = "senderEmail")
    @Mapping(source = "subject", target = "subject")
    @Mapping(source = "content", target = "body")
    EmailOciDTO toOciDTO(EmailRequestDTO requestDTO);
}
