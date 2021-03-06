package com.buildit.procurement.rest.controller;

import com.buildit.common.integration.MailIntegration;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantHireRequestExtensionDTO;
import com.buildit.procurement.application.dto.PlantHireRequestUpdateDTO;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
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
@CrossOrigin
public class ProcurementRestController {


    @Autowired
    RentalService rentalService;

    /**
     * PlantInventoryEntry [/plants/{_id}]
     * Retrieve PlantInventoryEntry details [GET]
     */
    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;
    //test:
    // localhost:8090/api/procurements/plants?name=excavator&startDate=2017-01-01&endDate=2017-01-01
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

    /**
     * PlantInventoryEntry [/plants/{_id}]
     * Retrieve PlantInventoryEntry details [GET]
     */


    /**
     * PurchaseOrder  Collection [/pos]
     * List All PurchaseOrders [GET]
     */

    /**
     * PurchaseOrder [/pos/{_id}]
     * Retrieve PurchaseOrder details [GET]
     */
    //test:localhost:8090/api/procurements/po?id=1
    @GetMapping("/pos")
    public PurchaseOrderDTO findPurchaseOrder(
        @RequestParam(name = "id", required = true) Optional<String> poId) {

        if (poId.isPresent()){
            return rentalService.findPurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    /**
     * PurchaseOrder [/pos/{_id}]
     * Update PurchaseOrder [PATCH]
     */

    /**
     * PurchaseOrder [/pos/{_id}]
     * Close PurchaseOrder [DELETE]
     */
    //test:localhost:8090/api/procurements/po?id=1
    @DeleteMapping("/pos")
    public void closePurchaseOrder(
            @RequestParam(name = "_id", required = true) Optional<String> poId){
        if (poId.isPresent()){
            rentalService.closePurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    /**
     * PurchaseOrder [/pos/{_id}]
     * Accept PurchaseOrder [POST]
     */

    /**
     * PurchaseOrder [/pos/{_id}]
     * Reject PurchaseOrder [DELETE]
     */
    //test:localhost:8090/api/procurements/po/reject?id=1
    @DeleteMapping("/pos/accept")
    public void rejectPurchaseOrder(
            @RequestParam(name = "_id", required = true) Optional<String> poId){
        if (poId.isPresent()){
            rentalService.rejectPurchaseOrder(poId.get());
        }
        else{
            throw new IllegalArgumentException("Purchase order ID must be present.");
        }
    }

    //test:
    // localhost:8090/api/procurements/phr/create

    /**
     * PlantHireRequest Collection [/phrs]
     * GET
     */

    /**
     * PlantHireRequest Collection [/phrs]
     * POST
     */
    @PostMapping("/phrs")
    public PlantHireRequestDTO createPlantHireRequest (@RequestBody Optional<PlantHireRequestDTO> partialDto) {
        PlantHireRequestDTO request = partialDto.get();
        System.out.println("REQUEST: " + request);
        PlantHireRequest dto = rentalService.createPlantHireRequest(request);
        System.out.println("PHR: " + dto);

        return plantHireRequestAssembler.toResource(rentalService.createPlantHireRequest(request));
    }

    /**
     * PlantHireRequest Collection [/phrs/{id}]
     * PATCH
     */
    @PatchMapping("/phrs/{id}")
    public PlantHireRequestDTO updatePlantHireRequest(@RequestBody Optional<PlantHireRequestUpdateDTO> partialDto) throws Exception{
        return rentalService.updatePlantHireRequest(partialDto.get());
    }

    /**
     * PlantHireRequest Collection [/phrs/{id}]
     * DELETE
     */
    @DeleteMapping("/phrs/{id}")
    public PlantHireRequestDTO closePlantHireRequest(@PathVariable String id) throws Exception{
        return rentalService.closePlantHireRequest(id);
    }

    /**
     * PlantHireRequest Collection [/phrs/{id}/accept]
     * POST
     */
    @PostMapping("/phrs/{id}/accept")
    public PlantHireRequestDTO acceptPlantHireRequest(@PathVariable String id) throws Exception {
        return rentalService.acceptPlantHireRequest(id);
    }

    /**
     * PlantHireRequest Collection [/phrs/{id}]
     * DELETE
     */
    @DeleteMapping("/phrs/{id}/accept")
    public PlantHireRequestDTO rejectPlantHireRequest(@PathVariable String id) throws Exception {
        return rentalService.rejectPlantHireRequest(id);
    }

    /**
     * PlantHireRequest Collection [/phrs/{id}/extensions]
     * PATCH
     */
    @PatchMapping("/phrs/{id}/extensions")
    public PlantHireRequestDTO extendPlantHireRequest(@RequestBody Optional<PlantHireRequestExtensionDTO> partialDto){
        System.out.println(partialDto.get());
        return rentalService.extendPlantHireRequest(partialDto.get());
    }

}
