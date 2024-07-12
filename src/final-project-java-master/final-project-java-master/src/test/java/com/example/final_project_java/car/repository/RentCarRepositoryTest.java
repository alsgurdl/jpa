//package com.example.final_project_java.car.repository;
//
//import com.example.final_project_java.car.entity.Car;
//import com.example.final_project_java.car.entity.CarOptions;
//import com.example.final_project_java.car.entity.RentCar;
//import com.example.final_project_java.userapi.entity.LoginMethod;
//import com.example.final_project_java.userapi.entity.User;
//import com.example.final_project_java.userapi.repository.UserRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.sql.Time;
//import java.time.LocalDateTime;
//import java.time.Year;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//@Rollback(false)
//class RentCarRepositoryTest {
//
//   @Autowired
//   private UserRepository userRepository;
//
//   @Autowired
//   private CarRepository carRepository;
//
//   @Autowired
//   private RentCarRepository rentCarRepository;
//
//   @Test
//   public void testRentCarUserRelationship() {
//      User user = User.builder()
//            .name("John Doe")
//            .email("john.doe@example.com")
//            .loginMethod(LoginMethod.GOOGLE)
//            .build();
//      userRepository.save(user);
//
//      RentCar rentCar = RentCar.builder()
//            .userId(user)
//            .rentDate(LocalDateTime.now())
//            .rentTime(Time.valueOf("10:00:00"))
//            .turninDate(LocalDateTime.now().plusDays(1))
//            .turninTime(Time.valueOf("10:00:00"))
//            .rentCarPrice(100)
//            .phoneNumber("123-456-7890")
//            .build();
//      rentCarRepository.save(rentCar);
//   }
//
//   @Test
//   @DisplayName("전기차 추가 ")
//   void testCarInsert() {
//      // given
//      Car car = Car.builder()
//            .carName("testCarNameDelete")
//            .carCompany("testCarCompanyDelete")
//            .maximumPassenger(4)
//            .carYear(Year.of(2020))
//            .carPrice(900000)
//            .carOptions(CarOptions.SAFETY)
//            .build();
//
//
//      // when
//      carRepository.save(car);
//
//      // then
//   }
//
//   @Test
//   @DisplayName("전체 조회")
//   void findAllCars() {
//      // given
//
//      // when
//      List<Car> all = carRepository.findAll();
//
//      // then
//      assertEquals(all.size(), 2);
//   }
//
//   @Test
//   @DisplayName("전기차 id 통해 탐색")
//   void findOneCar() {
//      // given
//      String id = "1a8cb186-9817-4fcb-b5da-fcfa9d7f7e8d";
//      // when
//      Car car = carRepository.findById(id).orElseThrow();
//      // then
//      assertEquals(car.getCarName(), "testCarName");
//
//   }
//
//   @Test
//   @DisplayName("삭제")
//   void delete() {
//      // given
//      String id = "1fe9c58f-f2ea-4693-a1ee-2e5bb1e258ab";
//      // when
//      carRepository.deleteById(id);
//      // then
//   }
//
//   @Test
//   @DisplayName("수정")
//   void update() {
//      // given
//      String id = "1a8cb186-9817-4fcb-b5da-fcfa9d7f7e8d";
//      // when
//      Car car = carRepository.findById(id).orElseThrow();
//      car.setCarOptions(CarOptions.CONVENIENCE);
//
//      carRepository.save(car);
//      // then
////      String option = car.getCarOptions().toString();
//
//      assertEquals((car.getCarOptions().toString()), "CONVENIENCE");
//
//   }
//
//
//}









































