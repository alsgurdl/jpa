package com.example.final_project_java.review.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.review.dto.request.ReviewCarCreateRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewChargeCreateRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewCarModifyRequestDTO;
import com.example.final_project_java.review.dto.request.ReviewChargeModifyRequestDTO;
import com.example.final_project_java.review.dto.response.ReviewDetailResponseDTO;
import com.example.final_project_java.review.dto.response.ReviewListResponseDTO;
import com.example.final_project_java.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/list")
    public ResponseEntity<?> getList() {
        log.info("/review/list - GET");

        List<ReviewDetailResponseDTO> responseDTO = reviewService.getList().getReviews();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 리뷰 상세보기 요청
    @GetMapping("/list/{no}")
    public ResponseEntity<?> retrieve(
            @PathVariable("no") int reviewNo
    ) {
        log.info("/review/list/{} - GET", reviewNo);

        try {
            ReviewDetailResponseDTO responseDTO = reviewService.detail(reviewNo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.info("조회 에러 발생! no : {}", reviewNo);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // 렌트카 리뷰 등록 요청
    @PostMapping("/car")
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
            @Validated @RequestPart(value = "car") ReviewCarCreateRequestDTO requestDTO,
            BindingResult result
    ) {
        log.info("/review/car - POST, dto : {}", requestDTO);
        log.info("Email: {}", userInfo.getEmail());

        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            return ResponseEntity.badRequest().body(fieldErrors);
        }

        try {
            String uploadedFilePath = null;
            if (reviewImage != null) {
                log.info("attached file name: {}", reviewImage.getOriginalFilename());
                uploadedFilePath = reviewService.uploadReviewImage(reviewImage);
            }

            ReviewListResponseDTO responseDTO = reviewService.createCar(requestDTO, uploadedFilePath, userInfo.getEmail(), requestDTO.getCarName());
            return ResponseEntity.ok().body(responseDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 충전소 리뷰 등록 요청
    @PostMapping("/charge")
    public ResponseEntity<?> createReview(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
            @Validated @RequestPart(value = "charge") ReviewChargeCreateRequestDTO requestDTO,
            BindingResult result
    ) {
        log.info("/review - POST, dto : {}", requestDTO);
        log.info("stationId : {}", requestDTO.getStationName());

        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        try {
            String uploadedFilePath = null;
            if (reviewImage != null) {
                log.info("file name: {}", reviewImage.getOriginalFilename());
                uploadedFilePath = reviewService.uploadReviewImage(reviewImage);
            }

            ReviewListResponseDTO responseDTO = reviewService.createCharge(requestDTO, uploadedFilePath, userInfo.getEmail(), requestDTO.getStationName());
            return ResponseEntity.ok().body(responseDTO);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 리뷰 삭제 요청
    @DeleteMapping("/{no}")
    public ResponseEntity<?> deleteReview(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("no") int reviewNo
    ) {
        log.info("/review/{} - DELETE", reviewNo);
        log.info("userInfo - {}", userInfo);

        try {
            ReviewListResponseDTO responseDTO = reviewService.delete(reviewNo, userInfo.getEmail());
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 전기차 리뷰 수정 요청
    @PatchMapping("/car/{no}")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
            @Validated @RequestPart(value = "car") ReviewCarModifyRequestDTO requestDTO,
            @PathVariable("no") int reviewNo,
            BindingResult result
    ) {
        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        log.info("/review/{} - PATCH", reviewNo);

        try {
            String uploadedFilePath = null;
            if (reviewImage != null) {
                log.info("file name: {}", reviewImage.getOriginalFilename());
                uploadedFilePath = reviewService.uploadReviewImage(reviewImage);
            }
            return ResponseEntity.ok()
                    .body(reviewService.update(reviewNo, requestDTO, uploadedFilePath, userInfo.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    // 충전소 리뷰 수정 요청
    @PatchMapping("/charge/{no}")
    public ResponseEntity<?> updateReview(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart(value = "reviewImage", required = false) MultipartFile reviewImage,
            @Validated @RequestPart(value = "charge") ReviewChargeModifyRequestDTO requestDTO,
            @PathVariable("no") int reviewNo,
            BindingResult result
    ) {
        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        log.info("/review/{} - PATCH", reviewNo);

        try {
            String uploadedFilePath = null;
            if (reviewImage != null) {
                log.info("file name: {}", reviewImage.getOriginalFilename());
                uploadedFilePath = reviewService.uploadReviewImage(reviewImage);
            }
            return ResponseEntity.ok().body(reviewService.update(reviewNo, requestDTO, uploadedFilePath, userInfo.getEmail()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/load-review")
    public ResponseEntity<?> loadFile(int reviewNo) {
        String filePath = reviewService.findReviewPath(reviewNo);
        log.info("filePath: {}", filePath);

        if (filePath != null) {
            return ResponseEntity.ok().body(filePath);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private MediaType findExtensionAndGetMediaType(String filePath) {
        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()) {
            case "JPG": case "JPEG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }
    }

    // 입력값 검증(Validation)의 결과를 처리해 주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity.badRequest().body(fieldErrors);
        }
        return null;
    }

}