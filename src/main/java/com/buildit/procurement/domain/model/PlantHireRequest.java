package com.buildit.procurement.domain.model;

import com.buildit.common.ResourceSupport;
import com.buildit.common.domain.BusinessPeriod;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Oleksandr on 3/21/2017.
 */

@Entity
@Data
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

    public static PlantHireRequest of(String id, BusinessPeriod rentalPeriod, POStatus status, PlantInventoryEntry plant, PurchaseOrder order) {
        PlantHireRequest phr = new PlantHireRequest();
        phr._id = id;
        phr.plant = plant;
        phr.rentalPeriod = rentalPeriod;
        phr.status = status;
        phr.order = order;
        return phr;
    }
}
