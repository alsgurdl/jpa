package com.example.final_project_java.userapi.dto.response;

import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoogleLoginResponseDTO {

   private String email;

   private String userName;

   @JsonFormat(pattern = "yyyy년 MM월 dd일")
   private LocalDateTime joinDate;

   private Map<String, String> token;

   private Role role;

   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
   private LocalDate birthDay;

   private String phoneNumber;

   public GoogleLoginResponseDTO(User user, Map<String, String> token) {
      this.email = user.getEmail();
      this.userName = user.getName();
      this.joinDate = user.getJoinDate();
      this.token = token;
      this.role = user.getRole();
      this.birthDay = user.getBirthday();
      this.phoneNumber = user.getPhoneNumber();
   }


}












































