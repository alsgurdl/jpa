package com.example.final_project_java.car.service;

import com.example.final_project_java.car.dto.request.CarCreateRequestDTO;
import com.example.final_project_java.car.dto.request.CarModifyRequestDTO;
import com.example.final_project_java.car.dto.response.CarDetailResponseDTO;
import com.example.final_project_java.car.dto.response.CarListResponseDTO;
import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.entity.CarOptions;
import com.example.final_project_java.car.repository.CarRepository;
import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.entity.UserId;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.final_project_java.car.entity.CarOptions.*;
import static com.example.final_project_java.car.entity.CarOptions.valueOf;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CarService {

   private final CarRepository carRepository;
   private final UserRepository userRepository;

   public CarListResponseDTO create(final CarCreateRequestDTO requestDTO, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다! 나가주세요");
         throw new RuntimeException("추가 권한 없습니다!");
      }

      carRepository.save(requestDTO.toEntity());
      return getList();
   }

   // 전기차 목록
   public CarListResponseDTO getList() {

      List<Car> entityList = carRepository.findAll();

      List<CarDetailResponseDTO> dtoList = entityList.stream()
            .map(CarDetailResponseDTO::new)
            .collect(Collectors.toList());

      return CarListResponseDTO.builder()
            .carList(dtoList)
            .build();

   }


   // 전기차 상세보기 가져오기
   public CarListResponseDTO retrieve(String carId) {

      Car car = getCar(carId);

      CarDetailResponseDTO carDetailResponseDTO = new CarDetailResponseDTO(car);

      List<CarDetailResponseDTO> dtoList = Collections.singletonList(carDetailResponseDTO);

      return CarListResponseDTO.builder()
            .carList(dtoList)
            .build();

      /*
      Car car = getCar(carId);

      Optional<Car> entityList = carRepository.findById(carId);

      List<CarDetailResponseDTO> dtoList = entityList.stream()
            .map(CarDetailResponseDTO::new)
            .collect(Collectors.toList());

      return CarListResponseDTO.builder()
            .carList(dtoList)
            .build();
      */
   }

   // 전기차 아이디 통해서 불러오기
   private Car getCar(String carId) {
      return carRepository.findById(carId).orElseThrow(
            () -> new RuntimeException("조회 차 없습니다" + carId)
      );
   }

   // 사용자 Role 통해서 정보 불러오기
   private Optional<User> getUser(String userId) {
      Optional<User> user = userRepository.findUserByUserIdOnly(userId);

      return user;
   }

   // 전기차 삭제
   public CarListResponseDTO delete(final String carId, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다! 나가주세요");
         throw new RuntimeException("권한 없습니다!");
      }

      Car car = carRepository.findById(carId).orElseThrow(
            () -> {
               log.error("id가 존재하지 않습니다! ID: {}", carId);
               throw new RuntimeException("id가 존재하지 않아 삭제에 실패했습니다.");
            }
      );
      carRepository.deleteById(carId);
      return getList();
   }


   public CarListResponseDTO update(final CarModifyRequestDTO requestDTO, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다! 나가주세요");
         throw new RuntimeException("권한 없습니다!");
      }

      Car car = carRepository.findById(requestDTO.getCarId()).orElseThrow(
            () -> {
               log.info("수정할 전기차 없습니다.");
               throw new RuntimeException("수정할 차 없습니다.");
            }
      );

      car.setCarName(requestDTO.getCarName());
      car.setCarCompany(requestDTO.getCarCompany());
      car.setMaximumPassenger(requestDTO.getMaximumPassenger());
      car.setCarYear(requestDTO.getCarYear());
      car.setCarPrice(requestDTO.getCarPrice());
      car.setCarPicture(requestDTO.getCarPicture());
      car.setCarOptions(valueOf(requestDTO.getCarOptions().toUpperCase()));
      car.setCategory(requestDTO.getCategory());

      carRepository.save(car);

      return getList();


   }
}












































