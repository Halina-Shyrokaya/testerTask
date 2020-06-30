package com.integration.tests.demo.services;

import com.integration.tests.demo.dtos.CarDTO;
import com.integration.tests.demo.entities.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

//integration
@SpringBootTest
public class CarServiceImplTests {

    @Autowired
    CarServiceImpl service;

    @Test
    public void addCarAndFindIt() {
        CarDTO carDTO = new CarDTO();
        carDTO.setName("audi");
        service.addCar(carDTO);
        List<Car> found = service.search("audi", null);
        assertThat(found.stream()).allMatch(car -> car.getName().equals("audi"));
    }


}
