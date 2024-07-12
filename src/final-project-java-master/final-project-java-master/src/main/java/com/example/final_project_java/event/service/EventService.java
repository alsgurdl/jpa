package com.example.final_project_java.event.service;

import com.example.final_project_java.aws.S3Service;
import com.example.final_project_java.event.Entity.Event;
import com.example.final_project_java.event.dto.request.EventCreateRequestDTO;
import com.example.final_project_java.event.dto.request.EventModifyRequestDTO;
import com.example.final_project_java.event.dto.response.EventDetailResponseDTO;
import com.example.final_project_java.event.dto.response.EventListResponseDTO;
import com.example.final_project_java.event.repository.EventRepository;
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
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    // 이벤트 목록 가져오기
    public EventListResponseDTO getList() {
        List<Event> entityList = eventRepository.findAll();

        log.info(entityList.toString());

        List<EventDetailResponseDTO> dtoList = entityList.stream()
                .map(EventDetailResponseDTO::new)
                .toList();

        log.info(dtoList.toString());

        return EventListResponseDTO.builder()
                .events(dtoList)
                .build();
    }

    public EventListResponseDTO create(
            final EventCreateRequestDTO requestDTO,
            final String uploadedFilePath,
            final String userId) {
        User user = getUser(userId);

        // 관리자만 글을 쓸 수 있게 처리
        if (user.getRole() != Role.ADMIN) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        eventRepository.save(requestDTO.toEntity(uploadedFilePath));
        log.info("이벤트 저장 완료! 제목 : {}", requestDTO.getTitle());

        return getList();
    }

    public EventListResponseDTO delete(final int eventNo, final String userId) throws Exception {
        User user = getUser(userId);

        // 관리자만 글을 삭제할 수 있게 처리
        if (user.getRole() != Role.ADMIN) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        Event event = eventRepository.findByEventNo(eventNo).orElseThrow(
                () -> {
                    log.error("글 번호가 존재하지 않아 삭제에 실패했습니다. - no : {}", eventNo);
                    throw new RuntimeException("글 번호가 존재하지 않아 삭제에 실패했습니다.");
                }
        );
        eventRepository.deleteById(String.valueOf(eventNo));

        return getList();
    }

    public EventListResponseDTO update(
            final int eventNo,
            final EventModifyRequestDTO requestDTO,
            final String uploadedFilePath,
            final String userId) throws Exception {
        User user = getUser(userId);

        // 관리자만 글을 수정할 수 있게 처리
        if (user.getRole() != Role.ADMIN) {
            log.warn("권한이 없습니다.");
            throw new RuntimeException("권한이 없습니다.");
        }

        Optional<Event> targetEntity = eventRepository.findByEventNo(eventNo);

        targetEntity.ifPresent(event -> {
            event.setTitle(requestDTO.getTitle());
            event.setContent(uploadedFilePath);
            eventRepository.save(event);
        });

        return getList();
    }

    public EventDetailResponseDTO detail(int eventNo) {

        Event event = eventRepository.findByEventNo(eventNo).orElseThrow(
                () -> {
                    log.warn("글 번호가 존재하지 않아 조회되지 않습니다. - no : {}", eventNo);
                    throw new RuntimeException("글 번호가 존재하지 않아 조회되지 않습니다.");
                }

        );
        return new EventDetailResponseDTO(event);

    }

    // 이미지 업로드
    public String uploadEventImage(MultipartFile eventImage) throws IOException {

        // 파일명을 유니크하게 변경
        String uniqueFileName
                = UUID.randomUUID() + "_" + eventImage.getOriginalFilename();

        return s3Service.uploadToS3BucketAdmin(eventImage.getBytes(), uniqueFileName);
    }

    private User getUser(String userId) {
        User user = userRepository.findUserByUserIdOnly(userId).orElseThrow(
                () -> new RuntimeException("회원 정보가 없습니다.")
        );
        return user;
    }

    public String findEventPath(int eventNo) {
        Event event = eventRepository.findByEventNo(eventNo).orElseThrow(() -> new RuntimeException());
        return event.getContent();
    }

}