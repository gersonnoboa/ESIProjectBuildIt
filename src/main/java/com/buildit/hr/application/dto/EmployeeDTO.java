package com.buildit.hr.application.dto;

import com.buildit.hr.domain.model.JobTitle;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by Gerson Noboa on 17/3/2017.
 */
@Data
public class EmployeeDTO extends ResourceSupport {
    String _id;
    String name;
    JobTitle title;
}
