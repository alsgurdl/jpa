package com.example.final_project_java.car.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "car")
public class Car {

   @Id
   @GeneratedValue(strategy = GenerationType.UUID)
   private String carId;

   @Column(nullable = false)
   private String carName;

   @Column(nullable = false)
   private String carCompany;

   @Column(nullable = false)
   private int maximumPassenger;

   @Column(nullable = false)
   private Year carYear;

   @Column(nullable = false)
   private int carPrice;

   private String carPicture;

   @Enumerated(EnumType.STRING)
   @Column(nullable = false)
   private CarOptions carOptions;

   @Column(nullable = false)
   private String category; // 차 종류


}












































