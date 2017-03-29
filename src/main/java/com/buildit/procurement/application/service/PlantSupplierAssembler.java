package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.PlantSupplier;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import com.buildit.rental.application.dto.PlantSupplierDTO;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Service
public class PlantSupplierAssembler extends ResourceAssemblerSupport<PlantSupplier, PlantSupplierDTO>{

public PlantSupplierAssembler() {super (ProcurementRestController.class, PlantSupplierDTO.class);}
    public PlantSupplierDTO toResource(PlantSupplier plantSupplier)
    {
     PlantSupplierDTO dto = new PlantSupplierDTO();
        dto.setSupplier_href(plantSupplier.getSupplier_href());
        return dto;
    }
}

