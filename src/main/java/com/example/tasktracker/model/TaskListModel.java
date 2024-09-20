package com.example.tasktracker.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskListModel {

    private List<TaskModel> tasks = new ArrayList<>();
}
