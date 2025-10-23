package com.viasoft.mailservice.service.adapter;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.model.enums.IntegrationType;

public interface AdapterStrategy {

    Object adapt(EmailRequestDTO request);

    IntegrationType getIntegrationType();
}