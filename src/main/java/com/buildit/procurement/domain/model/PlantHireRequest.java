package com.buildit.procurement.domain.model;

import com.buildit.common.domain.BusinessPeriod;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Oleksandr on 3/21/2017.
 */

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class PlantHireRequest {
    @Id
    String _id;

    @Embedded
    BusinessPeriod rentalPeriod;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Embedded
    PlantInventoryEntry plant;

    @Embedded
    PurchaseOrder order;

//    @Column(precision = 8, scale = 2)
//    BigDecimal price;
}
