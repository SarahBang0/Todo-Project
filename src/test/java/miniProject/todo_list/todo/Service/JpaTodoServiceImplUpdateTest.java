package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
import miniProject.todo_list.todo.Dto.TodoUpdateDto;
import miniProject.todo_list.todo.Entity.Priority;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Service.JpaUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class JpaTodoServiceImplUpdateTest {

    @Autowired
    JpaTodoServiceImpl jpaTodoService;
    @Autowired
    JpaUserServiceImpl jpaUserService;

    @Test
    @DisplayName("상태 수정 성공 / 할 일 상태를 미완료에서 완료로 바꿀 수 있다")
    void updateToComplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId,null);
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

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId,null);
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
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> jpaTodoService.updateToComplete(temp));
        assertThat(e.getMessage()).isEqualTo("할 일 조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("상태 수정 성공 / 할 일 상태를 완료에서 미완료로 바꿀 수 있다")
    void updateToUncomplete() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId,null);
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

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId,null);
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
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> jpaTodoService.updateToUncomplete(temp));
        assertThat(e.getMessage()).isEqualTo("할 일 조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 이름 / 우선순위 / 완료 상태 변경")
    void upateTodo() {
        UserResponseDto user = jpaUserService.joinUser(new UserJoinDto("spring@gmail.com", "Sarah"));
        TodoResponseDto todo = jpaTodoService.createTodo(new TodoCreateDto("clean the room", user.getId(), null));

        TodoResponseDto updated = jpaTodoService.updateTodo(todo.getId(), new TodoUpdateDto("drink water", true, Priority.HIGH));

        assertThat(updated.getTask()).isEqualTo("drink water");
        assertThat(updated.isComplete()).isTrue();
        assertThat(updated.getPriority()).isEqualTo(Priority.HIGH);
    }


}
