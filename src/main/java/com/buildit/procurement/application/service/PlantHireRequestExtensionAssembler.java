package com.buildit.procurement.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.PlantHireRequestExtensionDTO;
import com.buildit.procurement.application.dto.PlantHireRequestUpdateDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.PlantHireRequestExtension;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Service
public class PlantHireRequestExtensionAssembler extends ResourceAssemblerSupport<PlantHireRequestExtension, PlantHireRequestExtensionDTO> {

    @Autowired
    CommentAssembler commentAssembler;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PlantHireRequestExtensionAssembler() {
        super(ProcurementRestController.class, PlantHireRequestExtensionDTO.class);
    }

    public PlantHireRequestExtensionDTO toResource(PlantHireRequestExtension extension){
        PlantHireRequestExtensionDTO dto = new PlantHireRequestExtensionDTO();
        dto.set_id(extension.getId());
        BusinessPeriodDTO periodDTO = BusinessPeriodDTO.of(extension.getRentalPeriod().getStartDate(),extension.getRentalPeriod().getEndDate());
        dto.setRentalPeriod(periodDTO);
        PurchaseOrderDTO poDto = new PurchaseOrderDTO();
        poDto.setRentalPeriod(periodDTO);
        poDto.setOrder_href(extension.getOrder().getOrder_href());
        dto.setOrder(poDto);
        return dto;
    }
}
