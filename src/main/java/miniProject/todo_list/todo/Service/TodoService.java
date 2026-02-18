package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Entity.User;

import java.util.List;

public interface TodoService {

    TodoResponseDto createTodo(TodoCreateDto dto);

    Long deleteTodo(Long id);

    TodoResponseDto findTodoById(Long todoId);

    void updateToComplete(Long id);

    void updateToUncomplete(Long id);

    List<TodoResponseDto> findAll();

    List<TodoResponseDto> findAllDesc();

    List<TodoResponseDto> findAllByComplete();

    List<TodoResponseDto> findAllByUnComplete();

    List<TodoResponseDto> findByTaskContaining(String keyword);

    List<TodoResponseDto> findByUserId(Long userId);
}
