package com.viasoft.mailservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.viasoft.mailservice.dto.adapter.EmailAwsDTO;
import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.exception.ConfigurationException;
import com.viasoft.mailservice.model.enums.IntegrationType;
import com.viasoft.mailservice.service.adapter.AdapterStrategy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class EmailServiceImplTest {

    @Mock
    private AdapterStrategy awsStrategy;
    @Mock
    private AdapterStrategy ociStrategy;
    @Mock
    private Validator validator;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private ObjectWriter objectWriter;

    private EmailService emailService;
    private EmailRequestDTO requestDTO;

@BeforeEach
void setUp() {
    when(awsStrategy.getIntegrationType()).thenReturn(IntegrationType.AWS);
    when(ociStrategy.getIntegrationType()).thenReturn(IntegrationType.OCI);

    requestDTO = new EmailRequestDTO(); // DTO de entrada genérico

    try {
        when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(objectWriter);
        when(objectWriter.writeValueAsString(any())).thenReturn("{}");
    } catch (Exception e) {

    }
}

    private void instanciarServico(IntegrationType tipoConfigurado, List<AdapterStrategy> strategiesDisponiveis) {
        emailService = new EmailServiceImpl(
                tipoConfigurado,
                strategiesDisponiveis,
                objectMapper,
                validator
        );
    }

    @Test
    void deveProcessarComSucessoEstrategiaAws() throws Exception {
        instanciarServico(IntegrationType.AWS, List.of(awsStrategy, ociStrategy));
        EmailAwsDTO dtoAdaptado = new EmailAwsDTO();

        when(awsStrategy.adapt(requestDTO)).thenReturn(dtoAdaptado);
        when(validator.validate(dtoAdaptado)).thenReturn(Collections.emptySet());

        emailService.handleEmail(requestDTO);

        verify(awsStrategy).adapt(requestDTO);
        verify(ociStrategy, never()).adapt(any());
        verify(validator).validate(dtoAdaptado);
        verify(objectWriter).writeValueAsString(dtoAdaptado);
    }

    @Test
    void deveProcessarComSucessoEstrategiaOci() throws Exception {
        instanciarServico(IntegrationType.OCI, List.of(awsStrategy, ociStrategy));
        Object dtoAdaptado = new Object();

        when(ociStrategy.adapt(requestDTO)).thenReturn(dtoAdaptado);
        when(validator.validate(dtoAdaptado)).thenReturn(Collections.emptySet());

        emailService.handleEmail(requestDTO);

        verify(ociStrategy).adapt(requestDTO);
        verify(awsStrategy, never()).adapt(any());
        verify(validator).validate(dtoAdaptado);
        verify(objectWriter).writeValueAsString(dtoAdaptado);
    }

    @Test
    void deveLancarExcecaoDeValidacaoSeDtoAdaptadoForInvalido() {
        instanciarServico(IntegrationType.AWS, List.of(awsStrategy, ociStrategy));
        EmailAwsDTO dtoAdaptado = new EmailAwsDTO();

        when(awsStrategy.adapt(requestDTO)).thenReturn(dtoAdaptado);

        Set<ConstraintViolation<EmailAwsDTO>> violations = Collections.singleton(mock(ConstraintViolation.class));
        when(validator.validate(dtoAdaptado)).thenReturn(violations);

        assertThatThrownBy(() -> emailService.handleEmail(requestDTO))
                .isInstanceOf(ConstraintViolationException.class);

        verify(objectMapper, never()).writerWithDefaultPrettyPrinter();
    }

    @Test
    void deveLancarExcecaoDeConfiguracaoSeStrategyNaoEncontrada() {
        instanciarServico(IntegrationType.AWS, List.of(ociStrategy));

        assertThatThrownBy(() -> emailService.handleEmail(requestDTO))
                .isInstanceOf(ConfigurationException.class)
                .hasMessageContaining("Service implementation not configured for: AWS");

    }
}