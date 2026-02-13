package miniProject.todo_list.todo;

public interface TodoRepository {

    Todo save(Todo todo);

    Todo delete(Long todoId);

    Todo findById(Long todoId);

}
