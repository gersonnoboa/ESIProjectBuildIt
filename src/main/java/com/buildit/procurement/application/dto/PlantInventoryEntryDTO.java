package com.buildit.procurement.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Created by Gerson Noboa on 17/3/2017.
 */
@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String _id;
    String name;
    String description;
    BigDecimal price;
    String plant_href;
}
