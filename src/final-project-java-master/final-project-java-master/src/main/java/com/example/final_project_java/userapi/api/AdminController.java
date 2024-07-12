package com.example.final_project_java.userapi.api;

import com.example.final_project_java.car.dto.response.RentCarDetailResponseDTO;
import com.example.final_project_java.car.service.RentCarService;
import com.example.final_project_java.charger.dto.response.ReservationChargerResponseDTO;
import com.example.final_project_java.review.dto.response.ReviewDetailResponseDTO;
import com.example.final_project_java.review.service.ReviewService;
import com.example.final_project_java.userapi.service.AdminService;
import com.example.final_project_java.userapi.service.MyPageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final MyPageService myPageService;
    private final RentCarService rentCarService;
    private final ReviewService reviewService;
    private final AdminService adminService;

    @GetMapping("/station")
    public ResponseEntity<?> reservedStation() {
        log.info("/admin/station : GET!");

        List<ReservationChargerResponseDTO> responseDTO = myPageService.myReservedStation();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/station")
    public ResponseEntity<?> cancelReservation(@RequestParam Integer reservationNo) {
        log.info("/admin/station?reservationNo={} : DELETE!", reservationNo);
        System.out.println(reservationNo);

        adminService.cancelStationReservation(reservationNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/car")
    public ResponseEntity<?> reservedCar() {
        log.info("/admin/car : GET!");
        List<RentCarDetailResponseDTO> responseDTO = rentCarService.getList().getRentList();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/car")
    public ResponseEntity<?> cancelReservationCar(@RequestParam Integer reservationNo) {
        log.info("/admin/car?reservationNo={} : DELETE!", reservationNo);
        System.out.println(reservationNo);

        adminService.cancelCarReservation(reservationNo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/review")
    public ResponseEntity<?> writtenReview() {
        log.info("/admin/review : GET!");

        List<ReviewDetailResponseDTO> responseDTO = reviewService.getList().getReviews();
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/review")
    public ResponseEntity<?> deleteReview(@RequestParam Integer reviewNo) {
        log.info("/admin/review?reviewNo={} : DELETE!", reviewNo);
        System.out.println(reviewNo);

        adminService.deleteReviewByAdmin(reviewNo);
        return ResponseEntity.ok().build();
    }

}
