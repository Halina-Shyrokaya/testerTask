package com.integration.tests.demo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.tests.demo.dtos.CarDTO;
import com.integration.tests.demo.entities.Car;
import com.integration.tests.demo.services.CarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//integration
@WebMvcTest(controllers = CarsController.class)
@AutoConfigureMockMvc

public class CarsControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarServiceImpl service;

    @BeforeEach
    public void setUp() {
        Mockito.doCallRealMethod().when(service).addCar(any());
        doAnswer((Answer<Void>) invocation -> {
            Object[] args = invocation.getArguments();
            System.out.println("'Add car' method called with arguments: " + Arrays.toString(args));
            return null;
        }).when(service).addCar(any());
        Car car = new Car();
        car.setName("vaz");
        car.setId(345L);
        when(service.search(anyString(), anyLong())).thenReturn(List.of(car));
    }

    @Test
    public void listenToPostRequest() throws Exception {
        String json = "{\"name\":\"aston-martin\"}";
        mockMvc.perform(post("/addCar")
                .accept(MediaType.APPLICATION_JSON)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void listenToGetAllRequest() throws Exception {
        mockMvc.perform(get("/search")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void listenToGetWithValidParams() throws Exception {
        mockMvc.perform(get("/search")
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "vaz")
                .param("id", "345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("[{\"id\":345,\"name\":\"vaz\"}]"));
    }

    @Test
    public void listenToGetWithEmptyParams() throws Exception {
        mockMvc.perform(get("/search")
                .accept(MediaType.APPLICATION_JSON)
                .param("name", "")
                .param("id", "")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void listenToPostWithSerialization() throws Exception {
        CarDTO carDTO = new CarDTO();
        carDTO.setName("jaguar");
        mockMvc.perform(post("/addCar")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(carDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8"))
                .andExpect(status().isOk());
    }
}
