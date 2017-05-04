package com.buildit.common.integration;

import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

/**
 * Created by gerson on 04/05/17.
 */

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

    public void sendMail(String toEmail, PlantHireRequest phr) throws Exception{
        JavaMailSender mailSender = new JavaMailSenderImpl();
        String invoice1 =
                "{\n" +
                        "  \"order\":{\"_links\":{\"self\":{\"href\": \"" + phr.getOrder().getOrder_href() +"\"}}},\n" +
                        "  \"amount\":" + phr.getPrice() + ",\n" +
                        "  \"dueDate\": \"" + phr.getRentalPeriod().getEndDate().toString() +"\"\n" +
                        "}\n";

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom(gmailUsername); // Add the actual email addresses
        helper.setTo(toEmail);   // (also here)
        helper.setSubject("Invoice Purchase Order " + phr.get_id()); // Check the spelling the subject
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order.\n\nKindly yours,\n\nBuildIt Team!");

        helper.addAttachment("remittance.json", new ByteArrayDataSource(invoice1, "application/json"));

// I am Assuming "invoicingGateway" is an autowired reference to a spring bean
// associated with "InvoicingGateway"
        invoicingGateway.sendInvoice(rootMessage);
    }
}
