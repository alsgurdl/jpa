package com.example.final_project_java.noti.dto.request;

import com.example.final_project_java.noti.entity.Noti;
import com.example.final_project_java.userapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotiCreateRequestDTO {

   private String notiTitle;

   private String notiContent;

   private User notiWriter;

   private LocalDateTime regDate;

   private int views;

   public Noti toEntity() {
      return Noti.builder()
            .notiTitle(notiTitle)
            .notiContent(notiContent)
            .notiWriter(notiWriter)
            .regDate(regDate)
            .views(views)
            .build();
   }


}












































