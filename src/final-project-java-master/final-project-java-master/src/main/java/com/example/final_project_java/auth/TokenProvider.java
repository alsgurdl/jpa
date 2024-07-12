package com.example.final_project_java.auth;

import com.example.final_project_java.userapi.dto.response.GoogleUserResponseDTO;
import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class TokenProvider {

   @Value("${jwt.secret}")
   private String SECRET_KEY;

   /**
    * JSON Web Token을 생성하는 메서드
    * @param userEntity - 토큰의 내용(클레임)에 포함될 유저 정보
    * @return - 생성된 JSON을 암호화 한 토큰값
    */

   public String createToken(User userEntity, String secretKey, long duration, ChronoUnit unit) {

      Date expiry = Date.from(
            Instant.now().plus(100, ChronoUnit.DAYS)
      );

      // 토큰 생성
      Map<String, String> claims = new HashMap<>();
      claims.put("email", userEntity.getEmail());
      claims.put("phoneNumber", userEntity.getPhoneNumber());
      claims.put("role", userEntity.getRole().toString());
      claims.put("loginMethod", String.valueOf(userEntity.getLoginMethod()));
      claims.put("userId", userEntity.getId());
      claims.put("name", userEntity.getName());
      claims.put("birthDay", String.valueOf(userEntity.getBirthday()));

      log.info("phoneNumber : {}", userEntity.getPhoneNumber());
      log.info("birthDay: {}", userEntity.getBirthday());
      log.info("userId: {}", userEntity.getId());
      log.info("claims: {}", claims);

      return Jwts.builder()
            // token Header
            .signWith(
                  Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                  SignatureAlgorithm.HS512
            )
              .setClaims(claims)
            .setIssuer("Page운영자") // iss: 발급자 정보
            .setIssuedAt(new Date()) // iat: 발급 시간
            .setExpiration(expiry) // exp: 만료 시간
            .setSubject(userEntity.getId())

            .compact();

   }

   public String createAccessKey(User userEntity) {

      String token = createToken(userEntity, SECRET_KEY, 100, ChronoUnit.DAYS);
      log.info("token - {}", token);

      return token;
   }

   // 토큰에서 클레임을 추출하는 로직을 분리
   private Claims getClaims(String token, String secretKey) {
      Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token).getBody();
      return claims;
   }

   /**
    * 클라이언트가 전송한 토큰을 디코딩하여 토큰의 위조 여부를 확인
    * 토큰을 json으로 파싱해서 클레임(토큰 정보)을 리턴
    *
    * @param token - 필터가 전달해 준 토큰
    * @return - 토큰 안에 있는 인증된 유저 정보를 반환
    */
   public TokenUserInfo validateAndGetTokenUserInfo(String token) {
      Claims claims = getClaims(token, SECRET_KEY);

      log.info("claims: {}", claims);

      return TokenUserInfo.builder()
            .userId(claims.get("userId", String.class))
            .email(claims.get("email", String.class))
            .phoneNumber(claims.get("phoneNumber", String.class))
            .role(Role.valueOf(claims.get("role", String.class)))
            .loginMethod(LoginMethod.valueOf(claims.get("loginMethod", String.class)))
            .build();
   }

   public String createGoogleToken(GoogleUserResponseDTO userResponseDTO, String secretKey, long duration, ChronoUnit unit) {

      Date expiry = Date.from(
            Instant.now().plus(100, ChronoUnit.DAYS)
      );

      // 토큰 생성
      Map<String, String> claims = new HashMap<>();
      claims.put("email", userResponseDTO.getGoogleEmail());
      claims.put("role", String.valueOf(userResponseDTO.getRole()));
      claims.put("loginMethod", String.valueOf(userResponseDTO.getLoginMethod()));
      claims.put("userId", userResponseDTO.getId());


      return Jwts.builder()
            // token Header
            .signWith(
                  Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                  SignatureAlgorithm.HS512
            )
            .setIssuer("Page운영자") // iss: 발급자 정보
            .setIssuedAt(new Date()) // iat: 발급 시간
            .setExpiration(expiry) // exp: 만료 시간
            .setSubject(userResponseDTO.getId())
            .setClaims(claims)
            .compact();

   }


   public String createGoogleAccessKey(GoogleUserResponseDTO userResponseDTO) {
      return createGoogleToken(userResponseDTO, SECRET_KEY, 100, ChronoUnit.DAYS);
   }
}












































