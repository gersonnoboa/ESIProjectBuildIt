package com.buildit.procurement.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.domain.model.POStatus;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import com.buildit.procurement.domain.model.PurchaseOrder;

import javax.persistence.*;

/**
 * Created by Oleksandr on 3/22/2017.
 */
public class PlantHireRequestDTO {

    Long id;
    BusinessPeriodDTO rentalPeriod;
    POStatus status;
    PlantInventoryEntryDTO plant;
    PurchaseOrderDTO order;

}
