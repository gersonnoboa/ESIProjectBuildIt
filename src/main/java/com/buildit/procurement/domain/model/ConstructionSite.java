package com.buildit.procurement.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Embeddable
@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName="of")
public class ConstructionSite {
    String site_href;
}
