package com.buildit.procurement.application.service;

import com.buildit.common.application.dto.exceptions.PlantNotFoundException;
import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.*;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import com.buildit.procurement.infrastructure.RequestIdentifierFactory;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.model.PlantInventoryEntry;
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

    @Autowired
    PlantHireRequestAssembler PHRAssembler;

    @Autowired
    RentalService rentalService;



    public PlantHireRequest createPlantHireRequest (PlantHireRequestDTO hireRequestDTO) {

        PlantInventoryEntry plant = PlantInventoryEntry.of(
                hireRequestDTO.getPlant().getName(),
                hireRequestDTO.getPlant().getPlant_href());

        PlantSupplier supplier = PlantSupplier.of(
                hireRequestDTO.getSupplier().getSupplier_href());

        Comment comment = Comment.of(
                hireRequestDTO.getComment().getExplanation(),
                hireRequestDTO.getComment().getEmployee_href());

        ConstructionSite site = ConstructionSite.of(hireRequestDTO.getSite().getSite_href());

        EmployeeId siteEngineer = EmployeeId.of(hireRequestDTO.getSiteEngineer().getEmployee_href());
        EmployeeId worksEngineer = EmployeeId.of(hireRequestDTO.getWorksEngineer().getEmployee_href());

        PurchaseOrderDTO reqPoDTO = new PurchaseOrderDTO();
        reqPoDTO.setPlant(hireRequestDTO.getPlant());
        reqPoDTO.setRentalPeriod(hireRequestDTO.getRentalPeriod());

        PurchaseOrderDTO poDTO = createPurchaseOrder(reqPoDTO);
        PurchaseOrder po = PurchaseOrder.of(poDTO.getId().getHref());

        PlantHireRequest request = PlantHireRequest.of(
                requestIdentifierFactory.nextPlantHireRequestID(),
                siteEngineer,
                worksEngineer,
                comment,
                site,
                BusinessPeriod.of(
                        hireRequestDTO.getRentalPeriod().getStartDate(), hireRequestDTO.getRentalPeriod().getEndDate()),
                hireRequestDTO.getStatus(),
                plant,
                po,
                supplier
                );

        request = requestRepository.save(request);
//        //try {
        PurchaseOrderDTO purchaseOrder = rentalService.createPurchaseOrder(poDTO);
        return requestRepository.save(request);
        //}
//        catch (PlantNotFoundException e){
//            throw e;
//        }

        //return requestRepository.save(request);
    }

    // procurement domain

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
    public void  rejectPurchaseOrder(String id)
    {
        restTemplate.delete("http://localhost:8080/api/sales/orders/{id}/accept",id);

//        restTemplate.Delete parameters:
//        url - the URL
//        uriVariables - the variables to expand in the template
    }
    //---------------------------------------------------------------------------------------------------------
    public void closePurchaseOrder(String id)
    {
        restTemplate.delete("http://localhost:8080/api/sales/orders/{id}/",id);
    }
    //---------------------------------------------------------------------------------------------------------

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO requestDTO) {
        System.out.println("dto: "+requestDTO);
        PurchaseOrderDTO order =restTemplate.postForObject(
                "http://localhost:8080/api/sales/orders/",requestDTO,PurchaseOrderDTO.class);
        return order;
    }
    //---------------------------------------------------------------------------------------------------------

    public PlantHireRequestDTO acceptPlantHireRequest(String id) {
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleAcceptance();
        return PHRAssembler.toResource(requestRepository.save(phr));
    }


    public PlantHireRequestDTO rejectPlantHireRequest(String id) {
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleRejection();
        return PHRAssembler.toResource(requestRepository.save(phr));
    }

}
