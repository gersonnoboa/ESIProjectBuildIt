package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.EmployeeId;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import com.buildit.rental.application.dto.EmployeeIdDTO;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Service
public class EmployeeIdAssembler extends ResourceAssemblerSupport<EmployeeId, EmployeeIdDTO> {
    public EmployeeIdAssembler() {super(ProcurementRestController.class, EmployeeIdDTO.class);}

    public EmployeeIdDTO toResource(EmployeeId employeeId){
        EmployeeIdDTO dto = new EmployeeIdDTO();
        dto.setEmployee_href(employeeId.getEmployee_href());
        return dto;
    }
}
