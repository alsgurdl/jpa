package com.example.final_project_java.userapi.repository;

import com.example.final_project_java.userapi.entity.LoginMethod;
import com.example.final_project_java.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

   // SNS 로그인 한 사람 회원가입 시킬지 말지 결정하는 메서드.
   boolean existsByEmailAndLoginMethod(String email, LoginMethod loginMethod);

   // 이메일 찾기
   Optional<User> findByEmail(@Param("email") String email);


   boolean existsByEmail(String email);

   // User Entity는 복합키를 사용하기 때문에 findById를 호출할 때는 UserId 클래스를 반드시 주어야 한다. (3개의 pk 객체)
   // 그래서 userId만으로 조회할 수 있는 메서드를 임의로 추가함.

   @Query("SELECT u FROM User u WHERE id = :userId")
   Optional<User> findUserByUserIdOnly(@Param("userId") String userId);

   /*
   // 이메일 충복 체크
   // boolean existsByEmail(String email);   로컬 로그인 용

   Optional<User> findByEmail(String email);

   // SNS 로그인 한 사람 회원가입 시킬지 말지 결정하는 메서드.
   boolean existsByEmailAndLoginMethod(String email, LoginMethod loginMethod);

   // 중복 로그인 찾기
   Optional<User> findByEmailAndLoginMethod(String email, LoginMethod loginMethod);

   boolean existsByEmail(String email);

   // User Entity는 복합키를 사용하기 때문에 findById를 호출할 때는 UserId 클래스를 반드시 주어야 한다. (3개의 pk 객체)
   // 그래서 userId만으로 조회할 수 있는 메서드를 임의로 추가함.

   @Query("SELECT u FROM User u WHERE id = :userId")
   Optional<User> findUserByUserIdOnly(@Param("userId") String userId);
*/


}















































