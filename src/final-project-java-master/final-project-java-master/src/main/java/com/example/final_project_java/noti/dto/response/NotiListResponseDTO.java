package com.example.final_project_java.noti.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotiListResponseDTO {

   private List<NotiDetailResponseDTO> notiList;


}












































