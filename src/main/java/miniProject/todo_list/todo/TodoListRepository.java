package miniProject.todo_list.todo;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TodoListRepository implements TodoRepository {

    private static Map<Long, Todo> store = new HashMap<>();

    @Override
    public Todo save(Todo todo) {
        store.put(todo.getId(), todo);
        return todo;
    }

    @Override
    public Todo findById(Long todoId) {
        return store.get(todoId);
    }

    @Override
    public Todo delete(Long todoId) {
        return store.remove(todoId);
    }
}
