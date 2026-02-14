package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.todo.Repository.TodoRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Autowired
    public TodoServiceImpl(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createTodo(Todo todo) {
        todoRepository.save(todo);
    }

    @Override
    public Todo deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id);
        if(todo != null) {
            return todoRepository.delete(todo.getId());
        } else {
            throw new IllegalStateException("삭제 실패! 해당하는 할 일이 없습니다.");
        }
    }

    @Override
    public Todo findTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId);
        if(todo != null) {
            return todo;
        } else {
            throw new IllegalStateException("조회 실패! 해당하는 할 일이 없습니다.");
        }
    }

    @Override
    public void updateToComplete(Long id) {
        Todo todo = todoRepository.findById(id);
        if(todo != null && !todo.getIsComplete()) {
            todo.setComplete(true);
        } else {
            throw new IllegalStateException("상태 업데이트 실패! 해당하는 할 일이 없습니다.");
        }
    }

    @Override
    public void updateToUncomplete(Long id) {
        Todo todo = todoRepository.findById(id);
        if(todo != null && todo.getIsComplete()) {
            todo.setComplete(false);
        } else {
            throw new IllegalStateException("상태 업데이트 실패! 해당하는 할 일이 없습니다.");
        }
    }

    @Override
    public List<Todo> findAll() {
        if(todoRepository.findAll().size()!=0) {
            return todoRepository.findAll();
        } else {
            throw new IllegalStateException("조회 실패! 할 일 목록이 없습니다.");
        }
    }

    @Override
    public List<Todo> findAllDesc() {
        List<Todo> todoList = new ArrayList<>(todoRepository.findAll());
        todoList.sort(((o1, o2) -> o2.getId().compareTo(o1.getId())));
        return todoList;
    }

    @Override
    public List<Todo> findAllByComplete() {
        List<Todo> todoList = new ArrayList<>(todoRepository.findAllByComplete());
        if(todoList.size() != 0) {
            return todoList;
        } else {
            throw new IllegalStateException("조회 실패! 완료된 할 일이 없습니다.");
        }
    }

    @Override
    public List<Todo> findAllByUnComplete() {
        List<Todo> todoList = new ArrayList<>(todoRepository.findAllByUnComplete());
        if(todoList.size() != 0) {
            return todoList;
        } else {
            throw new IllegalStateException("조회 실패! 완료된 할 일이 없습니다.");
        }
    }


    @Override
    public List<Todo> findByTaskContaining(String keyword) {
        List<Todo> todoList = new ArrayList<>(todoRepository.findByTaskContaining(keyword));
        if(todoList.size() != 0) {
            return todoList;
        } else {
            throw new IllegalStateException("조회 실패! 키워드와 일치하는 할 일이 없습니다.");
        }
    }

    @Override
    public List<Todo> findByUserId(Long userId) {
        User user = userRepository.findById(userId);

        if(user == null) {
            throw new IllegalStateException("조회 실패! 존재하지 않는 유저입니다.");
        }

        return todoRepository.findTodoByUserId(user.getId());
    }


}
