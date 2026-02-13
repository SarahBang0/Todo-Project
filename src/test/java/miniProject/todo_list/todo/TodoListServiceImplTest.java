package miniProject.todo_list.todo;

import miniProject.todo_list.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TodoListServiceImplTest {

    TodoService todoService;
    TodoListRepository todoRepository;
    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @BeforeEach
    void beforeEach() {
        todoService = ac.getBean(TodoService.class);
    }

    @AfterEach
    void afterEach() {
        todoRepository = ac.getBean(TodoListRepository.class);
        todoRepository.clearStore();
    }

    @Test
    @DisplayName("할 일 생성하고 조회할 수 있어야 한다")
    void createAndFindTodo() {
        //given 주어졌을때
        Todo todo = new Todo(1L, "clean the room");

        //when 어떤 행위를 했을 때
        todoService.createTodo(todo);
        Todo result = todoService.findTodo(1L);

        //then 결과가 이래야 한다
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getId()).isEqualTo(todo.getId());
        Assertions.assertThat(result.getTask()).isEqualTo(todo.getTask());
    }

    @Test
    @DisplayName("삭제 성공 / 할 일을 삭제 할 수 있어야 한다")
    void deleteTodo() {
        //given
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);

        //when
        todoService.deleteTodo(1L);

        //then
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> todoService.findTodo(1L));
    }

    @Test
    @DisplayName("삭제 실패 / 존재하지 않는 할 일 삭제 시 에러가 나야 한다")
    void deleteTodoError() {
        Long temp = 12345L;
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> todoService.deleteTodo(temp));
        Assertions.assertThat(e.getMessage()).isEqualTo("삭제 실패! 해당하는 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 완료 - 수정 성공 / 할 일 상태를 완료로 바꿀 수 있어야 한다")
    void updateStatus() {
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);

        todoService.updateToComplete(1L);

        Todo updated = todoService.findTodo(1L);
        Assertions.assertThat(updated.getIsComplete()).isTrue();
    }

    @Test
    @DisplayName("할 일 완료 - 수정 실패 / 존재하지 않거나 이미 완료된 할 일은 완료로 바꿀 수 없다")
    void updateStatusFail() {
        // 이미 완료인 경우
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);
        todoService.updateToComplete(1L);

        IllegalStateException e1 = assertThrows(IllegalStateException.class, () -> todoService.updateToComplete(todo.getId()));
        Assertions.assertThat(e1.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");


        // 존재하지 않는 경우
        IllegalStateException e2 = assertThrows(IllegalStateException.class, () -> todoService.updateToComplete(3L));
        Assertions.assertThat(e2.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");
    }

    @Test
    @DisplayName("할 일 미완료 - 수정 성공 / 할 일 상태를 미완료로 바꿀 수 있어야 한다")
    void updateToUncomplete() {
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);
        todoService.updateToComplete(1L);

        todoService.updateToUncomplete(1L);

        Todo find = todoService.findTodo(1L);
        Assertions.assertThat(find.getIsComplete()).isFalse();
    }

    @Test
    @DisplayName("할 일 미완료 - 수정 실패 / 존재하지 않거나 미완료인 할 일을 미완료로 바꿀 수 없다.")
    void updateToUncompleteFail() {
        // 이미 미완료인 경우
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);

        IllegalStateException e1 = assertThrows(IllegalStateException.class, () -> todoService.updateToUncomplete(todo.getId()));
        Assertions.assertThat(e1.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");


        // 존재하지 않는 경우
        IllegalStateException e2 = assertThrows(IllegalStateException.class, () -> todoService.updateToUncomplete(3L));
        Assertions.assertThat(e2.getMessage()).isEqualTo("상태 업데이트 실패! 해당하는 할 일이 없습니다.");

    }

    @Test
    @DisplayName("할 일 목록을 모두 조회할 수 있다")
    void findAllTest() {
        Todo todo1 = new Todo(1L, "clean the room");
        Todo todo2 = new Todo(2L, "study spring");

        todoService.createTodo(todo1);
        todoService.createTodo(todo2);

        List<Todo> todoList = todoService.findAll();
        Assertions.assertThat(todoList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("완료된 목록들만 조회할 수 있다")
    void findAllByComplete() {

        todoService.createTodo(new Todo(1L, "clean the room"));
        todoService.createTodo(new Todo(2L, "study spring"));
        todoService.createTodo(new Todo(3L, "take a shower"));
        todoService.createTodo(new Todo(4L, "sleeping"));

        todoService.updateToComplete(1L);
        todoService.updateToComplete(3L);

        List<Todo> todoListCompleted = todoService.findAllByComplete();
        Assertions.assertThat(todoListCompleted.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 키워드로 할 일을 조회할 수 있어야 한다")
    void findByTaskContaining() {
        Todo todo = new Todo(1L, "clean the room");
        todoService.createTodo(todo);

        List<Todo> todoList = todoService.findByTaskContaining("clean");
        Assertions.assertThat(todoList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("할 일을 최신순 (Id기준 내림차순)으로 정렬할 수 있어야 한다")
    void findAllDesc() {
        todoService.createTodo(new Todo(1L, "clean the room"));
        todoService.createTodo(new Todo(2L, "study spring"));
        todoService.createTodo(new Todo(3L, "take a shower"));

        List<Todo> result = todoService.findAllDesc();

        Assertions.assertThat(result.get(0).getId()).isEqualTo(3);
        Assertions.assertThat(result.get(1).getId()).isEqualTo(2);
        Assertions.assertThat(result.get(2).getId()).isEqualTo(1);



    }
}