package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
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

import java.util.List;
import java.util.NoSuchElementException;

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
    @DisplayName("할 일을 생성하고 조회할 수 있어야 한다. / 우선순위 지정 X")
    void createTodo() {
        // given (유저 생성)
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();
        // 생성용 DTO 준비
        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId, null);

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
    @DisplayName("할 일 생성 및 조회 / 우선순위 지정 O")
    void createTodo2() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long userId = savedUser.getId();
        TodoResponseDto responseDto = jpaTodoService.createTodo(new TodoCreateDto("clean the room", userId, Priority.HIGH));

        TodoResponseDto todoById = jpaTodoService.findTodoById(responseDto.getId());

        assertThat(todoById.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("삭제 성공 / 할 일을 삭제할 수 있어야 한다.")
    void deleteTodo() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUserId, null);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);
        Long todoId = result.getId();

        jpaTodoService.deleteTodo(todoId);


        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> jpaTodoService.findTodoById(todoId));
        assertThat(e.getMessage()).isEqualTo("할 일 조회 실패! 해당 Id의 할 일이 없습니다.");
    }

    @Test
    @DisplayName("삭제 실패 / 존재하지 않는 할 일은 삭제할 수 없다")
    void deleteTodoFail() {
        Long temp = 12345L;
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> jpaTodoService.deleteTodo(temp));
        assertThat(e.getMessage()).isEqualTo("할 일 조회 실패! 해당 Id의 할 일이 없습니다.");
    }


    @Test
    @DisplayName("할 일 목록 전체 조회")
    void findAll() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId, null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId, null);

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
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId, null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId, null);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId, null);

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
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId,null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId, null);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId, null);

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
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId, null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId, null);
        TodoCreateDto createDto3 = new TodoCreateDto("eat something", savedUserId,null);

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
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId,null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId,null);
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
        TodoCreateDto createDto1 = new TodoCreateDto("clean the room", savedUserId, null);
        TodoCreateDto createDto2 = new TodoCreateDto("take a shower", savedUserId,null);
        TodoResponseDto result1 = jpaTodoService.createTodo(createDto1);
        TodoResponseDto result2 = jpaTodoService.createTodo(createDto2);

        List<TodoResponseDto> byUser = jpaTodoService.findByUserId(savedUserId);
        assertThat(byUser.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("할 일의 우선순위를 수정할 수 있다")
    void updateTodoPriority() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        TodoCreateDto createDto = new TodoCreateDto("clean the room", savedUser.getId(), null);
        TodoResponseDto result = jpaTodoService.createTodo(createDto);

        jpaTodoService.changeTodoPriority(result.getId(), Priority.HIGH);
        TodoResponseDto todoById = jpaTodoService.findTodoById(result.getId());

        assertThat(todoById.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("할 일의 우선순위로 검색 할 수 있다")
    void findAllByPriority() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        TodoResponseDto todoDto1 = jpaTodoService.createTodo(new TodoCreateDto("clean the room", savedUser.getId(),null));
        TodoResponseDto todoDto2 = jpaTodoService.createTodo(new TodoCreateDto("drinking", savedUser.getId(),null));

        jpaTodoService.changeTodoPriority(todoDto1.getId(), Priority.HIGH);

        List<TodoResponseDto> todosMidium = jpaTodoService.findAllByPriority(Priority.MEDIUM);
        List<TodoResponseDto> todosHigh = jpaTodoService.findAllByPriority(Priority.HIGH);

        assertThat(todosMidium.size()).isEqualTo(1);
        assertThat(todosHigh.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("우선 순위 높은 순서대로 할 일을 조회할 수 있다")
    void findAllByOrderByPriorityAsc() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        TodoResponseDto todoDto1 = jpaTodoService.createTodo(new TodoCreateDto("clean the room", savedUser.getId(),null));
        TodoResponseDto todoDto2 = jpaTodoService.createTodo(new TodoCreateDto("drinking", savedUser.getId(),null));
        TodoResponseDto todoDto3 = jpaTodoService.createTodo(new TodoCreateDto("study java", savedUser.getId(),null));

        jpaTodoService.changeTodoPriority(todoDto2.getId(), Priority.HIGH);
        TodoResponseDto resultTodo2 = jpaTodoService.findTodoById(todoDto2.getId());
        jpaTodoService.changeTodoPriority(todoDto1.getId(), Priority.LOW);
        TodoResponseDto resultTodo1 = jpaTodoService.findTodoById(todoDto1.getId());

        List<TodoResponseDto> allByOrderByPriorityAsc = jpaTodoService.findAllByOrderByPriorityAsc();
        System.out.println(allByOrderByPriorityAsc.get(0).getPriority());
        System.out.println(allByOrderByPriorityAsc.get(1).getPriority());
        System.out.println(allByOrderByPriorityAsc.get(2).getPriority());

        assertThat(allByOrderByPriorityAsc.get(0).getPriority()).isEqualTo(resultTodo2.getPriority());
        assertThat(allByOrderByPriorityAsc.get(1).getId()).isEqualTo(todoDto3.getId());
        assertThat(allByOrderByPriorityAsc.get(2).getId()).isEqualTo(resultTodo1.getId());


    }
}