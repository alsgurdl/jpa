package com.example.final_project_java.car.repository;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.entity.RentCar;
import com.example.final_project_java.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RentCarRepository extends JpaRepository<RentCar, Integer> {

//    boolean existsUser (User user); // 동일 유저 찾기
//
//    boolean existsRentDate (LocalDateTime rentDate, LocalDateTime turninDate); // 동일 대여 날

    //boolean existsCar (String carId); // 대여 확인

    Car findByCarId(String carId);

    Optional<RentCar> findByCarNo(int carNo);

    boolean existsByCarId(String carId);

//    boolean existsByUserIdAndRentDateBetween(String userId, String carId, LocalDateTime startDate, LocalDateTime endDate); // 유저랑 대여 날에 중복이 있는지

//    boolean existsByCarIdAndRentDateBetween(String carId, LocalDate rentDate, LocalDate turninDate); // 차랑 대여 날에 중복이 있는지

    // 해당 차량에 예약된 날짜를 가져온다.
    @Query("SELECT r.rentTime FROM RentCar r WHERE r.carId = :carId")
    List<LocalDateTime> findReservedDatesByCarId(@Param("carId") String carId);

    @Query("SELECT r.carId FROM RentCar r WHERE r.carNo = :carNo")
    String findCarIdByCarNo(@Param("carNo") int carNo);

    List<RentCar> findByUserId(String userId);

    @Query("SELECT u FROM User u WHERE id = :userId")
    Optional<User> findUserByUserIdOnly(@Param("userId") String userId);

    // 관리자페이지용 예약번호기준 예약 취소
    @Modifying
    @Transactional
    @Query("DELETE FROM RentCar WHERE reservationNo = :reservationNo")
    void deleteByReservationNo(@Param("reservationNo") Integer reservationNo);

}















































