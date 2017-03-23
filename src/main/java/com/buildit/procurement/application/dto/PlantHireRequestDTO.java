package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.domain.model.POStatus;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import lombok.Data;

/**
 * Created by Oleksandr on 3/22/2017.
 */

@Data
public class PlantHireRequestDTO  extends ResourceSupport {

    String _id;
    BusinessPeriodDTO rentalPeriod;
    POStatus status;
    PlantInventoryEntryDTO plant;
    PurchaseOrderDTO order;

}
