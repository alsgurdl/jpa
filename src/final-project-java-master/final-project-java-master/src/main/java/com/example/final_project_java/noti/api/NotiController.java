package com.example.final_project_java.noti.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.noti.dto.request.NotiCreateRequestDTO;
import com.example.final_project_java.noti.dto.request.NotiModifyRequestDTO;
import com.example.final_project_java.noti.dto.response.NotiDetailResponseDTO;
import com.example.final_project_java.noti.dto.response.NotiListResponseDTO;
import com.example.final_project_java.noti.entity.Noti;
import com.example.final_project_java.noti.service.NotiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/noti")
public class NotiController {

   private final NotiService notiService;

   // 이용방법 목록 요청
   @GetMapping("/info")
   public ResponseEntity<?> getNotiList() {
      log.info("/noti/info GET! 목록 조회 요청!");

      NotiListResponseDTO responseDTO = notiService.getList();

      return ResponseEntity.ok().body(responseDTO);
   }

   // 이용방법 상세 요청
   @GetMapping("/info/{id}")
   public ResponseEntity<?> retriveNotiInfo(@PathVariable("id") String notiId) {
      log.info("/noti/{} GET Request! Noti 상세", notiId);

      try {
         NotiListResponseDTO responseDTO = notiService.retriveOne(notiId);
         log.info("responseDTO - {}", responseDTO);
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         log.info("조회 에러 발생!  id: {}", notiId);
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   // 이용방벙 추가 요청
   @PostMapping
   public ResponseEntity<?> createNoti(@AuthenticationPrincipal TokenUserInfo userInfo,
                                       @Validated @RequestBody NotiCreateRequestDTO requestDTO,
                                       BindingResult result) {
      log.info("/noti POST! - dto: {}", requestDTO);
      log.info("TokenUserInfo: {}", userInfo);
      log.info("userId: {}", userInfo.getUserId());

      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) return validatedResult;

      try {
         NotiListResponseDTO responseDTO = notiService.create(requestDTO, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   // 이용방벙 수정 요청
   @PatchMapping("/{id}")
   public ResponseEntity<?> updateNotiInfo(@AuthenticationPrincipal TokenUserInfo userInfo,
                                           @Validated @RequestBody NotiModifyRequestDTO requestDTO,
                                           BindingResult result) {
      log.info("/noti/{} Noti Patch! Noti 수정", requestDTO.getNotiId());
      log.info("/requestDTO: {}", requestDTO);
      log.info("requestDTO name: {}", requestDTO.getNotiWriter());

      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) return validatedResult;

      try {
         NotiListResponseDTO responseDTO = notiService.update(requestDTO, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.internalServerError().body(e.getMessage());
      }

   }

   // 이용방법 삭제 요청
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteNoti(@AuthenticationPrincipal TokenUserInfo userInfo,
                                       @PathVariable("id") String notiId) {
      log.info("/noti/{} DELETE Request!", notiId);

      if (notiId == null || notiId.trim().equals("")) {
         return ResponseEntity.badRequest()
               .body("ID를 전달해 주세요.");
      }

      try {
         NotiListResponseDTO responseDTO = notiService.delete(notiId, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }

   }

   @PatchMapping("/views/{id}")
   public ResponseEntity<?> updateNotiViews(@PathVariable("id") String notiId) {
      try {
         NotiDetailResponseDTO responseDTO = notiService.updateViews(notiId);
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         log.error("Error updating views for id: {}", notiId, e);
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }



   // 입력값 검증(Validation)의 결과를 처리해 주는 전역 메서드
   public static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
      if (result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
         List<FieldError> fieldErrors = result.getFieldErrors();
         fieldErrors.forEach(err -> {
            log.warn("invalid client data - {}", err.toString());
         });
         return ResponseEntity
               .badRequest()
               .body(fieldErrors);
      }
      return null;
   }





}












































