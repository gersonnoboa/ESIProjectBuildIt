package com.buildit.common.integration;

import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.model.PurchaseOrder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

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

@MessagingGateway
interface InvoicingGateway {
    @Gateway(requestChannel = "sendInvoiceChannel")
    public void sendInvoice(MimeMessage msg);
}

@Service
@Configuration
interface InvoicingGatewayService extends InvoicingGateway{

}

@Configuration
public class MailIntegration {

    @Value("${gmail.username}")
    String gmailUsername;
    @Value("${gmail.password}")
    String gmailPassword;

    @Autowired
    InvoicingGateway invoicingGateway;

    @Bean
    IntegrationFlow sendInvoiceFlow() {
        return IntegrationFlows.from("sendInvoiceChannel")
                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .port(465)
                        .protocol("smtps")
                        .credentials(gmailUsername, gmailPassword)
                        .javaMailProperties(p -> p.put("mail.debug", "false")))
                .get();
    }

    public void sendMail(String toEmail, String subject, String body, String attachmentName, String attachmentText) throws Exception{
        JavaMailSender mailSender = new JavaMailSenderImpl();

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom(gmailUsername); // Add the actual email addresses
        helper.setTo(toEmail);   // (also here)
        helper.setSubject(subject); // Check the spelling the subject
        helper.setText(body + "\n\nKindly yours,\n\nBuildIt Team!");

        if (attachmentName != null){
            helper.addAttachment(attachmentName, new ByteArrayDataSource(attachmentText, "application/json"));
        }

// I am Assuming "invoicingGateway" is an autowired reference to a spring bean
// associated with "InvoicingGateway"
        invoicingGateway.sendInvoice(rootMessage);
    }

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

    @Bean
    IntegrationFlow router() {
        return IntegrationFlows.from("router-channel")
                .handle("invoicingService", "processAttachment")
                .get();
    }
}
