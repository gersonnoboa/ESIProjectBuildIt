package com.buildit.invoicing.application.service;

import com.buildit.invoicing.domain.model.Invoice;
import com.buildit.invoicing.application.dto.InvoiceDTO;
import com.buildit.procurement.application.service.PlantHireRequestAssembler;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 23/05/2017.
 */
@Service
public class InvoicingAssembler extends ResourceAssemblerSupport<Invoice, InvoiceDTO> {

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

    public InvoicingAssembler() {
        super(ProcurementRestController.class, InvoiceDTO.class);
    }

    public InvoiceDTO toResource(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.set_id(invoice.get_id());
        dto.setPhr(plantHireRequestAssembler.toResource(invoice.getPhr()));
        dto.setTotalPrice(invoice.getTotalPrice());
        dto.setInvoiceStatus(invoice.getInvoiceStatus());
        return dto;
    }
}