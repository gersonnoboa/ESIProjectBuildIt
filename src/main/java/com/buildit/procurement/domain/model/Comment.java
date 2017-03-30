package com.buildit.procurement.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Embeddable
@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName="of")
public class Comment {
    String explanation;
    String employee_href;
}
