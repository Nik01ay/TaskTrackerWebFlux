package com.example.tasktracker.mapper;

import com.example.tasktracker.entity.Task;
import com.example.tasktracker.model.TaskListModel;
import com.example.tasktracker.model.TaskModel;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(TaskMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = UserMapper.class)
public interface TaskMapper {

    Task requestToTask(TaskModel request);

    @Mapping(source = "taskId", target = "id")
    Task requestToTask(Long taskId, TaskModel request);
    TaskModel taskToResponse(Task task);

    List<TaskModel> taskListToResponseList(List<Task> tasks);

}
