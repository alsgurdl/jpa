package com.example.final_project_java.userapi.dto.request;

import com.example.final_project_java.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateRequestDTO {

    @Email
    private String email;

    @NotBlank
    private String userName;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String birthDay;

    public User toEntity() {
        return User.builder()
                .email(email)
                .name(userName)
                .phoneNumber(phoneNumber)
                .birthday(LocalDate.parse(birthDay))
                .build();
    }

}
