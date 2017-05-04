package com.buildit.invoicing.service;

import com.buildit.common.integration.MailIntegration;
import com.buildit.invoicing.domain.model.Invoice;
import com.buildit.invoicing.domain.model.InvoiceStatus;
import com.buildit.invoicing.domain.repository.InvoiceRepository;
import com.buildit.invoicing.dto.InvoiceDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class InvoicingService {

    @Autowired
    PlantHireRequestRepository phrRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    MailIntegration integration;

    public void processInvoice(InvoiceDTO invoice) {
        System.out.println("Will process invoice: " + invoice);

        PlantHireRequest phr = phrRepository.findOne(invoice.getPhrId());
        System.out.println("por favor " + phr);

        if (invoice.getPrice().compareTo(BigDecimal.valueOf(1000)) >= 0){ //mayor o igual a 1000
            invoiceRepository.save(Invoice.of(invoice.getId(), invoice.getPrice(), phr, InvoiceStatus.PENDING));
        }
        else{

            if (invoice.getPrice().compareTo(phr.getPrice()) == 0){
                try {
                    integration.sendMail("esi2017.g17@gmail.com", phr);
                }
                catch (Exception e){

                }
            }
            else{
                System.err.println("Not equal");
            }
        }
    }
}