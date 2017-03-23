package com.buildit.procurement.rest.controller;

import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.application.service.PlantHireRequestAssembler;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.application.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by lgarcia on 3/16/2017.
 */
@RestController
@RequestMapping("/api/procurements")
public class ProcurementRestController {

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;
    //test: localhost:8090/api/procurements/plants?name=excavator&startDate=2017-01-01&endDate=2017-01-01
    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants (
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {


//        if (plantName.isPresent() && startDate.isPresent() && endDate.isPresent()) {
//            if (endDate.get().isBefore(startDate.get()))
//                throw new IllegalArgumentException("Something wrong with the requested period ('endDate' happens before 'startDate')");

        if (plantName.isPresent() && startDate.isPresent() && endDate.isPresent()) {
            if (endDate.get().isBefore(startDate.get()))
                throw new IllegalArgumentException("Something wrong with the requested period ('endDate' happens before 'startDate')");
            return rentalService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get());
        } else
            throw new IllegalArgumentException(
                    String.format("Wrong number of parameters: Name='%s', Start date='%s', End date='%s'",
                            plantName.get(), startDate.get(), endDate.get()));
    }

    //test:localhost:8090/api/procurements/po/find?id=1
    @GetMapping("/po/find")
    public PurchaseOrderDTO findPurchaseOrder(
        @RequestParam(name = "id", required = true) Optional<String> poId) {

        if (poId.isPresent()){
            return rentalService.findPurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    //test:localhost:8090/api/procurements/po/close?id=1
    @GetMapping("/po/close")
    public void closePurchaseOrder(
            @RequestParam(name = "_id", required = true) Optional<String> poId){
        if (poId.isPresent()){
            rentalService.closePurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    //test:localhost:8090/api/procurements/po/reject?id=1
    @GetMapping("/po/reject")
    public void rejectPurchaseOrder(
            @RequestParam(name = "_id", required = true) Optional<String> poId){
        if (poId.isPresent()){
            rentalService.rejectPurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    @PostMapping("/orders")
    public PlantHireRequestDTO createPlantHireRequest (@RequestBody Optional<PlantHireRequestDTO> partialDto) {
        PlantHireRequestDTO request = partialDto.get();
        System.out.println("REQUEST: " + request);

        return plantHireRequestAssembler.toResource(rentalService.createPlantHireRequest(request));
    }
}
