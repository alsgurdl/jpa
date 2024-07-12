package com.example.final_project_java.userapi.service;

import com.example.final_project_java.car.repository.RentCarRepository;
import com.example.final_project_java.charger.repository.ReservationChargerRepository;
import com.example.final_project_java.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private final ReservationChargerRepository reservationChargerRepository;
    private final RentCarRepository rentCarRepository;
    private final ReviewRepository reviewRepository;

    public void cancelStationReservation(Integer reservationNo) {
        reservationChargerRepository.deleteByReservationNo(reservationNo);
        log.info("예약번호 '{}' 예약 취소됨.", reservationNo);
    }

    public void cancelCarReservation(Integer reservationNo) {
        rentCarRepository.deleteByReservationNo(reservationNo);
        log.info("예약번호 '{}' 예약 취소됨.", reservationNo);
    }

    public void deleteReviewByAdmin(Integer reviewNo) {
        reviewRepository.deleteByReviewNo(reviewNo);
        log.info("리뷰번호 '{}' 리뷰 삭제됨.", reviewNo);
    }
}
