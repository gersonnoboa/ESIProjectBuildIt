package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.hr.application.dto.EmployeeDTO;
import com.buildit.procurement.domain.model.POStatus;
import com.buildit.procurement.domain.model.PlantSupplier;
import com.buildit.rental.application.dto.ConstructionSiteDTO;
import com.buildit.rental.application.dto.EmployeeIdDTO;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.dto.PlantSupplierDTO;
import lombok.Data;

/**
 * Created by Oleksandr on 3/22/2017.
 */

@Data
public class PlantHireRequestDTO  extends ResourceSupport {

    String _id;
    BusinessPeriodDTO rentalPeriod;
    POStatus status;
    PlantInventoryEntryDTO plant;
    PurchaseOrderDTO order;
    EmployeeIdDTO siteEngineer;
    EmployeeIdDTO worksEngineer;
    ConstructionSiteDTO site;
    CommentDTO comment;
    PlantSupplierDTO supplier;

}
