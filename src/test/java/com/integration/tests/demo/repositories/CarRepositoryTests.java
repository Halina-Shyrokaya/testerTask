package com.integration.tests.demo.repositories;

import com.integration.tests.demo.entities.Car;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


// integration
@DataJpaTest
public class CarRepositoryTests {

    @Autowired
    TestEntityManager entityManager;
    @Autowired
    CarRepository repository;

    @Test
    public void saveCar() {
        Car car = new Car();
        car.setName("audi");
        car = entityManager.persistAndFlush(car);
        Optional<Car> found = repository.findById(car.getId());
        assertTrue(found.isPresent());
        assertThat(found.get()).isEqualTo(car);
    }

    @Test
    public void findCarsByName() {
        Car car = new Car();
        car.setName("audi");
        car = entityManager.persistAndFlush(car);
        assertThat(repository.findCarsByName(car.getName()).contains(car));
    }
}

