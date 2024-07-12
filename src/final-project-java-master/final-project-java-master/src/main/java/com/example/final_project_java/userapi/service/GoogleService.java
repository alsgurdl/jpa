package com.example.final_project_java.userapi.service;

import com.example.final_project_java.auth.TokenProvider;
import com.example.final_project_java.userapi.dto.response.GoogleLoginResponseDTO;
import com.example.final_project_java.userapi.dto.response.GoogleUserResponseDTO;
import com.example.final_project_java.userapi.dto.response.LoginResponseDTO;
import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GoogleService {

   private final UserRepository userRepository;
   private final TokenProvider tokenProvider;
   private final PasswordEncoder encoder;

   @Value("${sns.google.client.id}")
   private String googleClientId;

   @Value("${sns.google.client.secret}")
   private String googleClientSecret;

   @Value("${sns.google.redirect.uri}")
   private String googleRedirectUri;

   public GoogleLoginResponseDTO googleService(String idTokenString) {
      GoogleIdToken idToken = verifyGoogleIdToken(idTokenString);
      if (idToken != null) {
         GoogleIdToken.Payload payload = idToken.getPayload();

         String email = payload.getEmail();
         String name = (String) payload.get("name");
         String pictureUrl = (String) payload.get("picture");

         GoogleUserResponseDTO userResponseDTO = new GoogleUserResponseDTO(email, name, pictureUrl);
         String accessToken = tokenProvider.createGoogleAccessKey(userResponseDTO);

         log.info("google access token : {}", accessToken);

         if (!isDuplicate(userResponseDTO.getGoogleEmail())) {

            userRepository.save(userResponseDTO.toEntity(accessToken, encoder));
         }

         User foundUser = userRepository.findByEmail(userResponseDTO.getGoogleEmail()).orElseThrow();

         Map<String, String> token = getTokenMap(foundUser);
         log.info("accessToken : {}", token);

         return new GoogleLoginResponseDTO(foundUser, token);
      } else {
         throw new RuntimeException("Invalid ID token.");
      }
   }

   private GoogleIdToken verifyGoogleIdToken(String idTokenString) {
      GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
            GsonFactory.getDefaultInstance())
            .setAudience(Collections.singletonList(googleClientId))
            .build();

      try {
         return verifier.verify(idTokenString);
      } catch (GeneralSecurityException | IOException e) {
         log.error("Failed to verify ID token: ", e);
         return null;
      }
   }



   private Map<String, String> getTokenMap(User user) {
      String accessToken = tokenProvider.createAccessKey(user);

      Map<String, String> token = new HashMap<>();
      token.put("access_token", accessToken);
      return token;
   }

   public boolean isDuplicate(String email) {
      return userRepository.existsByEmailAndLoginMethod(email, LoginMethod.GOOGLE);
   }

}














































