package miniProject.todo_list.todo.Repository;

import miniProject.todo_list.todo.Entity.Todo;

import java.util.List;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo delete(Long todoId);

    Todo findById(Long todoId);

    List<Todo> findAll();

    List<Todo> findAllByComplete();

    List<Todo> findAllByUnComplete();

    List<Todo> findByTaskContaining(String keyword);

    List<Todo> findTodoByUserId(Long userId);
}
