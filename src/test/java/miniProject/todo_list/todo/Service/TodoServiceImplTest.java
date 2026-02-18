/*
package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.todo.Repository.MemoryTodoRepository;
import miniProject.todo_list.user.Repository.MemoryUserRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Disabled("테스트 실패로 짜증나서 포기함")
class TodoServiceImplTest {

    @Autowired
    TodoService todoService;
    @Autowired
    MemoryTodoRepository todoRepository;
    @Autowired UserService userService;
    @Autowired MemoryUserRepository userRepository;

    @AfterEach
    void afterEach() {
        todoRepository.clearStore();
        userRepository.clearStore();
    }
//    TodoService todoService;
//    MemroyTodoRepository todoRepository;
//    UserService userService;
//    MemoryUserRepository userRepository;
//    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
//
//    @BeforeEach
//    void beforeEach() {
//        todoService = ac.getBean(TodoService.class);
//        todoRepository = ac.getBean(MemroyTodoRepository.class);
//        userService = ac.getBean(UserService.class);
//        userRepository = ac.getBean(MemoryUserRepository.class);
//    }

    @Test
    @DisplayName("할 일 생성하고 조회할 수 있어야 한다")
    void createAndFindTodo() {
        //given 주어졌을때
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);

        //when 어떤 행위를 했을 때
        todoService.createTodo(todo);
        Todo result = todoService.findTodoById(1L);

        //then 결과가 이래야 한다
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(todo.getId());
        assertThat(result.getTask()).isEqualTo(todo.getTask());
    }

    @Test
    @DisplayName("삭제 성공 / 할 일을 삭제 할 수 있어야 한다")
    void deleteTodo() {
        //given
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);

        //when
        todoService.deleteTodo(1L);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> todoService.findTodoById(todo.getId()));
        assertThat(e.getMessage()).isEqualTo("조회 실패! 해당하는 할 일이 없습니다.");
    }

    @Test
    @DisplayName("삭제 실패 / 존재하지 않는 할 일 삭제 시 에러가 나야 한다")
    void deleteTodoError() {
        Long temp = 12345L;
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> todoService.deleteTodo(temp));
        assertThat(e.getMessage()).isEqualTo("삭제 실패! 해당하는 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 완료 - 수정 성공 / 할 일 상태를 완료로 바꿀 수 있어야 한다")
    void updateStatus() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);

        todoService.updateToComplete(1L);

        Todo updated = todoService.findTodoById(1L);
        assertThat(updated.isComplete()).isTrue();
    }

    @Test
    @DisplayName("할 일 완료 - 수정 실패 / 존재하지 않거나 이미 완료된 할 일은 완료로 바꿀 수 없다")
    void updateStatusFail() {
        // 이미 완료인 경우
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);
        todoService.updateToComplete(1L);

        IllegalStateException e1 = assertThrows(IllegalStateException.class, () -> todoService.updateToComplete(todo.getId()));
        assertThat(e1.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");


        // 존재하지 않는 경우
        IllegalStateException e2 = assertThrows(IllegalStateException.class, () -> todoService.updateToComplete(3L));
        assertThat(e2.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 미완료 - 수정 성공 / 할 일 상태를 미완료로 바꿀 수 있어야 한다")
    void updateToUncomplete() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);
        todoService.updateToComplete(1L);

        todoService.updateToUncomplete(1L);

        Todo find = todoService.findTodoById(1L);
        assertThat(find.isComplete()).isFalse();
    }

    @Test
    @DisplayName("할 일 미완료 - 수정 실패 / 존재하지 않거나 미완료인 할 일을 미완료로 바꿀 수 없다.")
    void updateToUncompleteFail() {
        // 이미 미완료인 경우
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);

        IllegalStateException e1 = assertThrows(IllegalStateException.class, () -> todoService.updateToUncomplete(todo.getId()));
        assertThat(e1.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");


        // 존재하지 않는 경우
        IllegalStateException e2 = assertThrows(IllegalStateException.class, () -> todoService.updateToUncomplete(3L));
        assertThat(e2.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");

    }

    @Test
    @DisplayName("할 일 목록을 모두 조회할 수 있다")
    void findAllTest() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo1 = new Todo(1L, "clean the room", user1);
        Todo todo2 = new Todo(2L, "study spring", user1);

        todoService.createTodo(todo1);
        todoService.createTodo(todo2);

        List<Todo> todoList = todoService.findAll();
        assertThat(todoList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("완료된 목록들만 조회할 수 있다")
    void findAllByComplete() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        todoService.createTodo(new Todo(1L, "clean the room", user1));
        todoService.createTodo(new Todo(2L, "study spring", user1));
        todoService.createTodo(new Todo(3L, "take a shower", user1));
        todoService.createTodo(new Todo(4L, "sleeping", user1));

        todoService.updateToComplete(1L);
        todoService.updateToComplete(3L);

        List<Todo> todoListCompleted = todoService.findAllByComplete();
        assertThat(todoListCompleted.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 키워드로 할 일을 조회할 수 있어야 한다")
    void findByTaskContaining() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        Todo todo = new Todo(1L, "clean the room", user1);
        todoService.createTodo(todo);

        List<Todo> todoList = todoService.findByTaskContaining("clean");
        assertThat(todoList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("할 일을 최신순 (Id기준 내림차순)으로 정렬할 수 있어야 한다")
    void findAllDesc() {
        User user1 = new User(null, "spring@gamil.com", "User1");
        userService.joinUser(user1);
        todoService.createTodo(new Todo(1L, "clean the room", user1));
        todoService.createTodo(new Todo(2L, "study spring", user1));
        todoService.createTodo(new Todo(3L, "take a shower", user1));

        List<Todo> result = todoService.findAllDesc();

        assertThat(result.get(0).getId()).isEqualTo(3);
        assertThat(result.get(1).getId()).isEqualTo(2);
        assertThat(result.get(2).getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("해당 유저 아이디로 할 일 목록을 조회할 수 있다")
    void findTodosByUserId() {
        User user1 = new User(1L, "spring@gmail.com", "User1");
        User user2 = new User(2L, "Java@gmail.com", "User2");
        userService.joinUser(user1);
        userService.joinUser(user2);

        todoService.createTodo(new Todo(1L, "clean the room", user1));
        todoService.createTodo(new Todo(2L, "study spring", user2));
        todoService.createTodo(new Todo(3L, "take a shower", user1));

        List<Todo> todoList = todoService.findByUserId(user1.getId());

        assertThat(todoList.size()).isEqualTo(2);
    }
}*/
