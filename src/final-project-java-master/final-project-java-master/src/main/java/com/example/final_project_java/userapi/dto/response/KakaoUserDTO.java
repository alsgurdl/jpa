package com.example.final_project_java.userapi.dto.response;

import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Setter @Getter
@ToString
public class KakaoUserDTO {

    private String id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    private String phoneNumber;

    @Setter @Getter
    @ToString
    public static class KakaoAccount {

        private String email;
        private Profile profile;
        private LoginMethod loginMethod;

        @Getter @Setter
        @ToString
        public static class Profile {
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }

    public User toEntity(String accessToken, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(this.kakaoAccount.email)
                .name(this.kakaoAccount.profile.nickname)
                .password(passwordEncoder.encode("password!")) // 비밀번호 암호화
                .profilePicture(this.kakaoAccount.profile.profileImageUrl)
                .phoneNumber(phoneNumber)
                .accessToken(accessToken)
                .loginMethod(LoginMethod.KAKAO)
                .build();
    }


}
