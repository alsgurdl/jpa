package com.example.final_project_java.charger.service;

import com.example.final_project_java.charger.Entity.ChargingStation;
import com.example.final_project_java.charger.Entity.ReservationCharger;
import com.example.final_project_java.charger.dto.request.ReservationChargerModifyRequestDTO;
import com.example.final_project_java.charger.dto.request.ReservationChargerRequestDTO;
import com.example.final_project_java.charger.dto.response.ChargerDetailResponseDTO;
import com.example.final_project_java.charger.dto.response.ChargerListResponseDTO;
import com.example.final_project_java.charger.dto.response.ReservationChargerResponseDTO;
import com.example.final_project_java.charger.dto.response.UnavailableTimeDTO;
import com.example.final_project_java.charger.repository.ChargerRepository;
import com.example.final_project_java.charger.repository.ReservationChargerRepository;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChargerService {

    private final UserRepository userRepository;
    private final ChargerRepository chargerRepository;
    @Autowired
    private final ReservationChargerRepository reservationRepository;

    // 전국 전기차 충전소 목록
    public ChargerListResponseDTO retrieve() {
        List<ChargingStation> entityList = chargerRepository.findAll();

        List<ChargerDetailResponseDTO> dtoList = entityList.stream()
                .map(ChargerDetailResponseDTO::new)
                .toList();

        return ChargerListResponseDTO.builder()
                .chargers(dtoList)
                .build();
    }

    // Plug & Go 충전소 목록
    public ChargerListResponseDTO plugAndGoRetrieve() {
        List<ChargingStation> entityList = chargerRepository.findAll();

        List<ChargerDetailResponseDTO> dtoList = entityList.stream()
                .filter(ChargingStation::isReservation_possible)
                .map(ChargerDetailResponseDTO::new)
                .sorted(Comparator.comparing(ChargerDetailResponseDTO::getChargingPrice).reversed())
                .toList();

        return ChargerListResponseDTO.builder()
                .chargers(dtoList)
                .build();
    }

    // 전기차 충전소 예약
    public ReservationChargerResponseDTO reservation(
            final ReservationChargerRequestDTO requestDTO,
            final String email,
            final String chargeId
            ) {

        User user = getUser(email);
        ChargingStation station = getChargingStation(chargeId);

        // 한 유저가 한번에 충전소 2개 이상 예약 금지
        if (reservationRepository.existsByUser(user)) {
            throw new IllegalStateException("이미 예약하신 충전소가 있습니다.");
        }

        LocalDateTime startTime = requestDTO.getStartDate().toLocalDateTime();
        LocalDateTime endTime = startTime.plusMinutes(requestDTO.getSelectedValue());

        // 겹치는 예약 시간 확인
        if (reservationRepository.existsByStationAndTimeOverlap(station.getStationId(), startTime, endTime)) {
            throw new IllegalStateException("해당 시간대에 이미 예약된 충전소가 있습니다.");
        }

        reservationRepository.save(requestDTO.toEntity(user, station));
        log.info("충전소 예약 완료!");

        return null;
    }

    // 충전소 예약 취소
    public ChargerListResponseDTO delete(final String chargeNo) throws Exception {
        ReservationCharger reservation = reservationRepository.findById(chargeNo).orElseThrow(
                () -> {
                    log.error("ID가 존재하지 않아 삭제에 실패하였습니다. 예약번호 : {}", chargeNo);
                    throw new RuntimeException("ID가 존재하지 않아 삭제에 실패했습니다.");
                }
        );
        reservationRepository.deleteById(chargeNo);

        return retrieve();
    }

    // 충전소 예약 수정
    public ReservationChargerResponseDTO update(ReservationChargerModifyRequestDTO requestDTO, final String chargeNo) throws Exception {
        Optional<ReservationCharger> targetEntity = reservationRepository.findById(chargeNo);

        targetEntity.ifPresent(reservation -> {
            reservation.setRentTime(requestDTO.getRentTime());
            reservationRepository.save(reservation);
        });

        return null;
    }

    private ChargingStation getChargingStation(String chargeId) {
        ChargingStation station = chargerRepository.findById(chargeId).orElseThrow(
                () -> new RuntimeException("충전소 정보가 없습니다.")
        );
        return station;
    }

    private User getUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }

    public List<UnavailableTimeDTO> getUnavailableTimes(String chargeId) {
        List<ReservationCharger> reservations = reservationRepository.findAllByStation_StationId(chargeId);
        List<UnavailableTimeDTO> unavailableTimes = new ArrayList<>();
        for (ReservationCharger reservation : reservations) {
            unavailableTimes.add(new UnavailableTimeDTO(reservation.getRentTime(), reservation.getEndTime()));
        }
        return unavailableTimes;
    }

}
