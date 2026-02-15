package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class JpaTodoServiceImpl implements TodoService{

    private final JpaTodoRepository jpaTodoRepository;
    private final JpaUserRepository jpaUserRepository;

    @Autowired
    public JpaTodoServiceImpl(JpaTodoRepository jpaTodoRepository, JpaUserRepository jpaUserRepository) {
        this.jpaTodoRepository = jpaTodoRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void createTodo(Todo todo) {
        Long userId = todo.getUser().getId();
        User findUser = jpaUserRepository.findById(userId).orElseThrow(()->
                new IllegalStateException("해당 유저는 존재하지 않습니다."));
        todo.setUser(findUser);
        jpaTodoRepository.save(todo);
    }

    @Override
    public Todo deleteTodo(Long id) {
        Todo todo = jpaTodoRepository.findById(id).orElseThrow(()-> new IllegalStateException("삭제 실패! 해당 Id의 할 일이 없습니다."));
        jpaTodoRepository.delete(todo);
        return todo;
    }

    @Override
    public Todo findTodoById(Long id) {
        return jpaTodoRepository.findById(id).orElseThrow(()->
                new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
    }

    @Override
    @Transactional
    public void updateToComplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        if(todo.isComplete()) {
            throw new IllegalStateException("수정 실패! 완료된 할 일은 완료로 바꿀 수 없습니다.");
        }
        todo.setComplete(true);
    }

    @Override
    @Transactional
    public void updateToUncomplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        if(todo.isComplete()) {
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
        User findedUser = jpaUserRepository.findById(userId).orElseThrow(()->
                new IllegalStateException("존재하지 않는 유저입니다."));
        return jpaTodoRepository.findByUserId(findedUser.getId());
    }
}
