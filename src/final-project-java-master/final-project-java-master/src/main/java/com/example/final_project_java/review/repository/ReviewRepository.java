package com.example.final_project_java.review.repository;

import com.example.final_project_java.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {

    Optional<Review> findByReviewNo(int reviewNo);

    // 관리자페이지용 리뷰번호기준 리뷰 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Review WHERE reviewNo = :reviewNo")
    void deleteByReviewNo(@Param("reviewNo") Integer reviewNo);
}