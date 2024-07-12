package com.example.final_project_java.event.dto.request;

import com.example.final_project_java.event.Entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateRequestDTO {

    @NotBlank
    @Size(min = 2, max = 30)
    private String title;

    public Event toEntity(String uploadedFilePath) {
        return Event.builder()
                .title(title)
                .content(uploadedFilePath)
                .build();
    }

}