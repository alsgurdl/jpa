package com.example.final_project_java.review.dto.request;

import com.example.final_project_java.review.entity.Review;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewCarModifyRequestDTO {

    @NotNull
    private String content;

    private int rating;

    public Review toEntity(String uploadedFilePath) {
        return Review.builder()
                .content(content)
                .rating(rating)
                .photo(uploadedFilePath)
                .build();
    }

}