package com.example.final_project_java.review.dto.request;

import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.review.entity.Review;
import com.example.final_project_java.userapi.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCarCreateRequestDTO {

    @NotBlank
    private String content;

    @NotNull
    private int rating;

    private String carName;

    public Review toEntity(User user, Car car, String uploadedFilePath) {
        return Review.builder()
                .content(content)
                .photo(uploadedFilePath)
                .rating(rating)
                .name(user.getName())
                .email(user.getEmail())
                .carId(car.getCarId())
                .carName(car.getCarName())
                .build();
    }

}