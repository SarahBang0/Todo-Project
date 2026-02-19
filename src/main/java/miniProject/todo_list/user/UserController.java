package miniProject.todo_list.user;

import lombok.RequiredArgsConstructor;
import miniProject.todo_list.todo.Service.JpaTodoServiceImpl;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Service.JpaUserServiceImpl;
import miniProject.todo_list.user.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService jpaUserService;


    // 모든 유저 조회
    @GetMapping("/api/users")
    public List<UserResponseDto> findAllUsers() {
        return jpaUserService.findAll();
    }

    // 특정 유저 조회
    @GetMapping("/api/users/{id}")
    public UserResponseDto findUser(@PathVariable Long id) {
        return jpaUserService.findUserById(id);
    }

    // 새로운 유저 생성
    @PostMapping("/api/users/create")
    public UserResponseDto createUser(@RequestBody UserJoinDto dto) {
        return jpaUserService.joinUser(dto);
    }

    // 유저 삭제
    @DeleteMapping("/api/users/{id}/delete")
    public void deleteUser(@PathVariable Long id) {
        jpaUserService.quitUser(id, id);
    }



}
