package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Service.JpaUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaTodoServiceImplTest {

    @Autowired
    JpaTodoServiceImpl jpaTodoService;
    @Autowired
    JpaUserServiceImpl jpaUserService;

    @Test
    @DisplayName("할 일을 생성하고 조회할 수 있어야 한다.")
    void createTodo() {
        // given (유저 생성)
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        // 생성용 DTO 준비
        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);

        // when
        TodoResponseDto response = jpaTodoService.createTodo(createDto);
        TodoResponseDto todoById = jpaTodoService.findTodoById(response.getId());

        // then
        assertThat(response.getTask()).isEqualTo("clean the room");
        assertThat(response.getId()).isNotNull();
        assertThat(todoById.getId()).isEqualTo(response.getId());
        assertThat(todoById.getTask()).isEqualTo("clean the room");
    }

    @Test
    @DisplayName("삭제 성공 / 할 일을 삭제할 수 있어야 한다.")
    void deleteTodo() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        jpaTodoService.deleteTodo(todoId);


        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.findTodoById(todoId));
        assertThat(e.getMessage()).isEqualTo("조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("삭제 실패 / 존재하지 않는 할 일은 삭제할 수 없다")
    void deleteTodoFail() {
        Long temp = 12345L;
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.deleteTodo(temp));
        assertThat(e.getMessage()).isEqualTo("삭제 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("상태 수정 성공 / 할 일 상태를 미완료에서 완료로 바꿀 수 있다")
    void updateToComplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        // 상태 변경
        jpaTodoService.updateToComplete(todoId);
        TodoResponseDto updated = jpaTodoService.findTodoById(todoId);

        assertThat(updated.isComplete()).isTrue();
    }

    @Test
    @DisplayName("상태 수정 실패 / 이미 완료인 할 일은 완료로 바꿀 수 없다")
    void updateToCompleteFail() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        // 상태 변경
        jpaTodoService.updateToComplete(todoId);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToComplete(todoId));
        assertThat(e.getMessage()).isEqualTo("이미 완료된 상태입니다.");
    }

    @Test
    @DisplayName("상태 수정 실패 / 존재하지 않는 할 일은 완료로 바꿀 수 없다")
    void updateToCompleteFail2() {
        Long temp = 12345L;
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToComplete(temp));
        assertThat(e.getMessage()).isEqualTo("조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("상태 수정 성공 / 할 일 상태를 완료에서 미완료로 바꿀 수 있다")
    void updateToUncomplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        jpaTodoService.updateToComplete(todoId);
        jpaTodoService.updateToUncomplete(todoId);

        TodoResponseDto updated = jpaTodoService.findTodoById(todoId);

        assertThat(updated.isComplete()).isFalse();
    }

    @Test
    @DisplayName("상태 수정 실패 / 미완료인 할 일은 미완료로 바꿀 수 없다")
    void updateToUncmpleteFail() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToUncomplete(todoId));
        assertThat(e.getMessage()).isEqualTo("미완료인 할 일은 미완료로 바꿀 수 없습니다.");
    }

    @Test
    @DisplayName("상태 수정 실패 / 존재하지 않는 할 일은 완료로 바꿀 수 없다")
    void updateToUncompleteFail2() {
        Long temp = 12345L;
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToUncomplete(temp));
        assertThat(e.getMessage()).isEqualTo("조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 목록 전체 조회")
    void findAll() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);

        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);

        assertThat(jpaTodoService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("할 일 목록 전체 최신순 (내림차순) 조회")
    void findAllDesc() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId);

        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);
        TodoResponseDto result3 = jpaTodoService.createTodo(createDto3);

        List<TodoResponseDto> result = jpaTodoService.findAllDesc();

        assertThat(result.size()).isEqualTo(3);

        assertThat(result.get(0).getId()).isEqualTo(result3.getId());
        assertThat(result.get(1).getId()).isEqualTo(result2.getId());
        assertThat(result.get(2).getId()).isEqualTo(result1.getId());
    }

    @Test
    @DisplayName("완료된 할 일들만 조회")
    void findAllByComplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId);

        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);
        TodoResponseDto result3 = jpaTodoService.createTodo(createDto3);

        jpaTodoService.updateToComplete(result1.getId());
        jpaTodoService.updateToComplete(result2.getId());

        assertThat(jpaTodoService.findAllByComplete().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("미완료인 할 일들만 조회")
    void findAllByUnComplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId);

        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);
        TodoResponseDto result3 = jpaTodoService.createTodo(createDto3);

        jpaTodoService.updateToComplete(result1.getId());

        assertThat(jpaTodoService.findAllByUnComplete().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 키워드로 할 일을 조회할 수 있어야 한다")
    void findByTaskContaining() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);
        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);

        List<TodoResponseDto> todoList = jpaTodoService.findByTaskContaining("clean");
        assertThat(todoList.size()).isEqualTo(1);
    }


    @Test
    @DisplayName("해당 유저의 할 일 목록을 조회할 수 있다")
    void findTodoByUser() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId);
        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);

        List<TodoResponseDto> byUser = jpaTodoService.findByUserId(savedUserId);
        assertThat(byUser.size()).isEqualTo(2);
    }

}