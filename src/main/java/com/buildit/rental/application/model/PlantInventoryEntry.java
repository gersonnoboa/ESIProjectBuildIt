package com.buildit.rental.application.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.*;

/**
 * Created by Oleksandr on 3/21/2017.
 */
@Embeddable
@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName="of")
public class PlantInventoryEntry {
    String name;
    String plant_href;
}
