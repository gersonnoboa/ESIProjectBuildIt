package com.buildit.rental.application.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.time.LocalDate;

/**
 * Created by Gerson Noboa on 17/3/2017.
 */
@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String name;
    LocalDate startDate;
    LocalDate endDate;
    String plant_href;

}
