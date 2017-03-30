package com.buildit.procurement.application.service;

import com.buildit.procurement.application.dto.CommentDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.Comment;
import com.buildit.procurement.domain.model.EmployeeId;
import com.buildit.procurement.rest.controller.ProcurementRestController;
import com.buildit.rental.application.dto.EmployeeIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by Oleksandr on 3/29/2017.
 */
@Service
public class CommentAssembler extends ResourceAssemblerSupport<Comment,CommentDTO>{

    @Autowired
    EmployeeIdAssembler employeeIdAssembler;

    public CommentAssembler() {super (ProcurementRestController.class, CommentDTO.class);}

    public CommentDTO toResource(Comment comment){
        CommentDTO dto = new CommentDTO();
        dto.setExplanation(comment.getExplanation());
        dto.setEmployee_href(comment.getEmployee_href());
        //dto.setEmployee(employeeIdAssembler.toResource(comment.getEmployee()));
        return dto;
    }

    public Comment toResource(CommentDTO comment){
        Comment model = Comment.of(comment.getExplanation(), comment.getEmployee_href());
        return model;
    }
}
