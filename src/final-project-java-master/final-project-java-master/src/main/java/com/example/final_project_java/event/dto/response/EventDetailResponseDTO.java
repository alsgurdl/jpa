package com.example.final_project_java.event.dto.response;

import com.example.final_project_java.event.Entity.Event;
import lombok.*;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDetailResponseDTO {

    private int eventNo;
    private String title;
    private String content;
    private String eventCategory;

    public EventDetailResponseDTO(Event event) {
        this.eventNo = event.getEventNo();
        this.title = event.getTitle();
        this.content = event.getContent();
        this.eventCategory = String.valueOf(event.getEventCategory());
    }

}