package com.example.tasktracker.mapper;

import com.example.tasktracker.entity.User;
import com.example.tasktracker.model.UserListModel;
import com.example.tasktracker.model.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User requestToUser(UserModel request);

    @Mapping(source = "userId", target = "id")
    User requestToUser(Long userId, UserModel request);
    UserModel userToResponse(User user);

    List<UserModel> userListToResponseList(List<User> users);

    default UserListModel userListToUserListResponse(List<User> users){
        UserListModel response = new UserListModel();

        response.setUsers(users.stream()
                .map(this::userToResponse)
                .collect(Collectors.toList()));

        return response;
    }
}
