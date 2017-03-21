package com.buildit.common.rest.common.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by Oleksandr on 3/21/2017.
 */
@Embeddable
@Value
@NoArgsConstructor(force=true,access= AccessLevel.PRIVATE)
@AllArgsConstructor(staticName="of")
public class BusinessPeriod {
    LocalDate startDate;
    LocalDate endDate;

    public long numberOfWorkingDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
