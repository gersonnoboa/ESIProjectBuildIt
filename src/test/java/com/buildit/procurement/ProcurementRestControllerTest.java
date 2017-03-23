package com.buildit.procurement;

import com.buildit.ProcurementApplication;
import com.buildit.procurement.application.dto.PlantHireRequestDTO;
import com.buildit.procurement.application.dto.PurchaseOrderDTO;
import com.buildit.procurement.application.service.RentalService;
import com.buildit.procurement.domain.model.PlantHireRequest;
import com.buildit.rental.application.dto.PlantInventoryEntryDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by gkgranada on 17/03/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ProcurementApplication.class,ProcurementRestControllerTest.RentalServiceMock.class})
@WebAppConfiguration
public class ProcurementRestControllerTest {
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @Autowired @Qualifier("_halObjectMapper")
    ObjectMapper mapper;

    @Autowired
    RentalService rentalService;

    @Configuration
    static class RentalServiceMock {
        @Bean
        public RentalService rentalService() {
            return Mockito.mock(RentalService.class);
        }
    }

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testGetAllPlants() throws Exception {
        Resource responseBody = new ClassPathResource("trucks.json", this.getClass());
        List<PlantInventoryEntryDTO> list =
                mapper.readValue(responseBody.getFile(), new TypeReference<List<PlantInventoryEntryDTO>>() { });
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(2);
        when(rentalService.findAvailablePlants("Truck", startDate, endDate)).thenReturn(list);
        MvcResult result = mockMvc.perform(
                get("/api/procurements/plants?name=Truck&startDate={start}&endDate={end}", startDate, endDate))
                .andExpect(status().isOk())
                .andReturn();

        List<PlantInventoryEntryDTO> plants = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<PlantInventoryEntryDTO>>() { });

        assertThat(plants.size()).isNotEqualTo(0);
    }

    @Test
    public void testFindPurchaseOrder() throws Exception {
        Resource responseBody = new ClassPathResource("purchase-order.json", this.getClass());
        PurchaseOrderDTO list = mapper.readValue(responseBody.getFile(), new TypeReference<PurchaseOrderDTO>() { });
        String id = "1";
        when(rentalService.findPurchaseOrder("1")).thenReturn(list);
        MvcResult result = mockMvc.perform(
                get("/api/procurements/po/find?id={id}", id))
                .andExpect(status().isOk())
                .andReturn();

        PurchaseOrderDTO pos = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<PurchaseOrderDTO>() { });

        assertThat(pos.get_id()).isEqualTo(id);
        assertThat(pos.get_id()).isNotEqualTo("2");
        assertThat(pos.getPlant().getName()).isEqualTo("Mini excavator");
    }

    @Test
    public void testClosePurchaseOrder() throws Exception {
        String id = "1";
        Mockito.doCallRealMethod().when(rentalService).closePurchaseOrder(id);

        MvcResult result = mockMvc.perform(
                get("/api/procurements/po/close?_id={id}", id))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    @Test
    public void testRejectPurchaseOrder() throws Exception {
        String id = "1";
        Mockito.doCallRealMethod().when(rentalService).rejectPurchaseOrder(id);

        MvcResult result = mockMvc.perform(
                get("/api/procurements/po/reject?_id={id}", id))
                .andExpect(status().isOk())
                .andReturn();

        assertThat(result.getResponse().getContentAsString()).isEqualTo("");
    }

    @Test
    public void testCreatePlantHireRequest() throws Exception {

        Resource parameter = new ClassPathResource("plant-hire-request.json", this.getClass());
        PlantHireRequestDTO hireRequestDTO = mapper.readValue(parameter.getFile(), new TypeReference<PlantHireRequestDTO>() {
        });

        Resource requestBody = new ClassPathResource("phr-po.json", this.getClass());
        PlantHireRequest hireRequestResponse = mapper.readValue(requestBody.getFile(), new TypeReference<PlantHireRequest>() {
        });

        when(rentalService.createPlantHireRequest(hireRequestDTO)).thenReturn(hireRequestResponse);

        MvcResult result = mockMvc.perform(
                post("/api/procurements/orders", hireRequestDTO))
                .andExpect(status().isOk())
                .andReturn();

        PlantHireRequestDTO phrdtoresponse = mapper.readValue(result.getResponse().getContentAsString(), PlantHireRequestDTO.class);
        assertThat(phrdtoresponse).isNotNull();
    }



}