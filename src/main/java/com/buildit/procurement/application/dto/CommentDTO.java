package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import com.buildit.rental.application.dto.EmployeeIdDTO;
import lombok.Data;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Data
public class CommentDTO extends ResourceSupport {
    String explanation;
    EmployeeIdDTO employee;
}
