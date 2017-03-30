package com.buildit.procurement.domain.model;

import com.buildit.common.domain.BusinessPeriod;
import com.buildit.rental.application.model.PlantInventoryEntry;
import lombok.Data;

/**
 * Created by Gerson Noboa on 30/3/2017.
 */

@Data
public class PlantHireRequestUpdate {

    String id;
    Comment comment;
    BusinessPeriod rentalPeriod;
    PlantInventoryEntry plant;
}
