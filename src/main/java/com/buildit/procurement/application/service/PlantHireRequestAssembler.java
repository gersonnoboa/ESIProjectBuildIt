package com.buildit.procurement.application.service;

import com.buildit.common.ExtendedLink;
import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

/**
 * Created by gkgranada on 22/03/2017.
 */
@Service
public class PlantHireRequestAssembler  extends ResourceAssemblerSupport<PlantHireRequest, PlantHireRequestDTO> {
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    CommentAssembler commentAssembler;
    @Autowired
    EmployeeIdAssembler employeeIdAssembler;
    @Autowired
    PlantSupplierAssembler plantSupplierAssembler;
    @Autowired
    ConstructionSiteAssembler constructionSiteAssembler;



    public PlantHireRequestAssembler() {
        super(ProcurementRestController.class, PlantHireRequestDTO.class);
    }

    public PlantHireRequestDTO toResource(PlantHireRequest plantHireRequest) {
        PlantHireRequestDTO dto = createResourceWithId(plantHireRequest.get_id(), plantHireRequest);
        dto.set_id(plantHireRequest.get_id());
        dto.setPlant(plantInventoryEntryAssembler.toResource(plantHireRequest.getPlant()));
        dto.setRentalPeriod(BusinessPeriodDTO.of(plantHireRequest.getRentalPeriod().getStartDate(),plantHireRequest.getRentalPeriod().getEndDate()));
        dto.setStatus(plantHireRequest.getStatus());
        dto.setOrder(purchaseOrderAssembler.toResource(plantHireRequest.getOrder()));
        dto.setSupplier(plantSupplierAssembler.toResource(plantHireRequest.getSupplier()));
        dto.setSiteEngineer(employeeIdAssembler.toResource(plantHireRequest.getSiteEngineer()));
        dto.setWorksEngineer(employeeIdAssembler.toResource(plantHireRequest.getWorksEngineer()));
        dto.setSite(constructionSiteAssembler.toResource(plantHireRequest.getSite()));
        dto.setComment(commentAssembler.toResource(plantHireRequest.getComment()));


        try {
            switch (dto.getStatus()) {
                case PENDING:
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(RentalService.class)
                                    .acceptPlantHireRequest(dto.get_id())).toString(),
                            "accept", POST));
                    dto.add(new ExtendedLink(
                            linkTo(methodOn(RentalService.class)
                                    .rejectPlantHireRequest(dto.get_id())).toString(),
                            "reject", DELETE));
                    break;
                case OPEN:
                    //dto.add(new ExtendedLink(linkTo(methodOn(SalesRestController.class).closePurchaseOrder(dto.get_id()))-toString()), "close", DELETE);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }



        return dto;
    }
}
