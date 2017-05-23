package com.buildit.invoicing.application.service;

import com.buildit.common.integration.MailIntegration;
import com.buildit.invoicing.domain.model.Invoice;
import com.buildit.invoicing.domain.model.InvoiceStatus;
import com.buildit.invoicing.domain.repository.InvoiceRepository;
import com.buildit.invoicing.application.dto.InvoiceDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;

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

        PlantHireRequest phr = phrRepository.findOne(invoice.getPhr_id());

        if (invoice.getTotal().compareTo(BigDecimal.valueOf(1000)) >= 0){ //mayor o igual a 1000
            invoiceRepository.save(Invoice.of(invoice.getInvoice_id(), invoice.getTotal(), phr, InvoiceStatus.PENDING));
        }
        else{

            if (invoice.getTotal().compareTo(phr.getPrice()) == 0){
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

    public void processAttachment(GenericMessage<String> message){

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = message.getPayload();

        try{
            InvoiceDTO invoiceDTO = mapper.readValue(jsonInString, InvoiceDTO.class);
            processInvoice(invoiceDTO);
        }
        catch (Exception e){
            System.out.println("life sucks: " + e);
            System.out.println(jsonInString);
        }

    }

    private HashMap<String, Object> convertMessageToHashMap(String value){
        value = value.substring(1, value.length()-1);           //remove curly brackets
        String[] keyValuePairs = value.split(",");              //split the string to creat key-value pairs
        HashMap<String,Object> map = new HashMap<>();

        for(String pair : keyValuePairs)                        //iterate over the pairs
        {
            String[] entry = pair.split(":");                   //split the pairs to get key and value
            map.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
        }

        return map;
    }
}