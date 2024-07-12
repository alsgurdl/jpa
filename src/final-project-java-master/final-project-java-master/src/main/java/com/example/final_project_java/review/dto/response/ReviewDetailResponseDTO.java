package com.example.final_project_java.review.dto.response;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.review.entity.Review;
import com.example.final_project_java.userapi.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDetailResponseDTO {

    private int reviewNo;
    private String content;
    private String photo;
    private int rating;
    private int thumb; // 좋아요 수
    private String email;
    private String name;
    private String carName;
    private String stationName;
    private LocalDateTime regDate;
    private LocalDateTime updateDate;

    public ReviewDetailResponseDTO(Review review) {
        this.reviewNo = review.getReviewNo();
        this.content = review.getContent();
        this.photo = review.getPhoto();
        this.rating = review.getRating();
        this.thumb = review.getThumb();
        this.email = review.getEmail();
        this.name = review.getName();
        this.carName = review.getCarName();
        this.stationName = review.getStationName();
        this.regDate = review.getRegDate();
        this.updateDate = review.getUpdateDate();
    }

}