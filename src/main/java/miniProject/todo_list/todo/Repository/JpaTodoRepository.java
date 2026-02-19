package miniProject.todo_list.todo.Repository;

import miniProject.todo_list.todo.Entity.Priority;
import miniProject.todo_list.todo.Entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaTodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserId(Long userId);

    List<Todo> findByTaskContaining(String keyword);

    List<Todo> findByIsCompleteTrue();

    List<Todo> findByIsCompleteFalse();

    List<Todo> findAllByOrderByIdDesc();

    List<Todo> findAllByPriority(Priority priority);

    List<Todo> findAllByOrderByPriority();
}
