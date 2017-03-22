package com.buildit.procurement.application.service;

import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.POStatus;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import com.buildit.procurement.domain.model.PurchaseOrder;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import com.buildit.procurement.infrastructure.RequestIdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gkgranada on 17/03/2017.
 */
@Service
public class RentalService {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    PlantHireRequestRepository requestRepository;

    @Autowired
    RequestIdentifierFactory requestIdentifierFactory;

    public PlantHireRequest createPlantHireRequest (PlantHireRequestDTO hireRequestDTO) {

        PlantInventoryEntry plant = PlantInventoryEntry.of(
                hireRequestDTO.getPlant().getName(), hireRequestDTO.getPlant().getPlant_href());
        PurchaseOrder po = PurchaseOrder.of(hireRequestDTO.getOrder().getOrder_href());

        PlantHireRequest request = PlantHireRequest.of(
                requestIdentifierFactory.nextPlantHireRequestID(),
                BusinessPeriod.of(
                        hireRequestDTO.getRentalPeriod().getStartDate(), hireRequestDTO.getRentalPeriod().getEndDate()),
                hireRequestDTO.getStatus(),
                plant,
                po
                );

        return requestRepository.save(request);
    }

    // procurement domain
    //---------------------------------------------------------------------------------------------------------
//    public PlantHireRequest createPlantHireRequest(String id, BusinessPeriod rentalPeriod,POStatus status,PlantInventoryEntry plant,PurchaseOrder order){
//
//        PlantHireRequest request = new PlantHireRequest();
//        request.set_id(id);
//        request.setRentalPeriod(rentalPeriod);
//        request.setStatus(status);
//        request.setPlant(plant);
//        request.setOrder(order);
//
//        return requestRepository.save(request);
//    }

    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------

    // inventory domain
    public List<PlantInventoryEntryDTO> findAvailablePlants(String plantName, LocalDate startDate, LocalDate endDate) {
        PlantInventoryEntryDTO[] plants = restTemplate.getForObject(
                "http://localhost:8080/api/inventory/plants?name={name}&startDate={start}&endDate={end}",
                PlantInventoryEntryDTO[].class, plantName, startDate, endDate);
        return Arrays.asList(plants);
    }

    //---------------------------------------------------------------------------------------------------------

    public PlantInventoryEntryDTO findPlant(String id) {
        PlantInventoryEntryDTO plant = restTemplate.getForObject(
                "http://localhost:8080/api/inventory/plants/{id}", PlantInventoryEntryDTO.class, id);
        return plant;
    }

    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------

    // sales domain
    public PurchaseOrderDTO findPurchaseOrder(String id)
    {
        PurchaseOrderDTO order =restTemplate.getForObject(
                "http://localhost:8080/api/sales/orders/{id}", PurchaseOrderDTO.class, id);
        return order;
    }

    //---------------------------------------------------------------------------------------------------------

    public List<PurchaseOrderDTO> findAll(){
        PurchaseOrderDTO[] orders = restTemplate.getForObject(
                "http://localhost:8080/api/sales/orders",
                PurchaseOrderDTO[].class);
        return Arrays.asList(orders);
    }

    //---------------------------------------------------------------------------------------------------------

    public PurchaseOrderDTO acceptPurchaseOrder(String id)
    {
        PurchaseOrderDTO order =restTemplate.postForObject(
                "http://localhost:8080/api/sales/orders/{id}/accept",null,PurchaseOrderDTO.class,id);
        return order;
    }

    //    postForObject Parameters:
    //    url - the URL
    //    request - the Object to be POSTed (may be null)
    //    responseType - the type of the return value
    //    uriVariables - the variables to expand the template

    //---------------------------------------------------------------------------------------------------------
    void  rejectPurchaseOrder(String id)
    {
        restTemplate.delete("http://localhost:8080/api/sales/orders/{id}/accept",id);

//        restTemplate.Delete parameters:
//        url - the URL
//        uriVariables - the variables to expand in the template
    }
    //---------------------------------------------------------------------------------------------------------
    void  closePurchaseOrder(String id)
    {
        restTemplate.delete("http://localhost:8080/api/sales/orders/{id}/",id);
    }
    //---------------------------------------------------------------------------------------------------------
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO POrder) {
        PurchaseOrderDTO order =restTemplate.postForObject(
                "http://localhost:8080/api/sales/orders/",POrder,PurchaseOrderDTO.class);
        return order;
    }
    //---------------------------------------------------------------------------------------------------------

}
