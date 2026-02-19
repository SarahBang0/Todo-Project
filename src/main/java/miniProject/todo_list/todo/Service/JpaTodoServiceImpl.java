package miniProject.todo_list.todo.Service;

import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
import miniProject.todo_list.todo.Dto.TodoUpdateDto;
import miniProject.todo_list.todo.Entity.Priority;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public TodoResponseDto createTodo(TodoCreateDto dto) {
        Long userId = dto.getUserId();
        User findUser = jpaUserRepository.findById(userId).orElseThrow(()->
                new IllegalStateException("해당 유저는 존재하지 않습니다."));
        Todo todo = dto.toEntity(findUser);
        Todo savedTodo = jpaTodoRepository.save(todo);
        return TodoResponseDto.fromEntity(savedTodo);
    }

    @Override
    public Long deleteTodo(Long id) {
        Todo findTodo = jpaTodoRepository.findById(id).orElseThrow(()->
                new IllegalStateException("삭제 실패! 해당 Id의 할 일이 없습니다."));
        jpaTodoRepository.delete(findTodo);
        return id;
    }

    @Override
    public TodoResponseDto findTodoById(Long id) {
        Todo findTodo = jpaTodoRepository.findById(id).orElseThrow(()->
                new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        return TodoResponseDto.fromEntity(findTodo);
    }

    @Override
    @Transactional
    public void updateToComplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        todo.complete();
    }

    @Override
    @Transactional
    public void updateToUncomplete(Long id) {
        Todo todo = jpaTodoRepository.findById(id)
                .orElseThrow(()-> new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));
        todo.unComplete();
    }

    @Override
    public List<TodoResponseDto> findAll() {
        List<Todo> todoList = jpaTodoRepository.findAll();
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findAllDesc() {
        List<Todo> todoList = jpaTodoRepository.findAllByOrderByIdDesc();
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findAllByComplete() {
        List<Todo> todoList = jpaTodoRepository.findByIsCompleteTrue();
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findAllByUnComplete() {
        List<Todo> todoList = jpaTodoRepository.findByIsCompleteFalse();
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findByTaskContaining(String keyword) {
        List<Todo> todoList = jpaTodoRepository.findByTaskContaining(keyword);
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findByUserId(Long userId) {
        User findedUser = jpaUserRepository.findById(userId).orElseThrow(()->
                new IllegalStateException("존재하지 않는 유저입니다."));
        List<Todo> todoList = jpaTodoRepository.findByUserId(findedUser.getId());
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return todoResponseDtos;
    }

    @Override
    @Transactional
    public TodoResponseDto changeTodoPriority(Long todoId, Priority priority) {
        Todo findTodo = jpaTodoRepository.findById(todoId).orElseThrow(()->
                new IllegalStateException("존재하지 않는 할 일 입니다."));
        findTodo.changePriority(priority);
        return TodoResponseDto.fromEntity(findTodo);
    }

    @Override
    public List<TodoResponseDto> findAllByPriority(Priority priority) {
        List<Todo> todoList = jpaTodoRepository.findAllByPriority(priority);
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return  todoResponseDtos;
    }

    @Override
    public List<TodoResponseDto> findAllByOrderByPriorityAsc() {
        List<Todo> todoList = jpaTodoRepository.findAllByOrderByPriority();
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        return  todoResponseDtos;
    }

    @Override
    @Transactional
    public TodoResponseDto updateTodo(Long todoId, TodoUpdateDto dto) {
        Todo findTodo = jpaTodoRepository.findById(todoId).orElseThrow(()->
                new IllegalStateException("조회 실패! 해당 Id의 할 일이 없습니다."));

        if(dto.getTask()!=null) {
            findTodo.setTask(dto.getTask());
        }

        if(dto.getIsComplete() != null) {
            if(dto.getIsComplete()) findTodo.complete();
            else findTodo.unComplete();
        }
        if(dto.getPriority() != null) {
            findTodo.setPriority(dto.getPriority());
        }
        return TodoResponseDto.fromEntity(findTodo);
    }
}
