package com.integration.tests.demo.services;

import com.integration.tests.demo.dtos.CarDTO;
import com.integration.tests.demo.entities.Car;
import com.integration.tests.demo.repositories.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

//unit
public class CarServiceImplUnitTests {

    @InjectMocks
    CarServiceImpl service;

    @Mock
    CarRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(repository.findById(anyLong()))
                .thenReturn(car());
    }

    @Test
    public void addCar() {
        CarDTO car = new CarDTO();
        car.setName("testCar");
        service.addCar(car);
        Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any(Car.class));
    }

    @Test
    public void findCarWithValidNameAndId() {

        List<Car> found = service.search("testCar", 3454L);
        Mockito.verify(repository, Mockito.times(1)).findById(3454L);
        assertThat(found.stream()).allMatch(car -> car.getName().equals("testCar"));
        assertThat(found.stream()).allMatch(car -> car.getId().equals(3454L));
    }

    @Test
    public void findCarWithValidIdAndNullName() {
        List<Car> found = service.search(null, 3454L);
        Mockito.verify(repository, Mockito.times(1)).findById(3454L);
        assertThat(found.stream()).allMatch(car -> car.getName().equals("testCar"));
        assertThat(found.stream()).allMatch(car -> car.getId().equals(3454L));
    }

    @Test
    public void findCarWithNullIdAndName() {
        service.search(null, null);
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

    @Test
    public void findCarWithEmptyNameAndNullId() {
        service.search(" ", null);
        Mockito.verify(repository, Mockito.times(1)).findCarsByName(" ");
    }

    @Test
    public void findCarWithNullIdAndValidName() {
        service.search("testCar", null);
        Mockito.verify(repository, Mockito.times(1)).findCarsByName("testCar");
    }

    private Optional<Car> car() {
        Car car = new Car();
        car.setId(3454L);
        car.setName("testCar");
        return Optional.of(car);
    }


}
