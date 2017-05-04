package com.buildit.common.integration;

import com.buildit.common.domain.Invoice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpMethod;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.scripting.Scripts;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.GenericMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gerson on 04/05/17.
 */

@MessagingGateway
interface  HttpIntegrationService {
    @Gateway(requestChannel = "requestChannel", replyChannel = "replyChannel")
    Object getInvoice(@Payload String id, @Header BigDecimal price, @Header String phr);
}

@Configuration
public class HttpIntegration {
    @Bean
    IntegrationFlow inboundHttpGateway() {
        return IntegrationFlows.from(
                Http.inboundChannelAdapter("/api/invoicing/invoices")
                        .requestPayloadType(String.class)
        )
                .channel("router-channel")
                .get();
    }

    @Bean
    IntegrationFlow router() {
        return IntegrationFlows.from("router-channel")
                .route("#jsonPath(payload, '$.amount') > 1000", routes -> routes
                        .subFlowMapping("false", subflow -> subflow.channel("fasttrack-channel"))
                        .subFlowMapping("true", subflow -> subflow.channel("normaltrack-channel"))
                )
                .get();
    }

    @Bean
    IntegrationFlow normalTrack() {
        return IntegrationFlows.from("normaltrack-channel")
                .handle(System.out::println)
                .get();
    }

    @Bean
    IntegrationFlow fastTrack() {
        return IntegrationFlows.from("fasttrack-channel")
                .handle(System.err::println)
                .get();
    }

}
