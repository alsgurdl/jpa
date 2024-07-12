package com.example.final_project_java.userapi.service;


import com.example.final_project_java.auth.TokenProvider;
import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.aws.S3Service;
import com.example.final_project_java.userapi.dto.request.LoginRequestDTO;
import com.example.final_project_java.userapi.dto.request.UserSignUpRequestDTO;
import com.example.final_project_java.userapi.dto.request.UserUpdateRequestDTO;
import com.example.final_project_java.userapi.dto.response.LoginResponseDTO;
import com.example.final_project_java.userapi.dto.response.UserInfoListResponseDTO;
import com.example.final_project_java.userapi.dto.response.UserInfoResponseDTO;
import com.example.final_project_java.userapi.dto.response.UserSignUpResponseDTO;
import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserService {

   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;
   private final TokenProvider tokenProvider;
   private final S3Service s3Service;

//   @Value("${upload.path}")
//   private String uploadRootPath;

   public boolean isDuplicateByEmail(String email) {
      if (userRepository.existsByEmail(email)) {
         log.warn("이메일이 중복되었습니다. - {}", email);
         return true;
      } else return false;
   }

   public UserSignUpResponseDTO create(final UserSignUpRequestDTO dto, final String uploadRootPath) {
      String email = dto.getEmail();

      if (isDuplicateByEmail(email)) {
         throw new RuntimeException("중복된 이메일 입니다.");
      }

      // 패스워드 인코딩
      String encoded = passwordEncoder.encode(dto.getPassword());
      dto.setPassword(encoded);

      User saved = userRepository.save(dto.toEntity(uploadRootPath));
      log.info("회원 가입 정상 수행됨! - saved user - {}", saved);

      return new UserSignUpResponseDTO(saved);
   }


   public String uploadProfileImage(MultipartFile profilePicture) throws IOException {

      String uniqueFileName = UUID.randomUUID() + "_" + profilePicture.getOriginalFilename();

      return s3Service.uploadToS3Bucket(profilePicture.getBytes(), uniqueFileName);
   }

   public LoginResponseDTO authenticate(LoginRequestDTO dto) {

      // 이메일을 통해 회원 정보 조회
      User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new RuntimeException("존재하지 않는 아이디 입니다."));

      // 패스워드 검증
      String rawPassword = dto.getPassword(); // 입력한 비번
      String encodedPassword = user.getPassword(); // DB에 저장된 암호화된 비번


      if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
         throw new RuntimeException("비밀번호가 틀렸습니다.");
      }

      log.info("{}님 로그인 성공!", user.getName());

      // 로그인 성공 후에 클라이언트에게 뭘 리턴해 줄 것인가?
      // -> JWT를 클라이언트에 발급해 주어야 한다! -> 로그인 유지를 위해!
      Map<String, String> token = getTokenMap(user);
      log.info("token - {}", token);

      user.setAccessToken(token.get("access_token"));
      userRepository.save(user);

      return new LoginResponseDTO(user, token);

   }

   // AccessKey와 RefreshKey를 새롭게 발급받아 Map으로 포장해 주는 메서드.
   private Map<String, String> getTokenMap(User user) {
      String accessToken = tokenProvider.createAccessKey(user);
      log.info("access token - {}", accessToken);

      Map<String, String> token = new HashMap<>();
      token.put("access_token", accessToken);
      return token;
   }

   public String findProfilePath(String userId) {
      User user
            = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
      return user.getProfilePicture();
   }

   public String logout(TokenUserInfo userInfo) {
      User foundUser = userRepository.findById(userInfo.getUserId())
            .orElseThrow();

      String accessToken = foundUser.getAccessToken();
      // accessToken이 null이 아니라면 카카오 로그인을 한 애겠지?
      if (accessToken != null) {
         String reqURI = "https://kapi.kakao.com/v1/user/logout";
         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", "Bearer " + accessToken);

         ResponseEntity<String> responseData
               = new RestTemplate().exchange(reqURI, HttpMethod.POST, new HttpEntity<>(headers), String.class);
         foundUser.changeAccessToken(null);
         userRepository.save(foundUser);

         return responseData.getBody();
      }
      return null;
   }

   // 회원 정보 수정하기
   public UserInfoListResponseDTO update(final UserUpdateRequestDTO requestDTO,
                                         final String userId) throws Exception {
      Optional<User> targetEntity = userRepository.findUserByUserIdOnly(userId);

      targetEntity.ifPresent(user -> {
         log.info("user Entity Present");
         user.setName(requestDTO.getUserName());
         user.setPhoneNumber(requestDTO.getPhoneNumber());
         user.setBirthday(LocalDate.parse(requestDTO.getBirthDay()));

         userRepository.save(user);
      });

      return retrieve(userId);

   }

   public UserInfoListResponseDTO retrieve(String userId) {
      Optional<User> user = userRepository.findUserByUserIdOnly(userId);

      List<UserInfoResponseDTO> collect = user.stream()
            .map(UserInfoResponseDTO::new)
            .collect(Collectors.toList());

      return UserInfoListResponseDTO.builder()
            .users(collect)
            .build();

   }

   public void deleteUser(String userId) {
      Optional<User> user = getUser(userId);

      if (user.isPresent()) {
         userRepository.delete(user.get());
         log.info("회원 삭제 ID: {}", userId);
      } else {
         log.warn("User not found with ID: {}", userId);
         throw new RuntimeException("User not found with ID: " + userId);
      }
   }

   private Optional<User> getUser(String userId) {
      Optional<User> user = userRepository.findUserByUserIdOnly(userId);

      return user;
   }




}












































