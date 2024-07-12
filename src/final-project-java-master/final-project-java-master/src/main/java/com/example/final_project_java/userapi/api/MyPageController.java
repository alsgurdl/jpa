package com.example.final_project_java.userapi.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.car.dto.response.RentCarDetailResponseDTO;
import com.example.final_project_java.car.service.RentCarService;
import com.example.final_project_java.charger.dto.response.ChargerListResponseDTO;
import com.example.final_project_java.charger.dto.response.ReservationChargerResponseDTO;
import com.example.final_project_java.charger.service.ChargerService;
import com.example.final_project_java.review.dto.response.ReviewDetailResponseDTO;
import com.example.final_project_java.review.service.ReviewService;
import com.example.final_project_java.userapi.service.AdminService;
import com.example.final_project_java.userapi.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

   private final MyPageService myPageService;
   private final AdminService adminService;
   private final ReviewService reviewService;
   private final RentCarService rentCarService;

   // 예약한 충전소 목록 불러오기
   @GetMapping
   public ResponseEntity<?> myReserveStation() {
      log.info("/mypage : GET!");

      List<ReservationChargerResponseDTO> responseDTO = myPageService.myReservedStation();
      return ResponseEntity.ok().body(responseDTO);
   }

   @DeleteMapping
   public ResponseEntity<?> cancelReservation(@RequestParam Integer reservationNo) {
      log.info("/mypage?reservationNo={} : DELETE!", reservationNo);
      System.out.println(reservationNo);

      myPageService.cancelStationReservation(reservationNo);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/car")
   public ResponseEntity<?> reservedCar() {
      log.info("/mypage/car : GET!");
      List<RentCarDetailResponseDTO> responseDTO = rentCarService.getList().getRentList();
      return ResponseEntity.ok().body(responseDTO);
   }

   @DeleteMapping("/car")
   public ResponseEntity<?> cancelReservationCar(@RequestParam Integer reservationNo) {
      log.info("/mypage/car?reservationNo={} : DELETE!", reservationNo);
      System.out.println(reservationNo);

      adminService.cancelCarReservation(reservationNo);
      return ResponseEntity.ok().build();
   }

   @GetMapping("/review")
   public ResponseEntity<?> writtenReview() {
      log.info("/mypage/review : GET!");

      List<ReviewDetailResponseDTO> responseDTO = reviewService.getList().getReviews();
      return ResponseEntity.ok().body(responseDTO);
   }

   @DeleteMapping("/review")
   public ResponseEntity<?> deleteReview(@RequestParam Integer reviewNo) {
      log.info("/mypage/review?reviewNo={} : DELETE!", reviewNo);
      System.out.println(reviewNo);

      adminService.deleteReviewByAdmin(reviewNo);
      return ResponseEntity.ok().build();
   }

}