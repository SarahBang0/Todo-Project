package miniProject.todo_list.todo;

public interface TodoService {

    void createTodo(Todo todo);

    Todo deleteTodo(Long id);

    Todo findTodo(Long todoId);

}
