package com.buildit.procurement.application.dto;

import com.buildit.common.domain.BusinessPeriod;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Data
public class PlantHireRequestUpdateDTO extends ResourceSupport {

    String _id;
    BusinessPeriod rentalPeriod;
    PlantInventoryEntryDTO plant;
    CommentDTO comment;

}
