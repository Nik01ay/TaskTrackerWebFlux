package com.example.tasktracker.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserListModel {

    private List<UserModel> users = new ArrayList<>();
}
