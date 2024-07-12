package com.example.final_project_java.car.repository;

import com.example.final_project_java.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {

    Optional<Car> findByCarName(String carName);

}















































