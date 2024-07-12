package com.example.final_project_java.userapi.dto.response;

import lombok.*;

import java.util.List;

@Setter @Getter @ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoListResponseDTO {

    private List<UserInfoResponseDTO> users;
}
