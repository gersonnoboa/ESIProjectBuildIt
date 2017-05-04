package com.buildit.invoicing.dto;

import lombok.Data;

import javax.print.DocFlavor;
import java.math.BigDecimal;

/**
 * Created by gerson on 04/05/17.
 */

@Data
public class InvoiceDTO {

    String id;
    BigDecimal price;
    String phrId;
}
