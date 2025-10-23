package com.viasoft.mailservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viasoft.mailservice.dto.request.EmailRequestDTO;
import com.viasoft.mailservice.exception.ConfigurationException;
import com.viasoft.mailservice.model.enums.IntegrationType;
import com.viasoft.mailservice.service.adapter.AdapterStrategy;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final IntegrationType integrationType;

    private final Map<IntegrationType, AdapterStrategy> strategyMap;

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public EmailServiceImpl(
            @Value("${mail.integracao}") IntegrationType integrationType,
            List<AdapterStrategy> strategies,
            ObjectMapper objectMapper,
            Validator validator) {

        this.integrationType = integrationType;
        this.objectMapper = objectMapper;
        this.validator = validator;

        this.strategyMap = strategies.stream()
                .collect(Collectors.toMap(AdapterStrategy::getIntegrationType, Function.identity()));
    }

    @Override
    public void handleEmail(EmailRequestDTO requestDTO) {
        logger.info("Processing email request for integration: {}", integrationType);

        AdapterStrategy strategy = strategyMap.get(integrationType);

        if (strategy == null) {
            logger.error("No Strategy implementation found for: {}", integrationType);
            throw new ConfigurationException("Service implementation not configured for: " + integrationType);
        }

        Object adaptedDTO = strategy.adapt(requestDTO);

        validateDto(adaptedDTO);
        logJson(adaptedDTO);
    }

    private void validateDto(Object dto) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            logger.warn("Failed to validate adapted DTO: {}", violations);
            throw new ConstraintViolationException(violations);
        }
    }

    private void logJson(Object dto) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);

            logger.info("\n----------------------------------\n{}\n----------------------------------", json);

        } catch (Exception e) {
            logger.error("Failed to serialize object to JSON", e);
            throw new RuntimeException("Failed to serialize object into JSON", e);
        }
    }
}
