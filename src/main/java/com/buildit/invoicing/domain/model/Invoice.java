package com.buildit.invoicing.domain.model;

import com.buildit.hr.domain.model.JobTitle;
import com.buildit.procurement.domain.model.PlantHireRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by gerson on 04/05/17.
 */
@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class Invoice {
    @Id
    String _id;

    BigDecimal totalPrice;

    @Embedded
    PlantHireRequest phr;

    @Enumerated(EnumType.STRING)
    InvoiceStatus invoiceStatus;
}