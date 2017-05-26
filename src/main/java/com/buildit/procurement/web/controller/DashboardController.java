package com.buildit.procurement.web.controller;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.procurement.application.dto.*;
import com.buildit.procurement.application.service.RentalService;
import com.buildit.procurement.domain.model.POStatus;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import com.buildit.procurement.infrastructure.RequestIdentifierFactory;
import com.buildit.procurement.web.dto.CatalogQueryDTO;
import com.buildit.procurement.web.dto.PartialPlantHireRequestDTO;
import com.sun.org.apache.xml.internal.resolver.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by gerson on 25/05/17.
 */

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    RentalService rentalService;

    @Autowired
    RequestIdentifierFactory identifierFactory;

    @GetMapping("/phr")
    public String getPhr(Model model, @RequestParam(name="id") String id){
        PlantHireRequestDTO phr = rentalService.getPlantHireRequest(id);
        model.addAttribute("phr", phr);
        return "dashboard/phrs/show";
    }

    @GetMapping("/phrs")
    public String getPhrs(Model model){
        List<PlantHireRequest> phrs = rentalService.getAllPlantHireRequests();
        model.addAttribute("phrs", phrs);
        return "dashboard/phrs/query-phrs";
    }

    @GetMapping("/plants/form")
    public String getPlantsForm(Model model){
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/phrs/query-form";
    }

    @PostMapping("/plants/form")
    public String getPlantsResult(Model model, CatalogQueryDTO query){
        List<PlantInventoryEntryDTO> plants =  rentalService.findAvailablePlants(
                query.getName(),
                query.getRentalPeriod().getStartDate(),
                query.getRentalPeriod().getEndDate()
        );
        model.addAttribute("plants", plants);
        model.addAttribute("dto", new PartialPlantHireRequestDTO());
        PlantHireRequestDTO phr = new PlantHireRequestDTO();
        model.addAttribute("phr", phr);

        System.out.println(query);
        model.addAttribute("catalogQuery", query);
        return "dashboard/phrs/query-result";
    }

    @PostMapping("/plants/new")
    public String createPhr(Model model, PartialPlantHireRequestDTO dto){

        PlantHireRequestDTO phr = new PlantHireRequestDTO();
        phr.set_id(identifierFactory.nextPlantHireRequestID());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        phr.setRentalPeriod(BusinessPeriodDTO.of(LocalDate.parse(dto.getStartDate(), formatter), LocalDate.parse(dto.getEndDate(), formatter)));
        phr.setStatus(POStatus.PENDING);

        PlantInventoryEntryDTO entry = new PlantInventoryEntryDTO();
        entry.set_id(dto.getId());
        entry.setName(dto.getName());
        entry.setDescription(dto.getDescription());
        entry.setPrice(new BigDecimal(dto.getPrice()));
        entry.setPlant_href(dto.getPlant_href());
        phr.setPlant(entry);

        EmployeeIdDTO siteEngineer = new EmployeeIdDTO();
        siteEngineer.setEmployee_href("");
        phr.setSiteEngineer(siteEngineer);

        EmployeeIdDTO worksEngineer = new EmployeeIdDTO();
        worksEngineer.setEmployee_href("");
        phr.setWorksEngineer(worksEngineer);

        ConstructionSiteDTO constructionSite = new ConstructionSiteDTO();
        constructionSite.setSite_href("");
        phr.setSite(constructionSite);

        CommentDTO comment = new CommentDTO();
        comment.setEmployee_href("");
        comment.setExplanation("");
        phr.setComment(comment);

        PlantSupplierDTO plantSupplierDTO = new PlantSupplierDTO();
        plantSupplierDTO.setSupplier_href("");
        phr.setSupplier(plantSupplierDTO);

        rentalService.createPlantHireRequest(phr);

        model.addAttribute("message", "Plant hire request created successfully");

        return "dashboard/phrs/action";
    }

    @PostMapping("/phrs/approve")
    public String approvePhr(Model model, String id){

        PlantHireRequestDTO phr = rentalService.acceptPlantHireRequest(id);
        model.addAttribute("message", "PHR approved successfully.");
        return "dashboard/phrs/action";
    }

    @PostMapping("/phrs/reject")
    public String rejectPhr(Model model, String id){

        PlantHireRequestDTO phr = rentalService.rejectPlantHireRequest(id);
        System.out.println(phr);
        model.addAttribute("message", "PHR rejected successfully.");
        return "dashboard/phrs/action";
    }

    @PostMapping("/phrs/cancel")
    public String cancelPhr(Model model, String id){

        try {
            PlantHireRequestDTO phr = rentalService.closePlantHireRequest(id);
            model.addAttribute("message", "PHR cancelled successfully.");
        }
        catch (Exception e){
            model.addAttribute("message", e.getMessage());
        }

        return "dashboard/phrs/action";
    }

    @GetMapping("/phrs/modify")
    public String modifyPhr(Model model, String id){

        PlantHireRequestDTO phr = rentalService.getPlantHireRequest(id);
        model.addAttribute("phr", phr);
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/phrs/edit";
    }

    @PostMapping("/phrs/modify")
    public String modifyPO(Model model, String id, CatalogQueryDTO dto){

        PlantHireRequestDTO phr = rentalService.getPlantHireRequest(id);
        PlantHireRequestExtensionDTO extension = new PlantHireRequestExtensionDTO();
        extension.set_id(id);
        extension.setRentalPeriod(BusinessPeriodDTO.of(dto.getRentalPeriod().getStartDate(), dto.getRentalPeriod().getEndDate()));
        extension.setOrder(phr.getOrder());

        PlantHireRequestDTO phrdto = rentalService.extendPlantHireRequest(extension);

        String message = "Purchase order modification successful";
        model.addAttribute("message", message);

        return "dashboard/phrs/action";
    }


    //********************************************
    //PO

    @GetMapping("/pos")
    public String getPO(Model model){
        List<PurchaseOrderDTO> pos = rentalService.findAll();
        model.addAttribute("pos", pos);
        return "dashboard/orders/show";
    }
}