package com.buildit.procurement.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Data
public class PlantHireRequestExtensionDTO extends ResourceSupport {

    String _id;
    BusinessPeriodDTO rentalPeriod;
    PurchaseOrderDTO purchaseOrder;

}
