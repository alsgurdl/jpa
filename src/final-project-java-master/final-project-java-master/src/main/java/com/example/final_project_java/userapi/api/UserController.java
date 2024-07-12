package com.example.final_project_java.userapi.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.userapi.dto.request.GoogleLoginRequestDTO;
import com.example.final_project_java.userapi.dto.request.LoginRequestDTO;
import com.example.final_project_java.userapi.dto.request.UserSignUpRequestDTO;
import com.example.final_project_java.userapi.dto.request.UserUpdateRequestDTO;
import com.example.final_project_java.userapi.dto.response.*;
import com.example.final_project_java.userapi.service.GoogleService;
import com.example.final_project_java.userapi.service.KakaoService;
import com.example.final_project_java.userapi.service.NaverService;
import com.example.final_project_java.userapi.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

   private final UserService userService;
   private final KakaoService kakaoService;
   private final NaverService naverService;
   private final GoogleService googleService;


   // 카카오 로그인
   @GetMapping("/kakaoLogin")
   public ResponseEntity<?> kakaoLogin(String code) {
      log.info("/api/auth/kakoLogin - GET code: {}", code);
      LoginResponseDTO responseDTO = kakaoService.kakaoService(code);

      return ResponseEntity.ok().body(responseDTO);
   }

   // 네이버 로그인
   @GetMapping("/naverLogin")
   public ResponseEntity<?> naverLogin(String code, String state) {
      log.info("/api/auth/kakoLogin - GET code: {}, state: {}", code, state);
      LoginResponseDTO responseDTO = naverService.naverService(code, state);

      return ResponseEntity.ok().body(responseDTO);
   }


   @PostMapping("/googleLogin")
   public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequestDTO request) {
      log.info("/api/auth/googleLogin - CODE: {}", request.getCode());
      GoogleLoginResponseDTO responseDTO = googleService.googleService(request.getCode());

      return ResponseEntity.ok().body(responseDTO);

   }

   // 이메일 중복 확인 요청 처리
   @GetMapping("/check")
   public ResponseEntity<?> check(String email) {
      if (email.trim().isEmpty()) {
         return ResponseEntity.badRequest()
                 .body("이메일 업습니다");
      }

      boolean resultFlag = userService.isDuplicateByEmail(email);
      log.info("중복?? - {}", resultFlag);
      return ResponseEntity.ok().body(resultFlag);

   }

   @PostMapping("/signup")
   public ResponseEntity<?> signUp(
           @Validated @RequestPart("user") UserSignUpRequestDTO dto,
           @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
           BindingResult result
   ) {
      log.info("/api/auth POST! - {}", dto);

      ResponseEntity<FieldError> resultEntity = getFieldErrorResponseEntity(result);
      log.info("resultEntity - {}", resultEntity);
      if (resultEntity != null) return resultEntity;

      try {
         String uploadedFilePath = null;
         if (profilePicture != null) {
            log.info("attached file name: {}", profilePicture.getOriginalFilename());
            // 전달받은 프로필 이미지를 먼저 지정된 경로에 저장한 후 저장 경로를 DB에 세팅하자.
            uploadedFilePath = userService.uploadProfileImage(profilePicture);
         }

         UserSignUpResponseDTO responseDTO = userService.create(dto, uploadedFilePath);
         return ResponseEntity.ok().body(responseDTO);
      } catch (IOException e) {
         log.info("Error - {}", e);
         throw new RuntimeException(e);
      }
   }

   @PostMapping("/signin")
   public ResponseEntity<?> signIn(@Validated @RequestBody LoginRequestDTO dto,
                                   BindingResult result) {
      log.info("/api/auth/signin- POST - {}", dto);

      ResponseEntity<FieldError> response = getFieldErrorResponseEntity(result);
      log.info("response - {}", response);
      if (response != null) return response;

      LoginResponseDTO responseDTO = userService.authenticate(dto);
      log.info("responseData: {}", responseDTO);
      return ResponseEntity.ok().body(responseDTO);

   }

   // 프로파일 사진 이미지 데이터를 클라이언트에게 응답 처리
   @GetMapping("/load-profile")
   public ResponseEntity<?> loadFile(
           @AuthenticationPrincipal TokenUserInfo userInfo
   ) {
      // 1. 프로필 사진의 경로부터 얻어야 한다.
      String filePath = userService.findProfilePath(userInfo.getUserId());
      log.info("filePath: {}", filePath);

      if (filePath != null) {
         return ResponseEntity.ok().body(filePath);
      } else {
         return ResponseEntity.notFound().build();
      }
   }

   // 로그아웃 처리
   @GetMapping("/logout")
   public ResponseEntity<?> logout(
           @AuthenticationPrincipal TokenUserInfo userInfo
   ) {
      log.info("/api/auth/logout - GET! - user: {}", userInfo.getEmail());

      String result = userService.logout(userInfo);
      return ResponseEntity.ok().body(result);
   }


   private MediaType findExtensionAndGetMediaType(String filePath) {

      // 파일 경로에서 확장자 추출
      // C:/todo_upload/nlskdnakscnlknklcs_abc.jpg
      String ext
              = filePath.substring(filePath.lastIndexOf(".") + 1);

      // 추출한 확장자를 바탕으로 MediaType을 설정 -> Header에 들어갈 Content-type이 됨.
      switch (ext.toUpperCase()) {
         case "JPG":
         case "JPEG":
            return MediaType.IMAGE_JPEG;
         case "PNG":
            return MediaType.IMAGE_PNG;
         case "GIF":
            return MediaType.IMAGE_GIF;
         default:
            return null;
      }
   }

   @GetMapping("/info")
   private ResponseEntity<?> myInfo(
           @AuthenticationPrincipal TokenUserInfo userInfo
   ) {
      log.info("/api/auth/info - GET");
      UserInfoListResponseDTO responseDTO = userService.retrieve(userInfo.getUserId());
      return ResponseEntity.ok().body(responseDTO);

   }

   @PutMapping("/update")
   private ResponseEntity<?> updateUserInfo(
           @AuthenticationPrincipal TokenUserInfo userInfo,
           @Validated @RequestBody UserUpdateRequestDTO requestDTO,
           BindingResult result
   ) {
      ResponseEntity<FieldError> validatedResult = getFieldErrorResponseEntity(result);
      if (validatedResult != null) return validatedResult;

      log.info("/api/auth/update - PUT");
      log.info("dto: {}", requestDTO);
      log.info("userInfo: {}", userInfo);

      try {
         return ResponseEntity.ok().body(userService.update(requestDTO, userInfo.getUserId()));
      } catch (Exception e) {
         return ResponseEntity.internalServerError()
                 .body(e.getMessage());
      }
   }

   @DeleteMapping("/delete")
   public ResponseEntity<?> deleteUser(@AuthenticationPrincipal TokenUserInfo userInfo) {
      log.info("/api/auth/delete - DELETE! - user: {}", userInfo.getEmail());
      log.info("userInfo - {}", userInfo);


      try {
         userService.deleteUser(userInfo.getUserId());
         return ResponseEntity.ok().body("User deleted successfully");
      } catch (Exception e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
               .body("Error deleting user: " + e.getMessage());
      }
   }


   private static ResponseEntity<FieldError> getFieldErrorResponseEntity(BindingResult result) {
      if (result.hasErrors()) {
         log.warn(result.toString());
         return org.springframework.http.ResponseEntity.badRequest()
                 .body(result.getFieldError());
      }
      return null;
   }


}












































