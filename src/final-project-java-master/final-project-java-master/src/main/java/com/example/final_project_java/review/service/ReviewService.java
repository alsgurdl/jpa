package com.example.final_project_java.review.service;

import com.example.final_project_java.aws.S3Service;
import com.example.final_project_java.car.entity.Car;
import com.example.final_project_java.car.repository.CarRepository;
import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.charger.repository.ChargerRepository;
import com.example.final_project_java.review.dto.request.ReviewCarCreateRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewChargeCreateRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewCarModifyRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewChargeModifyRequestDTO;
import com.example.final_project_java.review.dto.response.ReviewDetailResponseDTO;
import com.example.final_project_java.review.dto.response.ReviewListResponseDTO;
import com.example.final_project_java.review.entity.Review;
import com.example.final_project_java.review.repository.ReviewRepository;
import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final ChargerRepository chargerRepository;
    private final S3Service s3Service;

    public ReviewListResponseDTO getList() {
        List<Review> entityList = reviewRepository.findAll();

        List<ReviewDetailResponseDTO> dtoList = entityList.stream()
                .map(ReviewDetailResponseDTO::new)
                .toList();

        return ReviewListResponseDTO.builder()
                .reviews(dtoList)
                .build();
    }

    public ReviewDetailResponseDTO detail(int reviewNo) {
        Review review = reviewRepository.findByReviewNo(reviewNo).orElseThrow(
                () -> {
                    log.warn("글 번호가 존재하지 않아 조회되지 않습니다. - no : {}", reviewNo);
                    throw new RuntimeException("글 번호가 존재하지 않아 조회되지 않습니다.");
                }
        );
        return new ReviewDetailResponseDTO(review);
    }

    // 렌트카 리뷰 등록
    public ReviewListResponseDTO createCar(
            final ReviewCarCreateRequestDTO requestDTO,
            final String uploadFilePath,
            final String email,
            final String carName) {
        User user = userRepository.findByEmail(email).orElseThrow();
        log.info("조회한 user: {}", user.toString());

        Car car = carRepository.findByCarName(carName).orElseThrow();
        log.info("선택한 차 : {}", car.toString());

        Review review = requestDTO.toEntity(user, car, uploadFilePath);
        log.info("완성된 Review: {}", review);

        reviewRepository.save(review);
        log.info("리뷰 작성 완료!");

        return getList();
    }

    // 충전소 리뷰 등록
    public ReviewListResponseDTO createCharge(
            final ReviewChargeCreateRequestDTO requestDTO,
            final String uploadedFilePath,
            final String email,
            final String stationName) {
        User foundUser = userRepository.findByEmail(email).orElseThrow();
        log.info("조회한 user: {}", foundUser.toString());

        ChargingStation station = chargerRepository.findById(stationName).orElseThrow();
        log.info("조회한 충전소: {}", station.toString());

        Review review = requestDTO.toEntity(foundUser, station, uploadedFilePath);
        log.info("완성된 Review: {}", review);

        reviewRepository.save(review);
        log.info("리뷰 작성 완료!");

        return getList();
    }

    public ReviewListResponseDTO delete(final int reviewNo, final String email) throws Exception {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            log.warn("사용자를 찾을 수 없습니다.");
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        User user = userOptional.get();

        Review review = reviewRepository.findByReviewNo(reviewNo).orElseThrow(
                () -> new RuntimeException("리뷰를 찾을 수 없습니다.")
        );

        log.info("email: {}", email);
        log.info("review 작성자: {}", review.getEmail());
        log.info("권한: {}", user.getRole());
        log.info("이메일: {}", user.getEmail());

        if (!user.getRole().equals(Role.ADMIN) && !email.equals(review.getEmail())) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        reviewRepository.delete(review);

        return getList();
    }

    // 전기차 리뷰 수정
    public ReviewListResponseDTO update(
            final int reviewNo,
            final ReviewCarModifyRequestDTO requestDTO,
            final String uploadedFilePath,
            final String email
    ) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);

        Optional<Review> targetEntity = reviewRepository.findByReviewNo(reviewNo);

        // 작성자만 글을 수정할 수 있게 처리
        if (!email.equals(targetEntity.get().getEmail())) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        targetEntity.ifPresent(review -> {
            review.setContent(requestDTO.getContent());
            review.setPhoto(uploadedFilePath);
            review.setRating(requestDTO.getRating());
            reviewRepository.save(review);
        });

        return getList();
    }

    // 충전소 리뷰 수정
    public ReviewListResponseDTO update(
            final int reviewNo,
            final ReviewChargeModifyRequestDTO requestDTO,
            final String uploadedFilePath,
            final String email
    ) throws Exception {
        Optional<User> user = userRepository.findByEmail(email);

        Optional<Review> targetEntity = reviewRepository.findByReviewNo(reviewNo);

        // 작성자만 글을 수정할 수 있게 처리
        if (!email.equals(targetEntity.get().getEmail())) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        targetEntity.ifPresent(review -> {
            review.setContent(requestDTO.getContent());
            review.setPhoto(uploadedFilePath);
            review.setRating(requestDTO.getRating());
            reviewRepository.save(review);
        });

        return getList();
    }

    public String uploadReviewImage(MultipartFile reviewImage) throws IOException {

        // 파일명을 유니크하게 변경
        String uniqueFileName = UUID.randomUUID() + "_" + reviewImage.getOriginalFilename();

        return s3Service.uploadToS3Bucket(reviewImage.getBytes(), uniqueFileName);
    }

    public String findReviewPath(int reviewNo) {
        Review review = reviewRepository.findByReviewNo(reviewNo).orElseThrow(() -> new RuntimeException());
        return review.getPhoto();
    }

}