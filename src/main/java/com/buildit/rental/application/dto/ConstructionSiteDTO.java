package com.buildit.rental.application.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Data
public class ConstructionSiteDTO extends ResourceSupport {
    String site_href;
}
