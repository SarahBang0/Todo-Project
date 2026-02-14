package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Service.JpaUserServiceImpl;
import org.junit.jupiter.api.Assertions;
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
class JpaTodoServiceImplTest {

    @Autowired
    JpaTodoServiceImpl jpaTodoService;
    @Autowired
    JpaUserServiceImpl jpaUserService;

    @Test
    @DisplayName("할 일을 생성하고 조회할 수 있어야 한다.")
    void createTodo() {
        Todo todo = new Todo(null, "clean the room", 1L);

        jpaTodoService.createTodo(todo);
        Todo findTodo = jpaTodoService.findTodo(todo.getId());

        assertThat(findTodo.getId()).isEqualTo(todo.getId());
    }

    @Test
    @DisplayName("삭제 성공 / 할 일을 삭제할 수 있어야 한다.")
    void deleteTodo() {
        Todo todo = new Todo(null, "clean the room", 1L);
        jpaTodoService.createTodo(todo);

        jpaTodoService.deleteTodo(todo.getId());

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.findTodo(todo.getId()));
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
        Todo todo = new Todo(null, "clean the room", 1L);
        jpaTodoService.createTodo(todo);

        jpaTodoService.updateToComplete(todo.getId());

        assertThat(todo.getIsComplete()).isTrue();
    }

    @Test
    @DisplayName("상태 수정 실패 / 이미 완료인 할 일은 완료로 바꿀 수 없다")
    void updateToCompleteFail() {
        Todo todo = new Todo(null, "clean the room", 1L);
        jpaTodoService.createTodo(todo);
        jpaTodoService.updateToComplete(todo.getId());

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToComplete(todo.getId()));
        assertThat(e.getMessage()).isEqualTo("수정 실패! 완료된 할 일은 완료로 바꿀 수 없습니다.");
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
        Todo todo = new Todo(null, "clean the room", 1L);
        jpaTodoService.createTodo(todo);
        jpaTodoService.updateToComplete(todo.getId());

        jpaTodoService.updateToUncomplete(todo.getId());

        assertThat(todo.getIsComplete()).isFalse();
    }

    @Test
    @DisplayName("상태 수정 실패 / 미완료인 할 일은 미완료로 바꿀 수 없다")
    void updateToUncmpleteFail() {
        Todo todo = new Todo(null, "clean the room", 1L);
        jpaTodoService.createTodo(todo);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaTodoService.updateToUncomplete(todo.getId()));
        assertThat(e.getMessage()).isEqualTo("수정 실패! 미완료인 할 일은 미완료로 바꿀 수 없습니다.");
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
        jpaTodoService.createTodo(new Todo(null, "clean the room", 1L));
        jpaTodoService.createTodo(new Todo(null, "take a shower", 1L));

        assertThat(jpaTodoService.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("할 일 목록 전체 최신순 (내림차순) 조회")
    void findAllDesc() {
        Todo todo1 = new Todo(null, "clean the room", 1L);
        Todo todo2 = new Todo(null, "take a shower", 1L);
        Todo todo3 = new Todo(null, "study spring", 1L);
        jpaTodoService.createTodo(todo1);
        jpaTodoService.createTodo(todo2);
        jpaTodoService.createTodo(todo3);

        List<Todo> result = jpaTodoService.findAllDesc();

        assertThat(result.size()).isEqualTo(3);

        assertThat(result.get(0).getId()).isEqualTo(todo3.getId());
        assertThat(result.get(1).getId()).isEqualTo(todo2.getId());
        assertThat(result.get(2).getId()).isEqualTo(todo1.getId());
    }

    @Test
    @DisplayName("완료된 할 일들만 조회")
    void findAllByComplete() {
        Todo todo1 = new Todo(null, "clean the room", 1L);
        Todo todo2 = new Todo(null, "take a shower", 1L);
        Todo todo3 = new Todo(null, "study spring", 1L);
        jpaTodoService.createTodo(todo1);
        jpaTodoService.createTodo(todo2);
        jpaTodoService.createTodo(todo3);

        jpaTodoService.updateToComplete(todo1.getId());
        jpaTodoService.updateToComplete(todo3.getId());

        assertThat(jpaTodoService.findAllByComplete().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("미완료인 할 일들만 조회")
    void findAllByUnComplete() {
        Todo todo1 = new Todo(null, "clean the room", 1L);
        Todo todo2 = new Todo(null, "take a shower", 1L);
        Todo todo3 = new Todo(null, "study spring", 1L);
        jpaTodoService.createTodo(todo1);
        jpaTodoService.createTodo(todo2);
        jpaTodoService.createTodo(todo3);

        jpaTodoService.updateToComplete(todo1.getId());

        assertThat(jpaTodoService.findAllByUnComplete().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("특정 키워드로 할 일을 조회할 수 있어야 한다")
    void findByTaskContaining() {
        jpaTodoService.createTodo(new Todo(null, "clean the room", 1L));

        List<Todo> todoList = jpaTodoService.findByTaskContaining("clean");
        assertThat(todoList.size()).isEqualTo(1);
    }


}