package com.example.final_project_java.review.dto.response;

import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewListResponseDTO {
    
    private List<ReviewDetailResponseDTO> reviews; // 리뷰 목록들

}