package com.buildit.procurement.application.dto;

import com.buildit.common.ResourceSupport;
import lombok.Data;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Data
public class CommentDTO extends ResourceSupport {
    String explanation;
    String employee_href;
}
