package com.example.final_project_java.car.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarListResponseDTO {

   private String error;
   private List<CarDetailResponseDTO> carList;

}












































