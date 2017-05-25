package com.buildit.procurement.web.controller;

import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.service.RentalService;
import com.buildit.procurement.domain.model.PlantHireRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by gerson on 25/05/17.
 */

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    RentalService rentalService;

    @GetMapping("/phrs")
    public String getPhr(Model model, @RequestParam(name="id") String id){

        PlantHireRequestDTO phr = rentalService.getPlantHireRequest(id);
        model.addAttribute("phr", phr);
        return "dashboard/phrs/show";
    }
}