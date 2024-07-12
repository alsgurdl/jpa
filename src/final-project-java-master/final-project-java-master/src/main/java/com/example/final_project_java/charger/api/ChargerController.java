package com.example.final_project_java.charger.api;

import com.example.final_project_java.auth.TokenUserInfo;
import com.example.final_project_java.charger.dto.request.ReservationChargerModifyRequestDTO;
import com.example.final_project_java.charger.dto.request.ReservationChargerRequestDTO;
import com.example.final_project_java.charger.dto.request.PageDTO;
import com.example.final_project_java.charger.dto.response.ChargerListResponseDTO;
import com.example.final_project_java.charger.dto.response.ReservationChargerResponseDTO;
import com.example.final_project_java.charger.dto.response.UnavailableTimeDTO;
import com.example.final_project_java.charger.service.ChargerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/charge")
public class ChargerController {

    private final ChargerService chargerService;

    // 충전소 목록 요청
    @GetMapping("/list")
    public ResponseEntity<?> retrieveChargerList() {
        log.info("/charge/list : GET!");

        ChargerListResponseDTO responseDTO = chargerService.retrieve();
        return ResponseEntity.ok().body(responseDTO);
    }
    
    // Plug & Go 충전소 목록 요청
    @GetMapping("/reservation")
    public ResponseEntity<?> plugAndGoRetrieveChargerList() {
        log.info("/charge/reservation : GET!");

        ChargerListResponseDTO responseDTO = chargerService.plugAndGoRetrieve();
        return ResponseEntity.ok().body(responseDTO);
    }

    // 충전소 예약하기
    @PostMapping("/reservation")
    public ResponseEntity<?> reservationCharge(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody ReservationChargerRequestDTO requestDTO,
            BindingResult result
    ) {
        log.info("/charge/reservation : POST - dto : {}", requestDTO);
        log.info("TokenUserInfo : {}", userInfo);

        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        ReservationChargerResponseDTO responseDTO = chargerService.reservation(requestDTO, requestDTO.getEmail(), requestDTO.getChargeId());
        return ResponseEntity.ok().body(responseDTO);
    }

    // 충전소 예약 취소
    @DeleteMapping("/reservation/{no}")
    public ResponseEntity<?> deleteReservation(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PathVariable("no") String chargeNo
    ) {
        log.info("/charge/reservation/{} : DELETE", chargeNo);

        if (chargeNo == null || chargeNo.trim().equals("")) {
            return ResponseEntity.badRequest().body("ID를 전달해 주세요!");
        }

        try {
            ChargerListResponseDTO responseDTO = chargerService.delete(chargeNo);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 충전소 예약 변경
    @PatchMapping("/reservation")
    public ResponseEntity<?> updateReservation(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody ReservationChargerModifyRequestDTO requestDTO,
            BindingResult result
    ) {
        ResponseEntity<List<FieldError>> validatedResult = getValidatedResult(result);
        if (validatedResult != null) return validatedResult;

        try {
            return ResponseEntity.ok().body(chargerService.update(requestDTO, requestDTO.getChargeNo()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(e.getMessage());
        }
    }

    // 입력값 검증(Validation)의 결과를 처리해 주는 전역 메서드
    private static ResponseEntity<List<FieldError>> getValidatedResult(BindingResult result) {
        if (result.hasErrors()) { // 입력값 검증 단계에서 문제가 있었다면 true
            List<FieldError> fieldErrors = result.getFieldErrors();
            fieldErrors.forEach(err -> {
                log.warn("invalid client data - {}", err.toString());
            });
            return ResponseEntity
                    .badRequest()
                    .body(fieldErrors);
        }
        return null;
    }

    @GetMapping("/unavailable-times/{stationId}")
    public List<UnavailableTimeDTO> getUnavailableTimes(@PathVariable String stationId) {
        return chargerService.getUnavailableTimes(stationId);
    }
}