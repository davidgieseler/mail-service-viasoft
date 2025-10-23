package com.viasoft.mailservice.service.adapter;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.mapper.EmailMapper;
import com.viasoft.mailservice.model.enums.IntegrationType;
import org.springframework.stereotype.Component;

@Component
public class AwsAdapterStrategy implements AdapterStrategy {

    private final EmailMapper mapper;

    public AwsAdapterStrategy(EmailMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object adapt(EmailRequestDTO request) {
        return mapper.toAwsDTO(request);
    }

    @Override
    public IntegrationType getIntegrationType() {
        return IntegrationType.AWS;
    }
}