package com.example.tasktracker.model;


import com.example.tasktracker.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String id;

    private String username;

    private String email;

    private String password; //todo передача в одном направлении

    private Set<RoleType> role;
    // todo дописать роли

}
