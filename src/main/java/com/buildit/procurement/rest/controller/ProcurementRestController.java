package com.buildit.procurement.rest.controller;

import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.application.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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

    @GetMapping("/plants")
    public List<PlantInventoryEntryDTO> findAvailablePlants (
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {


//        if (plantName.isPresent() && startDate.isPresent() && endDate.isPresent()) {
//            if (endDate.get().isBefore(startDate.get()))
//                throw new IllegalArgumentException("Something wrong with the requested period ('endDate' happens before 'startDate')");
            return rentalService.findAvailablePlants(plantName.get(), startDate.get(), endDate.get());
//        } else
//            throw new IllegalArgumentException(
//                    String.format("Wrong number of parameters: Name='%s', Start date='%s', End date='%s'",
//                            plantName.get(), startDate.get(), endDate.get()));
    }

    @PostMapping("/orders")
    public ResponseEntity<PlantHireRequestDTO> createPlantHireRequest(
            @RequestParam(name = "plantHireRequest", required = false) Optional<PlantHireRequestDTO> request )
            throws URISyntaxException{

            PlantHireRequestDTO phrDTO = rentalService.createPlantHireRequest(request.get());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(new URI(phrDTO.getId().getHref()));
            return new ResponseEntity<PlantHireRequestDTO>(phrDTO, headers, HttpStatus.CREATED);
    }
}
