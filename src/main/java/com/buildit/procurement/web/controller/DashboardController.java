package com.buildit.procurement.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by gerson on 25/05/17.
 */

@Controller
@RequestMapping("/dashboard")
public class DashboardController {


    @GetMapping("/phrs")
    public String getPhr(@RequestParam(name="id") String id){

        return "dashboard/phrs/show";
    }
}