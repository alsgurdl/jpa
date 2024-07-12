package com.example.final_project_java.car.service;

import com.example.final_project_java.car.dto.request.RentCarRequestDTO;
import com.example.final_project_java.car.dto.request.RentCarResModifyRequestDTO;
import com.example.final_project_java.car.dto.response.RentCarDetailResponseDTO;
import com.example.final_project_java.car.dto.response.RentCarListResponseDTO;
import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.entity.RentCar;
import com.example.final_project_java.car.repository.CarRepository;
import com.example.final_project_java.car.repository.RentCarRepository;
import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RentCarService {

   private final CarRepository carRepository;
   private final UserRepository userRepository;
   private final RentCarRepository rentCarRepository;


   // 전기차 예약 목록 보기 (관리자가 모든 예약 보는것)
   public RentCarListResponseDTO getList() {
      List<RentCar> entityList = rentCarRepository.findAll();

      List<RentCarDetailResponseDTO> dtoList = entityList.stream()
              .map(RentCarDetailResponseDTO::new)
              .collect(Collectors.toList());

      return RentCarListResponseDTO.builder()
              .rentList(dtoList)
              .build();
   }


   // 사용자 Role 통해서 정보 불러오기
//   private Optional<User> getUserRole(String userId) {
//
//      Optional<User> user = rentCarRepository.findUserByUserIdOnly(userId);
//
//      return user;
//   }


   // 한 유저의 전기차 예약 목록을 가져온다.
   public RentCarListResponseDTO getRentListByUser(String userId) {

      List<RentCar> entityList = rentCarRepository.findByUserId(userId);
      List<RentCarDetailResponseDTO> dtoList = entityList.stream()
              .map(RentCarDetailResponseDTO::new)
              .collect(Collectors.toList());

      return RentCarListResponseDTO.builder()
              .rentList(dtoList)
              .build();
   }


   // 전기차 예약 상세보기 가져오기
   public RentCarListResponseDTO reservationInfo (int carNo) {

      RentCar reservation = getReservation(carNo);

      RentCarDetailResponseDTO responseDTO = new RentCarDetailResponseDTO(reservation);

      List<RentCarDetailResponseDTO> dtoList = Collections.singletonList(responseDTO);

      return RentCarListResponseDTO.builder()
              .rentList(dtoList)
              .build();
   }

   // 전기차 예약PK로 상세 정보 가져오기
   private RentCar getReservation(int carNo) {
      return rentCarRepository.findById(carNo).orElseThrow(
              () -> new RuntimeException("예약내역이 없습니다." + carNo)
      );
   }

   // 예약하기
   public RentCarDetailResponseDTO reservation (
           final RentCarRequestDTO requestDTO,
           final String userId,
           final String carId,
           final String carName,
//           final LocalDate rentDate,
//           final LocalDate turninDate,
           final LocalDateTime rentTime,
           final LocalDateTime turninTime
           ) {
      User user = getUser(userId);
      Car carInfo = getCarInfo(carId);
      log.info("carInfo - {}", carInfo);

      // 예외처리
//      if (rentCarRepository.existsByCarIdAndRentDateBetween(requestDTO.getCarId(), rentTime, turninTime)){
//         throw new IllegalStateException("what is this!!!!!");
//      }

      RentCar save = rentCarRepository.save(requestDTO.toEntity(user, carInfo, rentTime, turninTime));
      log.info("차량 예약 완료.");

      return null;
   }

   // 예약 상세
   private Car getCarInfo(String carId) {
      Car car = carRepository.findById(carId).orElseThrow(
              () -> new RuntimeException("차량정보가 존재하지 않습니다.")
      );
      return car;
   }

   // 유저
   private User getUser(String userId) {
      User user = userRepository.findUserByUserIdOnly(userId).orElseThrow(
              () -> new RuntimeException("회원정보가 존재하지 않습니다.")
      );
      return user;
   }

   private User getEmail(String email) {
      User user = userRepository.findByEmail(email).orElseThrow(
              () -> new RuntimeException("회원정보가 존재하지 않습니다.")
      );
      return user;
   }


   // 렌트카 예약 삭제 (유저가 본인 예약 삭제하는것)
//   public RentCarListResponseDTO delete(int carNo, String userId) {
//      RentCar deleteReservation = rentCarRepository.findById(carNo).orElseThrow(
//              () -> {
//                 log.error("예약번호가 조회되지 않아 삭제가 불가능합니다. 예약순서: {}", carNo);
//                 throw new RuntimeException("예약번호가 존재하지 않아 삭제에 실패했습니다.");
//              }
//      );
//      rentCarRepository.deleteById(carNo);
//
//      return getRentListByUser(userId);
//   }


   // 렌트카 예약 삭제
   public RentCarListResponseDTO delete(int carNo, String userId) {
      User user = getUser(userId);


      RentCar deleteReservation = rentCarRepository.findById(carNo).orElseThrow(
              () -> {
                 log.error("예약번호가 조회되지 않아 삭제가 불가능합니다. 예약순서: {}", carNo);
                 throw new RuntimeException("예약번호가 존재하지 않아 삭제에 실패했습니다.");
              }
      );
      rentCarRepository.deleteById(carNo);

      return getList();
   }

   // 렌트카 예약 수정
   public RentCarListResponseDTO update(RentCarResModifyRequestDTO requestDTO, int carNo, String email) {
      User user = getEmail(email);

      // 회원인지 확인
      if (user.getRole() != Role.COMMON) {
         throw new IllegalArgumentException("해당 사용자는 수정 권한이 없습니다.");
      }

      Optional<RentCar> targetEntity = rentCarRepository.findByCarNo(carNo);

      // 문자열 날짜 데이터를 바로 LocalDateTime으로 수정이 안됨
      // 일단은 java.util.Date으로 변환
      Date rentTime = new Date(requestDTO.getRentTime());
      Date turninTime = new Date(requestDTO.getTurninTime());

      RentCar reservation = targetEntity.orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다. " + carNo));

      // Date 객체를 LocalDateTime으로 변환 -> date객체.toInstant().atZone()....
      reservation.setRentTime(rentTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()); // 픽업시간 설정
      reservation.setTurninTime(turninTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()); // 반납 시간 설정
      reservation.setExtra(requestDTO.getExtra()); // 비고 설정

      log.info("completed reservation: {}", reservation);

      RentCar savedReservation = rentCarRepository.save(reservation);

      return getList();
   }

   // 달력에 예약한 날짜들 표시하기 (예약 못하게)
   public List<LocalDateTime> searchDate (String carId) {
//      RentCar rentCar = rentCarRepository.findCarIdByCarNo(carId);
//
//      if (rentCar == null) {
//         throw new RuntimeException("해당 ID의 차량을 찾을 수 없습니다.");
//      }
//      List<LocalDateTime> reservationDates = rentCarRepository.findReservedDatesByCarId(carId);

      return rentCarRepository.findReservedDatesByCarId(carId);
   }

   // carNo로 carId를 조회하는 메서드
   public String getCarIdByCarNo(int carNo) {
      // 데이터베이스나 다른 저장소에서는 carNo로 carId를 조회하는 로직
      return rentCarRepository.findCarIdByCarNo(carNo);
   }

}












































