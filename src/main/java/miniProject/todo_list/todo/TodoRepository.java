package miniProject.todo_list.todo;

import java.util.List;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo delete(Long todoId);

    Todo findById(Long todoId);

    List<Todo> findAll();

    List<Todo> findAllByComplete();

    List<Todo> findByTaskContaining(String keyword);

    List<Todo> findTodoByUserId(Long userId);
}
