package com.example.final_project_java.car.dto.request;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.entity.CarOptions;
import com.example.final_project_java.userapi.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarCreateRequestDTO {

   @NotBlank
   private String carName;

   @NotBlank
   private String carCompany;

   private int maximumPassenger;

   private Year carYear;

   private int carPrice;

   private String carPicture;

   private CarOptions carOptions;

   private String category;

   // DTO -> Entity
   public Car toEntity() {
      return Car.builder()
            .carName(carName)
            .carCompany(carCompany)
            .maximumPassenger(maximumPassenger)
            .carYear(carYear)
            .carPrice(carPrice)
            .carPicture(carPicture)
            .carOptions(carOptions)
            .category(category)
            .build();
   }


}












































