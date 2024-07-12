package com.example.final_project_java.noti.repository;

import com.example.final_project_java.noti.entity.Noti;
import com.example.final_project_java.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotiRespository extends JpaRepository<Noti, String> {

   @Query("SELECT u FROM User u WHERE id = :userId")
   Optional<User> findUserByUserIdOnly(@Param("userId") String userId);

}















































