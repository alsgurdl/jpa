package com.example.final_project_java.event.dto.response;

import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventListResponseDTO {

    private List<EventDetailResponseDTO> events; // 이벤트 목록들

}