package com.example.final_project_java.car.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.car.dto.request.CarCreateRequestDTO;
import com.example.final_project_java.car.dto.request.CarModifyRequestDTO;
import com.example.final_project_java.car.dto.response.CarListResponseDTO;
import com.example.final_project_java.car.repository.RentCarRepository;
import com.example.final_project_java.car.service.CarService;
import com.example.final_project_java.car.service.RentCarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Car API", description = "전기차 조회, 추가, 삭제, 수정 api 입니다. ")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/car")
public class CarController {

   private final CarService carService;
   private final RentCarService rentCarService;

   // 전기차 추가
   @PostMapping
   public ResponseEntity<?> createCar(@AuthenticationPrincipal TokenUserInfo userInfo,
                                      @Validated @RequestBody CarCreateRequestDTO requestDTO,
                                      BindingResult result
   ) {
      log.info("/car POST! - dto: {}", requestDTO);
      log.info("TokenUserInfo: {}", userInfo);
      log.info("userId - {}", userInfo.getUserId());
      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) return validatedResult;

      try {
         CarListResponseDTO responseDTO = carService.create(requestDTO, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         log.info("추가 못했습니다");
         e.printStackTrace();
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   // 전기차 목록 요청
   @GetMapping("/res")
   public ResponseEntity<?> getCarList() {
      log.info("/car GET! 목록 조회!!!");
      CarListResponseDTO responseDTO = carService.getList();
      log.info("car info : {}", responseDTO);

      return ResponseEntity.ok().body(responseDTO);
   }

   // 전기차 상세보기 요청
   @GetMapping("/res/{id}")
   public ResponseEntity<?> retrieveCarInfo(@PathVariable("id") String carId) {
      log.info("/car/{} GET request!", carId);
      try {
         CarListResponseDTO responseDTO = carService.retrieve(carId);
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         log.info("조회 에러 발생! id: {}", carId);
         return ResponseEntity.internalServerError().body(e.getMessage());
      }
   }

   // 전기차 삭제
   @DeleteMapping("/{id}")
   public ResponseEntity<?> deleteCar(@AuthenticationPrincipal TokenUserInfo userInfo,
                                      @PathVariable("id") String carId) {
      log.info("/car/{} DELETE request!", carId);

      if (carId == null || carId.trim().equals("")) {
         return ResponseEntity.badRequest()
               .body("ID를 전달해 주세요!");
      }

      try {
         CarListResponseDTO responseDTO = carService.delete(carId, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         return ResponseEntity.badRequest().body(e.getMessage());
      }

   }

   // 전기차 수정
   @PatchMapping("/{id}")
   public ResponseEntity<?> updateCarInfo(@AuthenticationPrincipal TokenUserInfo userInfo,
                                          @Validated @RequestBody CarModifyRequestDTO requestDTO,
                                          BindingResult result) {
      log.info("/car PATCH!! 수정");
      log.info("/requestDTO: {}", requestDTO);
      log.info("requestDTO name: {}", requestDTO.getCarName());
      log.info("requestDTO carOption -{}", requestDTO.getCarOptions());
      ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
      if (validatedResult != null) return validatedResult;

      try {
         CarListResponseDTO responseDTO = carService.update(requestDTO, userInfo.getUserId());
         return ResponseEntity.ok().body(responseDTO);
      } catch (Exception e) {
         e.printStackTrace();
         return ResponseEntity.internalServerError()
               .body(e.getMessage());
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
