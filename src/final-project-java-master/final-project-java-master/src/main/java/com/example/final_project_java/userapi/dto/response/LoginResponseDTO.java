package com.example.final_project_java.userapi.dto.response;

import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDTO {

    private String userId;

    private String email;

    private String userName;

    private String phoneNumber;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDay;

    @JsonFormat(pattern = "yyyy년 MM월 dd일")
    private LocalDate joinDate;

    private Map<String, String> token; // 인증 토큰 (핵심)

    private String role; // 권한

    private LoginMethod loginMethod;

    public LoginResponseDTO(User user, Map<String, String> token) {
        this.userId = user.getId();
        this.email = user.getEmail();
        this.userName = user.getName();
        this.phoneNumber = user.getPhoneNumber() != null? user.getPhoneNumber() : "010-0000-0000"; // 핸드폰 번호가 null이면 임의로 번호 줌
        this.birthDay = LocalDate.from(user.getBirthday() != null ? LocalDate.from(user.getBirthday()) : LocalDate.now());
        // 기타 SNS 플랫폼 로그인 유저는 따로 회원가입을 진행한 적이
        // 없으니 joinDate가 null이다.
        this.joinDate = user.getJoinDate() == null ? LocalDate.now() : LocalDate.from(user.getJoinDate()); // LocalDateTime 타입이 다르면 from을 이용해서 넣어라!
        this.token = token;
        this.role = String.valueOf(user.getRole());
    }

}