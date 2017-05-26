package com.buildit.procurement.application.service;

import com.buildit.common.domain.BusinessPeriod;
import com.buildit.common.integration.MailIntegration;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PlantHireRequestExtensionDTO;
import com.buildit.procurement.application.dto.PlantHireRequestUpdateDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.domain.model.*;
import com.buildit.procurement.domain.repository.PlantHireRequestRepository;
import com.buildit.procurement.domain.repository.PurchaseOrderRepository;
import com.buildit.procurement.infrastructure.PurchaseOrderIdentifierFactory;
import com.buildit.procurement.infrastructure.RequestIdentifierFactory;
import com.buildit.procurement.application.dto.PlantInventoryEntryDTO;
import com.buildit.procurement.domain.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
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
    PurchaseOrderIdentifierFactory purchaseOrderIdentifierFactory;

    @Autowired
    PlantHireRequestAssembler PHRAssembler;

    @Autowired
    RentalService rentalService;

    @Autowired
    PlantHireRequestUpdateAssembler plantHireRequestUpdateAssembler;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    MailIntegration mailIntegration;

    @PostConstruct
    private void setUpAuth() {
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor("user1", "user1"));
    }

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
        reqPoDTO.set_id(purchaseOrderIdentifierFactory.nextPurchaseOrderID());
        reqPoDTO.setPlant(hireRequestDTO.getPlant());
        reqPoDTO.setRentalPeriod(hireRequestDTO.getRentalPeriod());
        reqPoDTO.setOrder_href("to");

        BusinessPeriod period = BusinessPeriod.of(
                hireRequestDTO.getRentalPeriod().getStartDate(), hireRequestDTO.getRentalPeriod().getEndDate());

        PurchaseOrderDTO poDTO = createPurchaseOrder(reqPoDTO);
        //PurchaseOrder po = PurchaseOrder.of(poDTO.getId().getHref());

        //PurchaseOrder po = PurchaseOrder.of("http://localhost:8080/api/inventory/plants/1");

        PurchaseOrder po = PurchaseOrder.of(
                purchaseOrderIdentifierFactory.nextPurchaseOrderID(),
                plant,
                period
        );

        purchaseOrderRepository.save(po);

        PlantHireRequest request = PlantHireRequest.of(
                requestIdentifierFactory.nextPlantHireRequestID(),
                siteEngineer,
                worksEngineer,
                comment,
                site,
                period,
                POStatus.PENDING,
                plant,
                po,
                supplier,
                hireRequestDTO.getPlant().getPrice()
                );

        return requestRepository.save(request);
    }

    // procurement domain

    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------

    private void runInterceptors () {
        restTemplate.getInterceptors().add(
                new BasicAuthorizationInterceptor("user1", "user1"));
    }

    // inventory domain
    public List<PlantInventoryEntryDTO> findAvailablePlants(String plantName, LocalDate startDate, LocalDate endDate) {
        runInterceptors();

        PlantInventoryEntryDTO[] plants = restTemplate.getForObject(
                "http://localhost:8080/api/inventory/plants?name={name}&startDate={start}&endDate={end}",
                PlantInventoryEntryDTO[].class, plantName, startDate, endDate);
        System.out.println("Plants: " + Arrays.asList(plants));

        for (PlantInventoryEntryDTO plant : plants) {
            plant.setPlant_href(plant.getId().getHref());
        }

        return Arrays.asList(plants);
    }

    //---------------------------------------------------------------------------------------------------------

    public PlantInventoryEntryDTO findPlant(String id) {
        runInterceptors();
        PlantInventoryEntryDTO plant = restTemplate.getForObject(
                "http://localhost:8080/api/inventory/plants/{id}", PlantInventoryEntryDTO.class, id);
        plant.setPlant_href(plant.getId().getHref());
        return plant;
    }

    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------

    // sales domain
    public PurchaseOrderDTO findPurchaseOrder(String id) {
        runInterceptors();
        PurchaseOrderDTO order =restTemplate.getForObject(
                "http://localhost:8080/api/sales/orders/{id}", PurchaseOrderDTO.class, id);
        order.setOrder_href(order.getId().getHref());
        return order;
    }

    //---------------------------------------------------------------------------------------------------------

    public List<PurchaseOrderDTO> findAll(){
        runInterceptors();
        PurchaseOrderDTO[] orders = restTemplate.getForObject(
                "http://localhost:8080/api/sales/orders",
                PurchaseOrderDTO[].class);
        return Arrays.asList(orders);
    }

    //---------------------------------------------------------------------------------------------------------

    public PurchaseOrderDTO acceptPurchaseOrder(String id) {
        runInterceptors();
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
    public void  rejectPurchaseOrder(String id){
        runInterceptors();
        restTemplate.delete("http://localhost:8080/api/sales/orders/{id}/accept",id);


//        restTemplate.Delete parameters:
//        url - the URL
//        uriVariables - the variables to expand in the template
    }
    //---------------------------------------------------------------------------------------------------------
    public void closePurchaseOrder(String id) {
        runInterceptors();
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

    public PlantHireRequestDTO getPlantHireRequest(String id) {
        PlantHireRequest phr = requestRepository.findOne(id);
        return PHRAssembler.toResource(phr);
    }

    public PlantHireRequestDTO acceptPlantHireRequest(String id) {
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleAcceptance();

        String purchaseOrder =
                "{\n" +
                        "  \"id\":" + phr.getOrder().get_id() + ",\n" +
                        "  \"plant\":" + phr.getPlant().getPlant_href() + ",\n" +
                        "  \"dueDate\": \"" + phr.getRentalPeriod().getEndDate().toString() +"\"\n" +
                        "  \"total\": \"" + phr.getOrder().getTotal() +"\"\n" +
                        "}\n";

        phr.getOrder().handleAcceptance();

        try {
            mailIntegration.sendMail("esi2017.g17@gmail.com",
                    "Purchase Order",
                    "Dear customer\n\nAttached you will find the Purchase Order.",
                    "purchase-order.json",
                    purchaseOrder);
        }
        catch (Exception e){
            System.err.println("Error");
        }

        return PHRAssembler.toResource(requestRepository.save(phr));
    }


    public PlantHireRequestDTO rejectPlantHireRequest(String id) {
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleRejection();
        return PHRAssembler.toResource(requestRepository.save(phr));
    }

    public PlantHireRequestDTO closePlantHireRequest(String id) throws Exception{
        PlantHireRequest phr = requestRepository.findOne(id);
        phr.handleClosure();
        return PHRAssembler.toResource(requestRepository.save(phr));
    }

    public PlantHireRequestDTO updatePlantHireRequest(PlantHireRequestUpdateDTO newPhr) throws Exception{
        PlantHireRequest phr = requestRepository.findOne(newPhr.get_id());
        PlantHireRequestUpdate update = plantHireRequestUpdateAssembler.toResource(newPhr);
        phr.handleUpdate(update);
        return PHRAssembler.toResource((requestRepository.save(phr)));
    }

    public PlantHireRequestDTO extendPlantHireRequest(PlantHireRequestExtensionDTO newPhr) {
        BusinessPeriod period = BusinessPeriod.of(
                newPhr.getRentalPeriod().getStartDate(),newPhr.getRentalPeriod().getEndDate());

        if (newPhr.getOrder().getOrder_href() != null && !newPhr.getOrder().getOrder_href().equalsIgnoreCase("")){
            PurchaseOrderDTO order =restTemplate.getForObject(
                    newPhr.getOrder().getOrder_href(),PurchaseOrderDTO.class);
            order.setRentalPeriod(newPhr.getRentalPeriod());
            PurchaseOrderDTO extendedPO =restTemplate.patchForObject(
                    "http://localhost:8080/api/sales/orders/{id}/extensions",order,PurchaseOrderDTO.class);
        }

        PlantHireRequest phr = requestRepository.findOne(newPhr.get_id());
        phr.setOrder(phr.getOrder());
        phr.setRentalPeriod(period);
        phr.handleExtension(phr);
        return PHRAssembler.toResource((requestRepository.save(phr)));
    }

}
