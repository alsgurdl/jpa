package com.example.final_project_java.userapi.dto.response;

import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Year;

@Setter
@Getter
@ToString
public class NaverUserDTO {

    @JsonProperty("response")
    private NaverAccount naverAccount;

    private LocalDate birthDay;

    private String userId;

    @Setter @Getter
    @ToString
    public static class NaverAccount {

        private String id;

        private String name;

        private String email;

        private String nickname;

        @JsonProperty("profile_image")
        private String profileImageUrl;

        private String mobile;

        private LoginMethod loginMethod;

    }

    public User toEntity(String accessToken, PasswordEncoder passwordEncoder) {
        return User.builder()
                .id(userId)
                .email(this.naverAccount.email)
                .name(this.naverAccount.name)
                .password(passwordEncoder.encode("password!")) // 비밀번호 암호화
                .profilePicture(this.naverAccount.profileImageUrl)
                .phoneNumber(this.naverAccount.mobile)
                .accessToken(accessToken)
                .loginMethod(LoginMethod.NAVER)
                .birthday(birthDay)
                .build();
    }


}
