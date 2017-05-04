package com.buildit.procurement.application.service;

import com.buildit.common.domain.BusinessPeriod;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantHireRequestExtensionDTO;
import com.buildit.procurement.application.dto.PlantHireRequestUpdateDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.*;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import com.buildit.procurement.infrastructure.RequestIdentifierFactory;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.buildit.rental.application.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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

    @Autowired
    PlantHireRequestUpdateAssembler plantHireRequestUpdateAssembler;

    public PlantHireRequest createPlantHireRequest (PlantHireRequestDTO hireRequestDTO) {
        System.out.println("CREATE: " + hireRequestDTO);
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
        reqPoDTO.set_id("1");
        reqPoDTO.setPlant(hireRequestDTO.getPlant());
        reqPoDTO.setRentalPeriod(hireRequestDTO.getRentalPeriod());
        reqPoDTO.setOrder_href("to");

        PurchaseOrderDTO poDTO = createPurchaseOrder(reqPoDTO);
        //PurchaseOrder po = PurchaseOrder.of(poDTO.getId().getHref());
        PurchaseOrder po = PurchaseOrder.of("http://localhost:8080/api/inventory/plants/1");

        PlantHireRequest request = PlantHireRequest.of(
                requestIdentifierFactory.nextPlantHireRequestID(),
                siteEngineer,
                worksEngineer,
                comment,
                site,
                BusinessPeriod.of(
                        hireRequestDTO.getRentalPeriod().getStartDate(), hireRequestDTO.getRentalPeriod().getEndDate()),
                POStatus.PENDING,
                plant,
                po,
                supplier,
                BigDecimal.valueOf(100)
                );

        return requestRepository.save(request);
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
        //PurchaseOrderDTO order =restTemplate.postForObject("http://localhost:8080/api/sales/orders/",requestDTO,PurchaseOrderDTO.class);

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.set_id("1");
        order.setOrder_href("");
        return requestDTO;
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

    public PlantHireRequestDTO closePlantHireRequest(String id){
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleClosure();
        return PHRAssembler.toResource(requestRepository.save(phr));
    }

    public PlantHireRequestDTO updatePlantHireRequest(PlantHireRequestUpdateDTO newPhr){
        PlantHireRequest phr = requestRepository.findOne(newPhr.get_id());
        PlantHireRequestUpdate update = plantHireRequestUpdateAssembler.toResource(newPhr);
        phr.handleUpdate(update);
        return PHRAssembler.toResource((requestRepository.save(phr)));
    }

    public PlantHireRequestDTO extendPlantHireRequest(PlantHireRequestExtensionDTO newPhr) {
        BusinessPeriod period = BusinessPeriod.of(
                newPhr.getRentalPeriod().getStartDate(),newPhr.getRentalPeriod().getEndDate());
        PurchaseOrderDTO order =restTemplate.getForObject(
                newPhr.getOrder().getOrder_href(),PurchaseOrderDTO.class);
        order.setRentalPeriod(newPhr.getRentalPeriod());
        PurchaseOrderDTO extendedPO =restTemplate.patchForObject(
                "http://localhost:8080/api/sales/orders/{id}/extensions",order,PurchaseOrderDTO.class);
        PlantHireRequest phr = requestRepository.findOne(newPhr.get_id());
        phr.setOrder(PurchaseOrder.of(extendedPO.getOrder_href()));
        phr.setRentalPeriod(period);
        phr.handleExtension(phr);
        return PHRAssembler.toResource((requestRepository.save(phr)));
    }
}
