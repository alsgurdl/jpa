package com.example.final_project_java.userapi.dto.request;

import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;

@Data
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpRequestDTO {

   @NotBlank
   @Email
   private String email;

   @NotBlank
   @Size(min = 8, max = 20)
   private String password;

   @NotBlank
   private String name;

   @NotNull
   private String birthday;

   @NotNull
   private String phoneNumber;

   public User toEntity(String uploadFilePath) {
      return User.builder()
            .email(email)
            .password(password)
            .name(name)
            .birthday(LocalDate.parse(birthday))
            .phoneNumber(phoneNumber)
            .profilePicture(uploadFilePath)
            .loginMethod(LoginMethod.COMMON)
            .build();
   }


}












































