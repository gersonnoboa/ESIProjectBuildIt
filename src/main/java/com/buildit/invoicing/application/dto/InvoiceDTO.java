package com.buildit.invoicing.application.dto;

import com.buildit.common.ResourceSupport;
import com.buildit.invoicing.domain.model.InvoiceStatus;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import lombok.Data;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by gerson on 04/05/17.
 */

@Data
public class InvoiceDTO extends ResourceSupport{

    String _id;
    BigDecimal totalPrice;
    PlantHireRequestDTO phr;
    InvoiceStatus invoiceStatus;
}
