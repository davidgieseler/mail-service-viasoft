package com.viasoft.mailservice.service.adapter;

import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.mapper.EmailMapper;
import com.viasoft.mailservice.model.enums.IntegrationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OciAdapterStrategyTest {

    @Mock
    private EmailMapper mockMapper;

    @InjectMocks
    private OciAdapterStrategy strategy;

    @Test
    void deveAdaptarChamandoMapperParaOci() {
        EmailRequestDTO requestDTO = new EmailRequestDTO();

        strategy.adapt(requestDTO);

        verify(mockMapper, times(1)).toOciDTO(requestDTO);
        verify(mockMapper, never()).toAwsDTO(any());
    }

    @Test
    void deveRetornarTipoIntegracaoCorreto() {
        IntegrationType type = strategy.getIntegrationType();

        assertThat(type).isEqualTo(IntegrationType.OCI);
    }
}
