package com.buildit.procurement.web.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import lombok.Data;

/**
 * Created by gerson on 26/05/17.
 */

@Data
public class PartialPlantHireRequestDTO {
    BusinessPeriodDTO rentalPeriod;
    PlantInventoryEntryDTO entry;
}
