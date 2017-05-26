package com.buildit.procurement.web.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by gerson on 26/05/17.
 */

@Data
public class PartialPlantHireRequestDTO {
    //PlantInventoryEntryDTO entry;
    String startDate;
    String endDate;

    String id;
    String name;
    String description;
    String price;
    String plant_href;
}
