package com.buildit.procurement.domain.model;

import com.buildit.common.rest.common.domain.BusinessPeriod;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Oleksandr on 3/21/2017.
 */

@Entity
@Data
public class PlantHireRequest {
    @Id @GeneratedValue
    Long id;

    @Embedded
    BusinessPeriod rentalPeriod;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    PlantInventoryEntry plant;

    @Embedded
    PurchaseOrder order;

    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
