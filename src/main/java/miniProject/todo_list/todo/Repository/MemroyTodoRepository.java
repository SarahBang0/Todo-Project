package miniProject.todo_list.todo.Repository;

import miniProject.todo_list.todo.Entity.Todo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MemroyTodoRepository implements TodoRepository {

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

    @Override
    public List<Todo> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Todo> findAllByComplete() {
        List<Todo> todoList = new ArrayList<>();
        for(Todo todo : store.values()) {
            if(todo.getIsComplete()) {
                todoList.add(todo);
            }
        }
        return todoList;
    }

    @Override
    public List<Todo> findAllByUnComplete() {
        List<Todo> todoList = new ArrayList<>();
        for(Todo todo : store.values()) {
            if(!todo.getIsComplete()) {
                todoList.add(todo);
            }
        }
        return todoList;
    }

    @Override
    public List<Todo> findByTaskContaining(String keyword) {
        List<Todo> todoList = new ArrayList<>();
        for(Todo todo : store.values()) {
            if(todo.getTask().contains(keyword)) {
                todoList.add(todo);
            }
        }
        return todoList;
    }

    @Override
    public List<Todo> findTodoByUserId(Long userId) {
        List<Todo> todoList = new ArrayList<>();
        for(Todo todo : store.values()) {
            if(todo.getUserId().equals(userId)) {
                todoList.add(todo);
            }
        }
        return todoList;
    }

    public void clearStore() {
        store.clear();
    }

}
