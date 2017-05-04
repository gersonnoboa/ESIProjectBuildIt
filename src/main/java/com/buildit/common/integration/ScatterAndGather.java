package com.buildit.common.integration;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.http.Http;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.stereotype.Service;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
/**
 * Created by gerson on 04/05/17.
 */

@Service
class InvoiceProcessor {
    public String extractInvoice(MimeMessage msg) throws Exception {
        Multipart multipart = (Multipart) msg.getContent();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (bodyPart.getContentType().contains("json") &&
                    bodyPart.getFileName().startsWith("invoice"))
                return IOUtils.toString(bodyPart.getInputStream(), "UTF-8");
        }
        throw new Exception("oops");
    }
}

@Configuration
public class ScatterAndGather {

    /*@Bean
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

*/

    @Value("${gmail.username}")
    String gmailUsername;
    @Value("${gmail.password}")
    String gmailPassword;

    @Bean
    IntegrationFlow inboundMail() {

        return IntegrationFlows.from(Mail.imapInboundAdapter(
                String.format("imaps://%s:%s@imap.gmail.com:993/INBOX", gmailUsername, gmailPassword)
                ).selectorExpression("subject matches '.*invoice.*'"),
                e -> e.autoStartup(true)
                        .poller(Pollers.fixedDelay(40000))
        ).transform("@invoiceProcessor.extractInvoice(payload)")
                .channel("router-channel")
                .get();
    }


}
