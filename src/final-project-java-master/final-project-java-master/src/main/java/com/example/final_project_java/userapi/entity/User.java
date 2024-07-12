package com.example.final_project_java.userapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.final_project_java.userapi.entity.Role.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user")
@IdClass(UserId.class)
public class User {

   @Id
   @Column(name = "user_id")
   @GeneratedValue(strategy = GenerationType.UUID)
   private String id;

   @Column(nullable = false)
   private String name;

   @Id
   @Column(name = "email", nullable = false)
   private String email;

   private String password;

   private String phoneNumber;

   @Enumerated(EnumType.STRING)
   @Builder.Default
   private Role role = COMMON;

   @CreationTimestamp
   private LocalDateTime joinDate;

   private String profilePicture;

   private LocalDate birthday;

   @Id
   @Column(name = "login_method")
   @Enumerated(EnumType.STRING)
   private LoginMethod loginMethod;

   private String accessToken; // 소셜 로그인 시 발급받는 accessToken을 저장 -> 로그아웃 때 필요


   // access token 저장 필드
   public void changeAccessToken(String accessToken) {
      this.accessToken = accessToken;
   }



}












































