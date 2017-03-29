package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.ConstructionSite;
import com.buildit.procurement.domain.model.EmployeeId;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import com.buildit.rental.application.dto.ConstructionSiteDTO;
import com.buildit.rental.application.dto.EmployeeIdDTO;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Service
public class ConstructionSiteAssembler extends  ResourceAssemblerSupport<ConstructionSite, ConstructionSiteDTO>{

    public ConstructionSiteAssembler() {super(ProcurementRestController.class, ConstructionSiteDTO.class);}

    public ConstructionSiteDTO toResource(ConstructionSite constructionSite){
        ConstructionSiteDTO dto = new ConstructionSiteDTO();
        dto.setSite_href(constructionSite.getSite_href());
        return dto;
    }
}

