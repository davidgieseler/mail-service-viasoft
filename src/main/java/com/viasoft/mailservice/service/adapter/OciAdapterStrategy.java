package com.viasoft.mailservice.service.adapter;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.mapper.EmailMapper;
import com.viasoft.mailservice.model.enums.IntegrationType;
import org.springframework.stereotype.Component;

@Component
public class OciAdapterStrategy implements AdapterStrategy {

    private final EmailMapper mapper;

    public OciAdapterStrategy(EmailMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object adapt(EmailRequestDTO request) {
        return mapper.toOciDTO(request);
    }

    @Override
    public IntegrationType getIntegrationType() {
        return IntegrationType.OCI;
    }
}