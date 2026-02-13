package miniProject.todo_list.todo;

import miniProject.todo_list.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class TodoListServiceImplTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    void createTodo() {

        TodoService bean = ac.getBean(TodoService.class);

        Todo todo1 = new Todo(1L, "hello todo");
        Todo todo2 = new Todo(2L, "this is spring");

        bean.createTodo(todo1);
        bean.createTodo(todo2);

        Todo findTodo1 = bean.findTodo(1L);
        Todo findTodo2 = bean.findTodo(2L);

        Assertions.assertThat(todo1).isEqualTo(findTodo1);
        Assertions.assertThat(todo2).isEqualTo(findTodo2);

    }

}