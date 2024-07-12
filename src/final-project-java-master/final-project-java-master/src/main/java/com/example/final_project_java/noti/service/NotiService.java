package com.example.final_project_java.noti.service;

import com.example.final_project_java.noti.dto.request.NotiCreateRequestDTO;
import com.example.final_project_java.noti.dto.request.NotiModifyRequestDTO;
import com.example.final_project_java.noti.dto.response.NotiDetailResponseDTO;
import com.example.final_project_java.noti.dto.response.NotiListResponseDTO;
import com.example.final_project_java.noti.entity.Noti;
import com.example.final_project_java.noti.repository.NotiRespository;
import com.example.final_project_java.userapi.entity.Role;
import com.example.final_project_java.userapi.entity.User;
import com.example.final_project_java.userapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class NotiService {

   private final NotiRespository notiRespository;
   private final UserRepository userRepository;

   // 이용방법 목록
   public NotiListResponseDTO getList() {

      List<Noti> entityList = notiRespository.findAll();

      List<NotiDetailResponseDTO> dtoList = entityList.stream()
            .map(NotiDetailResponseDTO::new)
            .collect(Collectors.toList());

      return NotiListResponseDTO.builder()
            .notiList(dtoList)
            .build();
   }

   // 이용방법 상세
   public NotiListResponseDTO retriveOne(String notiId) {

      Noti noti = getNoti(notiId);

      NotiDetailResponseDTO notiDetailResponseDTO = new NotiDetailResponseDTO(noti);

      List<NotiDetailResponseDTO> dtoList = Collections.singletonList(notiDetailResponseDTO);

      return NotiListResponseDTO.builder()
            .notiList(dtoList)
            .build();
   }

   // 이용방법 추가
   public NotiListResponseDTO create(final NotiCreateRequestDTO requestDTO, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다! 나가주세요");
         throw new RuntimeException("권한 없습니다.");
      }
      
      notiRespository.save(requestDTO.toEntity());

      return getList();
      
   }

   // 이용방법 삭제
   public NotiListResponseDTO delete(final String notiId, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다!");
         throw new RuntimeException("권한 없습니다.");
      }

      notiRespository.findById(notiId).orElseThrow(
            () -> {
               log.error("id가 존재하지 않습니다. ID: {}", notiId);
               throw new RuntimeException("ID가 존재하지 않아 삭제에 실패했습니다.");
            }
      );
      notiRespository.deleteById(notiId);
      return getList();
   }

   // 이용방법 수정
   public NotiListResponseDTO update(final NotiModifyRequestDTO requestDTO, final String userId) {
      Optional<User> user = getUser(userId);

      if (user.get().getRole() != Role.ADMIN) {
         log.warn("권한 없습니다!");
         throw new RuntimeException("권한 없습니다.");
      }

      Noti noti = notiRespository.findById(requestDTO.getNotiId()).orElseThrow(
            () -> {
               log.info("수정할 Notification 없습니다.");
               throw new RuntimeException("수정할 Notification 없습니다.");
            }
      );

      noti.setNotiTitle(requestDTO.getNotiTitle());
      noti.setNotiContent(requestDTO.getNotiContent());
      noti.setUpdatedDate(requestDTO.getUpdatedDate());

      notiRespository.save(noti);

      return getList();
   }

   public NotiDetailResponseDTO updateViews(String notiId) throws Exception {
      // Retrieve the notification
      Noti noti = notiRespository.findById(notiId)
            .orElseThrow(() -> new Exception("Notification not found"));

      // Update the view count
      noti.setViews(noti.getViews() + 1);

      // Save the updated notification
      notiRespository.save(noti);

      // Return the updated notification details
      return new NotiDetailResponseDTO(noti);
   }


   // 이용방법 아이디 통해서 불러오기
   private Noti getNoti(String notiId) {
      Noti noti = notiRespository.findById(notiId).orElseThrow(
            () -> new RuntimeException("조회 이용방법 없습니다. " + notiId)
      );

      return noti;

   }

   // 사용자 Role 통해서 정보 불러오기
   private Optional<User> getUser(String userId) {
      Optional<User> user = userRepository.findUserByUserIdOnly(userId);

      return user;
   }



}












































