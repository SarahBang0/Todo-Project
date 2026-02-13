package miniProject.todo_list.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TodoListServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    @Autowired
    public TodoListServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public void createTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    public Todo deleteTodo(Long todoId) {
        return todoRepository.delete(todoId);
    }

    @Override
    public Todo findTodo(Long todoId) {
        return todoRepository.findById(todoId);
    }
}
