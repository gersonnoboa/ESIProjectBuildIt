package com.buildit.rental.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Gerson Noboa on 17/3/2017.
 */
@Service
public class RentalService {
    @Autowired
    RestTemplate restTemplate;

    public List<PlantInventoryEntryDTO> findAvailablePlants(String plantName, LocalDate startDate, LocalDate endDate) {
        PlantInventoryEntryDTO[] plants = restTemplate.getForObject(
                "http://localhost:8090/api/inventory/plants?name={name}&startDate={start}&endDate={end}",
                PlantInventoryEntryDTO[].class, plantName, startDate, endDate);
        return Arrays.asList(plants);
    }
}
