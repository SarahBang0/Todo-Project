package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.user.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class JpaTodoServiceImpl implements TodoService{

    private final JpaTodoRepository jpaTodoRepository;

    @Autowired
    public JpaTodoServiceImpl(JpaTodoRepository jpaTodoRepository) {
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    public void createTodo(Todo todo) {
        jpaTodoRepository.save(todo);
    }

    @Override
    public Todo deleteTodo(Long id) {
        Todo todo = jpaTodoRepository.findById(id).orElseThrow(()-> new IllegalStateException("삭제 실패! 해당 Id의 할 일이 없습니다."));
        jpaTodoRepository.delete(todo);
        return todo;
    }

    @Override
    public Todo findTodo(Long id) {
        return jpaTodoRepository.findById(id).orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
    }

    @Override
    public void updateToComplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        if(todo.getIsComplete()) {
            throw new IllegalStateException("수정 실패! 완료된 할 일은 완료로 바꿀 수 없습니다.");
        }
        todo.setComplete(true);
    }

    @Override
    public void updateToUncomplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        if(todo.getIsComplete()) {
            todo.setComplete(false);
        } else {
            throw new IllegalStateException("수정 실패! 미완료인 할 일은 미완료로 바꿀 수 없습니다.");
        }

    }

    @Override
    public List<Todo> findAll() {
        return jpaTodoRepository.findAll();
    }

    @Override
    public List<Todo> findAllDesc() {
        return jpaTodoRepository.findAllByOrderByIdDesc();
    }

    @Override
    public List<Todo> findAllByComplete() {
        return jpaTodoRepository.findByIsCompleteTrue();
    }

    @Override
    public List<Todo> findAllByUnComplete() {
        return jpaTodoRepository.findByIsCompleteFalse();
    }

    @Override
    public List<Todo> findByTaskContaining(String keyword) {
        return jpaTodoRepository.findByTaskContaining(keyword);
    }

    @Override
    public List<Todo> findByUserId(Long userId) {
        return jpaTodoRepository.findByUserId(userId);
    }
}
