package com.buildit.procurement.domain.model;

import com.buildit.common.domain.BusinessPeriod;
import lombok.Data;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Data
public class PlantHireRequestExtension {

    String id;
    BusinessPeriod rentalPeriod;
    PurchaseOrder order;
}
