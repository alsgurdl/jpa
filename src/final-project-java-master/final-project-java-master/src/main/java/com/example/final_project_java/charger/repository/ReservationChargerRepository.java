package com.example.final_project_java.charger.repository;

import com.example.final_project_java.charger.Entity.ReservationCharger;
import com.example.final_project_java.userapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationChargerRepository extends JpaRepository<ReservationCharger, String> {
    boolean existsByUser(User user);

    List<ReservationCharger> findAllByStation_StationId(String stationId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ReservationCharger WHERE reservationNo = :reservationNo")
    void deleteByReservationNo(@Param("reservationNo") Integer reservationNo);

    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
            "FROM ReservationCharger r " +
            "WHERE r.station.id = :stationId " +
            "AND ((:startTime BETWEEN r.rentTime AND r.endTime) " +
            "OR (:endTime BETWEEN r.rentTime AND r.endTime) " +
            "OR (r.rentTime BETWEEN :startTime AND :endTime))")
    boolean existsByStationAndTimeOverlap(@Param("stationId") String stationId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
}