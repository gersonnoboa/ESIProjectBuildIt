package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Oleksandr on 3/22/2017.
 */
@Data
public class PurchaseOrderDTO extends ResourceSupport {

        String _id;
        PlantInventoryEntryDTO plant;
        BusinessPeriodDTO rentalPeriod;
        BigDecimal total;
        POStatus status;
}
