package com.mercadolibre.grupo1.projetointegrador.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.grupo1.projetointegrador.dtos.DeliveryDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Rogério Lambert
 * Classe de testes de integração dos endpoints de gestão de entregas,
 * realizada como parte do requisito 6 do bootcamper Rogério lambert
 */

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeliveryTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "http://localhost:8080/api/v1/fresh-products/delivery";


    @Test
    @WithMockUser(username = "customer3", roles = {"CUSTOMER"})
    @DisplayName("Testa endpoint de consulta de uma entrega de um cliente")
    public void itShouldReturnDeliveryOfCustomer() throws Exception {
        //Executa requisição
        MvcResult result = mockMvc.perform(get(BASE_URL + "/customer/3"))
                .andExpect(status().isOk())
                .andReturn();
        DeliveryDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDTO.class);
        //Executa aferições
        assertEquals(response.getId(), 3L);
        assertEquals(response.getCourierId(), 8L);
        assertEquals(response.getDeliveryAddress(), "rua c, 1000");
        assertEquals(response.getPurchaseOrderId(), 4L);
    }

    @Test
    @WithMockUser(username = "courier1", roles = {"COURIER"})
    @DisplayName("Testa endpoint de consulta de uma entrega de um entregador")
    public void itShouldReturnDeliveryOfCourier() throws Exception {
        //Executa requisição
        MvcResult result = mockMvc.perform(get(BASE_URL + "/courier/3"))
                .andExpect(status().isOk())
                .andReturn();
        DeliveryDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDTO.class);
        //Executa aferições
        assertEquals(response.getId(), 3L);
        assertEquals(response.getCourierId(), 8L);
        assertEquals(response.getDeliveryAddress(), "rua c, 1000");
        assertEquals(response.getPurchaseOrderId(), 4L);
    }

    @Test
    @WithMockUser(username = "courier1", roles = {"COURIER"})
    @DisplayName("Testa endpoint de consulta todas as entrega de um entregador")
    public void itShouldReturnDeliveryListOfCourier() throws Exception {
        //Executa requisição
        MvcResult result = mockMvc.perform(get(BASE_URL + "/courier/list"))
                .andExpect(status().isOk())
                .andReturn();
        List<DeliveryDTO> response = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDTO[].class));
        //Executa aferições
        assertEquals(response.size(),4);
    }

    @Test
    @WithMockUser(username = "customer3", roles = {"CUSTOMER"})
    @DisplayName("Testa endpoint de consulta de todas as entregas de um cliente")
    public void itShouldReturnDeliveryListOfCustomer() throws Exception {
        //Executa requisição
        MvcResult result = mockMvc.perform(get(BASE_URL + "/customer/list"))
                .andExpect(status().isOk())
                .andReturn();
        List<DeliveryDTO> response = Arrays.asList(objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDTO[].class));
        //Executa aferições
        assertEquals(response.size(),2);
    }

    @Test
    @WithMockUser(username = "agent3", roles = {"AGENT"})
    @DisplayName("Testa se uma entrega é persistida corretamente")
    public void itShouldDeliveryBePersisted() throws Exception {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(null)
                .deliveryAddress("Rua Direita 1")
                .courierId(8L)
                .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                .purchaseOrderId(3L)
                .build();

        String payload = objectMapper.writeValueAsString(deliveryDTO);
        //Executa requisição
        MvcResult result = mockMvc.perform(post(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andReturn();
        DeliveryDTO reponseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), DeliveryDTO.class);

        assertNotEquals(reponseDTO.getId(), null);
    }

    @Test
    @WithMockUser(username = "agent3", roles = {"AGENT"})
    @DisplayName("Testa se mensagem de erro é retornada quando se tenta cadastrar uma entrega sem um purchaseOrderId")
    public void itShouldResponseErrorMessageWhenPurchaseOrderIdIsNull() throws Exception {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(null)
                .deliveryAddress("Rua Direita 1")
                .courierId(2L)
                .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                .purchaseOrderId(null)
                .build();

        String payload = objectMapper.writeValueAsString(deliveryDTO);
        //Executa requisição
        mockMvc.perform(post(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("O id  da compra (purchaseOrderId) deve ser informado")));;
    }

    @Test
    @WithMockUser(username = "agent3", roles = {"AGENT"})
    @DisplayName("Testa se mensagem de erro é retornada quando se tenta cadastrar uma entrega sem um courierId")
    public void itShouldResponseErrorMessageWhenCourierIdIsNull() throws Exception {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(null)
                .deliveryAddress("Rua Direita 1")
                .courierId(null)
                .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                .purchaseOrderId(3L)
                .build();

        String payload = objectMapper.writeValueAsString(deliveryDTO);
        //Executa requisição
        mockMvc.perform(post(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("O id do entregador (courierId) deve ser informado")));;
    }

    @Test
    @WithMockUser(username = "agent3", roles = {"AGENT"})
    @DisplayName("Testa se mensagem de erro é retornada quando se tenta cadastrar uma entrega sem um horário de agendamento")
    public void itShouldResponseErrorMessageWhenDeliveryScheduleIsNull() throws Exception {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(null)
                .deliveryAddress("Rua Direita 1")
                .courierId(2L)
                .deliverySchedule(null)
                .purchaseOrderId(3L)
                .build();

        String payload = objectMapper.writeValueAsString(deliveryDTO);
        //Executa requisição
        mockMvc.perform(post(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("O horário de agendamento deve ser preenchido")));;
    }

    @Test
    @WithMockUser(username = "agent3", roles = {"AGENT"})
    @DisplayName("Testa se mensagem de erro é retornada quando se tenta cadastrar uma entrega sem um endereço de entrega")
    public void itShouldResponseErrorMessageWhenDeliveryAddressIsNull() throws Exception {
        //Setup do teste
        DeliveryDTO deliveryDTO = DeliveryDTO.builder()
                .id(null)
                .deliveryAddress(null)
                .courierId(2L)
                .deliverySchedule(LocalDateTime.of(2022, 5, 6,14,30))
                .purchaseOrderId(3L)
                .build();

        String payload = objectMapper.writeValueAsString(deliveryDTO);
        //Executa requisição
        mockMvc.perform(post(BASE_URL + "/").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("O endereço de entrega deve ser informado")));;
    }

}
