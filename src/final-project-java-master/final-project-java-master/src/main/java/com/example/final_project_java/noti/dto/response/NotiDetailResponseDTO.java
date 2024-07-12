package com.example.final_project_java.noti.dto.response;

import com.example.final_project_java.noti.entity.Noti;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotiDetailResponseDTO {

   private int notiId;
   private String notiWriter;
   private String notiTitle;
   private String notiContent;
   private LocalDateTime regDate;
   private int views;

   public NotiDetailResponseDTO(Noti noti) {
      this.notiId = noti.getNotiId();
      this.notiWriter = String.valueOf(noti.getNotiWriter());
      this.notiTitle = noti.getNotiTitle();
      this.notiContent = noti.getNotiContent();
      this.regDate = noti.getRegDate();
      this.views = noti.getViews();
   }


}












































