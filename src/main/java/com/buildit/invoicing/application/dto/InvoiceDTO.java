package com.buildit.invoicing.application.dto;

import lombok.Data;

import javax.print.DocFlavor;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by gerson on 04/05/17.
 */

@Data
public class InvoiceDTO {

    String invoice_id;
    BigDecimal total;
    String phr_id;
    //LocalDate date;
}
