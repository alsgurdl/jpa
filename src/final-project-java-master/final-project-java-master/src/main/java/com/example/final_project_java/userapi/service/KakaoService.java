package com.example.final_project_java.userapi.service;

import com.example.final_project_java.auth.TokenProvider;
import com.example.final_project_java.userapi.dto.response.KakaoUserDTO;
import com.example.final_project_java.userapi.dto.response.LoginResponseDTO;
import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class KakaoService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    // 이메일 중복확인
    public boolean isDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            log.info("중복된 이메일입니다. -> {}", email);
            return true;
        } else return false;
    }

    // AccessKey를 새롭게 발급받아 Map으로 포장해 주는 메서드.
    private Map<String, String> getTokenMap(User user) {
        String accessToken = tokenProvider.createAccessKey(user);

        Map<String, String> token = new HashMap<>();
        token.put("access_token", accessToken);
        return token;
    }

    //  카카오 토큰 받을때 필요한 것
    @Value("${kakao.client_id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect_url}")
    private String KAKAO_REDIRECT_URL;

    @Value("${kakao.client_secret}")
    private String KAKAO_CLIENT_SECRET;


    // 사용자 정보 얻어오는 호출 등 사용
    public LoginResponseDTO kakaoService (String code) {
        // 인가 코드를 통해 토큰 발급 받기
        String accessToken = getKakaoAccessToken(code);
        log.info("access_token:{} ", accessToken);

        //토큰을 통해 사용자 정보 가져오기
        KakaoUserDTO userDTO = getKakaoUserInfo(accessToken);

        // 카카오 로그인 이메일 중복 체크
        // 이메일이 중복되지 않았으면 이전에 로그인한적 없는 신규 회원 -> DB에 저장.
        if(!isDuplicate(userDTO.getKakaoAccount().getEmail())) {
            User newKakaoUser = userDTO.toEntity(null, passwordEncoder);
            Map<String, String> tokenMap = getTokenMap(newKakaoUser);
            newKakaoUser.setAccessToken(tokenMap.get("access_token"));
            User saved = userRepository.save(newKakaoUser);
            return new LoginResponseDTO(newKakaoUser, tokenMap);
        }
        // 이메일이 중복되었으면 로그인한 이메일 -> DB에 넣지 않는다.
        User foundUser
                = userRepository.findByEmail(userDTO.getKakaoAccount().getEmail()).orElseThrow();

        // 우리 사이트에서 사용하는 jwt를 생성.
        Map<String, String> token = getTokenMap(foundUser);

        // 기존에 로그인했던 사용자의 access token값을 update
        foundUser.changeAccessToken(token.get("access_token"));
        userRepository.save(foundUser);

        return new LoginResponseDTO(foundUser, token);

    }

    // 사용자 정보 가져오기
    private KakaoUserDTO getKakaoUserInfo(String accessToken) {
        // 요청 uri
        String requestURI = "https://kapi.kakao.com/v2/user/me";

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 보내기
        RestTemplate template = new RestTemplate();
        ResponseEntity<KakaoUserDTO> responseEntity
                = template.exchange(requestURI, HttpMethod.GET, new HttpEntity<>(headers), KakaoUserDTO.class);

        // 응답 바디 꺼내기
        KakaoUserDTO responseData = responseEntity.getBody();
        log.info("user profile:{}", responseData);

        return responseData;

    }

    // 토큰만 뽑는 부분
    private String getKakaoAccessToken(String code) {
        // 요청 uri
        String requestURI = "https://kauth.kakao.com/oauth/token";

        // 요청 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 요청 바디(파라미터) 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); // 카카오 공식 문서 기준 값으로 세팅
        params.add("client_id", KAKAO_CLIENT_ID); // 카카오 디벨로퍼 REST API 키
        params.add("redirect_uri", KAKAO_REDIRECT_URL); // 카카오 디벨로퍼 등록된 redirect uri
        params.add("code", code); // 프론트에서 인가 코드 요청시 전달받은 코드값
        params.add("client_secret", KAKAO_CLIENT_SECRET); // 카카오 디벨로퍼 client secret(활성화 시 추가해 줘야 함)

        // 헤더와 바디 정보를 합치기 위해 HttpEntity 객체 생성
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, headers);

        // 카카오 서버로 POST 통신
        RestTemplate template = new RestTemplate();
        ResponseEntity<Map> responseEntity
                = template.exchange(requestURI, HttpMethod.POST, requestEntity, Map.class);


        // 응답 데이터에서 필요한 정보를 가져오기
        Map<String, Object> responseData = (Map<String, Object>) responseEntity.getBody();
        log.info("토큰 요청 응답 데이터:{}", responseData);


        // access_token의 토큰값을 스트링 타입으로 받고 리턴하기
        return (String) responseData.get("access_token");

    }


}
