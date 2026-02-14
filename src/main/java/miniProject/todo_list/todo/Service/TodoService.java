package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;

import java.util.List;

public interface TodoService {

    void createTodo(Todo todo);

    Todo deleteTodo(Long id);

    Todo findTodo(Long todoId);

    void updateToComplete(Long id);

    void updateToUncomplete(Long id);

    List<Todo> findAll();

    List<Todo> findAllDesc();

    List<Todo> findAllByComplete();

    List<Todo> findAllByUnComplete();

    List<Todo> findByTaskContaining(String keyword);

    List<Todo> findByUserId(Long userId);
}
