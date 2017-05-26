package com.buildit.procurement.web.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.BusinessPeriod;
import lombok.Data;

/**
 * Created by gerson on 26/05/17.
 */
@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}
