package com.buildit.common.integration;

import com.buildit.invoicing.application.dto.InvoiceDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;

/**
 * Created by gerson on 04/05/17.
 */

@Configuration
public class HttpIntegration {

    @Bean
    IntegrationFlow inboundHttp() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/invoicing/invoices").requestPayloadType(InvoiceDTO.class))
                .handle("invoicingService", "processInvoice")
                .get();
    }

}
