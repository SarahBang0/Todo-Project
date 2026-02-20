package miniProject.todo_list.user.Service;

import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Dto.UserUpdateDto;
import miniProject.todo_list.user.Entity.User;

import java.util.List;

public interface UserService {

    UserResponseDto joinUser(UserJoinDto dto);

    Long quitUser(Long requestId, Long targetId);

    UserResponseDto findUserById(Long id);

    List<UserResponseDto> findAll();

    UserResponseDto updateUser(Long userId, UserUpdateDto dto);

    UserResponseDto findUserByEmail(String email);

    List<UserResponseDto> findUserByUserName(String userName);


}
