package com.example.final_project_java.event.Entity;

import jakarta.persistence.*;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventNo; // 글 번호

    private String title; // 이벤트 제목

    private String content; // 사진 url

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventCategory eventCategory = EventCategory.ACTIVE_EVENT; // 진행 중인 이벤트 (기본값)

}