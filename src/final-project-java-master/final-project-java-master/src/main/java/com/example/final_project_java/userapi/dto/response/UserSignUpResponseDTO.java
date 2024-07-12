package com.example.final_project_java.userapi.dto.response;

import com.example.final_project_java.userapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpResponseDTO {

   private String email;

   private String password;

   private String name;

   public UserSignUpResponseDTO(User saved) {
      this.email = saved.getEmail();
      this.password = saved.getPassword();
      this.name = saved.getName();
   }


}












































