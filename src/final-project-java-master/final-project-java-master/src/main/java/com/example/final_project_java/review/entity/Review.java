package com.example.final_project_java.review.entity;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.userapi.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

import static jakarta.persistence.CascadeType.ALL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int reviewNo; // 이용후기 글 번호

    private String content; // 후기 내용

    private String photo; // 후기 사진

    @CreationTimestamp
    @Column(columnDefinition = "DATETIME(6)")
    private LocalDateTime regDate;

    @UpdateTimestamp
    @Column(columnDefinition = "DATETIME(6)")
    private LocalDateTime updateDate;

    private int rating;

    private int thumb; // 좋아요 수

    private String email;

    private String name;

    private String carId;

    private String carName;

    private String stationId;

    private String stationName;

}