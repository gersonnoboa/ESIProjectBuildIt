package com.buildit.procurement.application.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Data
public class PlantSupplierDTO extends ResourceSupport {
    String supplier_href;
}
