package com.buildit.hr.domain.model;

import com.buildit.hr.domain.model.JobTitle;
import lombok.*;

import javax.persistence.*;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName="of")
public class Employee {
    @Id
    String _id;
    String name;

    @Enumerated(EnumType.STRING)
    JobTitle title;
}
