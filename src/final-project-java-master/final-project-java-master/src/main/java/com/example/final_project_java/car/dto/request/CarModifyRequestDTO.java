package com.example.final_project_java.car.dto.request;

import com.example.final_project_java.car.entity.CarOptions;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Year;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModifyRequestDTO {

   private String carId;

   @NotBlank
   private String carName;

   @NotBlank
   private String carCompany;

   @NotNull
   private int maximumPassenger;

   @NotNull
   private Year carYear;

   @NotNull
   private int carPrice;

   private String carPicture;

   private String carOptions;

   private String category;



}












































