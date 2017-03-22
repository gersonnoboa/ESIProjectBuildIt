package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String name;
    String plant_href;
}
