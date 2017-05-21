package com.buildit.procurement.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Data
public class PlantHireRequestExtensionDTO extends ResourceSupport {

    String _id;
    BusinessPeriodDTO rentalPeriod;
    PurchaseOrderDTO order;

}
