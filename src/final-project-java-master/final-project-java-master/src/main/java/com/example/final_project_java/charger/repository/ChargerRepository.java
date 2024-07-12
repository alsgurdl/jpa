package com.example.final_project_java.charger.repository;

import com.example.final_project_java.charger.Entity.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChargerRepository extends JpaRepository<ChargingStation, String>  {

    Optional<ChargingStation> findByStationName(String stationName);

}