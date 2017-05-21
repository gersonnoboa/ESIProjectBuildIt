package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.PlantHireRequestUpdateDTO;
import com.buildit.procurement.domain.model.PlantHireRequestUpdate;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Service
public class PlantHireRequestUpdateAssembler extends ResourceAssemblerSupport<PlantHireRequestUpdate, PlantHireRequestUpdateDTO> {

    @Autowired
    CommentAssembler commentAssembler;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PlantHireRequestUpdateAssembler() {
        super(ProcurementRestController.class, PlantHireRequestUpdateDTO.class);
    }

    public PlantHireRequestUpdateDTO toResource(PlantHireRequestUpdate update){
        PlantHireRequestUpdateDTO dto = new PlantHireRequestUpdateDTO();
        dto.set_id(update.getId());
        dto.setComment(commentAssembler.toResource(update.getComment()));
        return dto;
    }

    public PlantHireRequestUpdate toResource(PlantHireRequestUpdateDTO updateDto){
        PlantHireRequestUpdate update = new PlantHireRequestUpdate();
        update.setComment(commentAssembler.toResource(updateDto.getComment()));
        update.setRentalPeriod(updateDto.getRentalPeriod());
        update.setPlant(plantInventoryEntryAssembler.toResource(updateDto.getPlant()));

        return update;
    }
}
