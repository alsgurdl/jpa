package com.example.final_project_java.userapi.entity;

import lombok.*;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserId implements Serializable {

    private String id;
    private String email;
    private LoginMethod loginMethod;


}
