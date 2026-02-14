package miniProject.todo_list.todo;

import java.util.List;

public interface TodoService {

    void createTodo(Todo todo);

    Todo deleteTodo(Long id);

    Todo findTodo(Long todoId);

    void updateToComplete(Long todoId);

    void updateToUncomplete(Long todoId);

    List<Todo> findAll();

    List<Todo> findAllDesc();

    List<Todo> findAllByComplete();

    List<Todo> findByTaskContaining(String keyword);

    List<Todo> findByUserId(Long userId);
}
