package com.buildit.common.integration;

import com.buildit.invoicing.dto.InvoiceDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.math.BigDecimal;

/**
 * Created by gerson on 04/05/17.
 */

@Configuration
public class HttpIntegration {

    @Bean
    IntegrationFlow inboundHttp() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/invoicing/invoices")
                        .requestPayloadType(InvoiceDTO.class))
                .handle("invoicingService", "processInvoice")
                .get();
    }

}
