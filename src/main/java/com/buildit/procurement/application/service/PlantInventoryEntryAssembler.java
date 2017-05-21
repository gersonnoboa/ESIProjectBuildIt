package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 22/03/2017.
 */
@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {

    public PlantInventoryEntryAssembler() {
        super(ProcurementRestController.class, PlantInventoryEntryDTO.class);
    }

    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plantInventoryEntry) {
        PlantInventoryEntryDTO dto = new PlantInventoryEntryDTO();
        dto.setPlant_href(plantInventoryEntry.getPlant_href());
        dto.setName(plantInventoryEntry.getName());

        return dto;
    }

    public PlantInventoryEntry toResource(PlantInventoryEntryDTO plant){
        PlantInventoryEntry entry = PlantInventoryEntry.of(plant.getName(), plant.getPlant_href());
        return entry;
    }

}