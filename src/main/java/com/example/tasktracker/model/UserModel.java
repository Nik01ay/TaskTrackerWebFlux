package com.example.tasktracker.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    private String id;

    private String username;

    private String email;

}
