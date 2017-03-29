package com.buildit.hr.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.hr.application.dto.EmployeeDTO;
import com.buildit.hr.domain.model.Employee;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.service.PlantInventoryEntryAssembler;
import com.buildit.procurement.application.service.PurchaseOrderAssembler;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by gkgranada on 22/03/2017.
 */
//@Service
//public class EmployeeAssembler extends ResourceAssemblerSupport<Employee, EmployeeDTO> {
////    @Autowired
////    PlantInventoryEntryAssembler plantInventoryEntryAssembler;
////    @Autowired
////    PurchaseOrderAssembler purchaseOrderAssembler;
////
////    public EmployeeAssembler() {
////        super(EmployeeRestController.class, EmployeeDTO.class);
////    }
////
////    public EmployeeDTO toResource(Employee employee) {
////        EmployeeDTO dto = createResourceWithId(plantHireRequest.get_id(), Employee.);
////        dto.set_id(Employee.get_id());
////        dto.setPlant(plantInventoryEntryAssembler.toResource(plantHireRequest.getPlant()));
////        dto.setRentalPeriod(BusinessPeriodDTO.of(plantHireRequest.getRentalPeriod().getStartDate(),plantHireRequest.getRentalPeriod().getEndDate()));
////        dto.setStatus(plantHireRequest.getStatus());
////        dto.setOrder(purchaseOrderAssembler.toResource(plantHireRequest.getOrder()));
////
////        return dto;
////    }
//}
