package miniProject.todo_list.todo.Service;

import lombok.RequiredArgsConstructor;
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
import java.util.NoSuchElementException;

@Service
@Primary
@RequiredArgsConstructor // final 붙은 필드만 모아서 생성자 자동 생성
public class JpaTodoServiceImpl implements TodoService{

    private final JpaTodoRepository jpaTodoRepository;
    private final JpaUserRepository jpaUserRepository;

/*    @Autowired //생성자 하나면 생량 가능
    public JpaTodoServiceImpl(JpaTodoRepository jpaTodoRepository, JpaUserRepository jpaUserRepository) {
        this.jpaTodoRepository = jpaTodoRepository;
        this.jpaUserRepository = jpaUserRepository;
    }*/


    // 유저 조회 검사 로직
    private User getUserOrThrow(Long userId) {
        System.out.println("userId = " + userId);
        return jpaUserRepository.findById(userId).orElseThrow(()->
                new NoSuchElementException("유저 조회 실패! 해당 Id의 유저가 존재하지 않습니다."));
    }

    // 할 일 조회 검사 로직
    private Todo getTodoOrThrow(Long todoId) {
        System.out.println("todoId = " + todoId);
        return jpaTodoRepository.findById(todoId).orElseThrow(()->
                new NoSuchElementException("할 일 조회 실패! 해당 Id의 할 일이 없습니다."));
    }

    // Todo 엔티티 List -> Todo Dto List 변경 로직
    private List<TodoResponseDto> toTodoResponseDto(List<Todo> todoList) {
        List<TodoResponseDto> todoResponseDtos = new ArrayList<>();
        for(Todo todo : todoList) {
            todoResponseDtos.add(TodoResponseDto.fromEntity(todo));
        }
        if(todoResponseDtos.size()==0) {
            throw new NoSuchElementException("해당하는 할 일 목록이 없습니다.");
        }
        return todoResponseDtos;
    }


    // 할 일 생성
    @Override
    public TodoResponseDto createTodo(TodoCreateDto dto) {
        Long userId = dto.getUserId();
        User findUser = getUserOrThrow(userId);
        Todo todo = dto.toEntity(findUser);
        Todo savedTodo = jpaTodoRepository.save(todo);
        return TodoResponseDto.fromEntity(savedTodo);
    }

    // 할 일 삭제
    @Override
    public Long deleteTodo(Long todoId) {
        Todo findTodo = getTodoOrThrow(todoId);
        jpaTodoRepository.delete(findTodo);
        return todoId;
    }


    // 할 일 조회
    @Override
    public TodoResponseDto findTodoById(Long todoId) {
        Todo findTodo = getTodoOrThrow(todoId);
        return TodoResponseDto.fromEntity(findTodo);
    }

    // 할 일 완료 상태 변경 (미완료 -> 완료)
    @Override
    @Transactional
    public void updateToComplete(Long todoId) {
        Todo todo = getTodoOrThrow(todoId);
        todo.complete();
    }


    // 할 일 완료 상태 변경 (완료 -> 미완료)
    @Override
    @Transactional
    public void updateToUncomplete(Long todoId) {
        Todo todo = getTodoOrThrow(todoId);
        todo.unComplete();
    }


    // 모든 할 일 조회
    @Override
    public List<TodoResponseDto> findAll() {
        List<Todo> todoList = jpaTodoRepository.findAll();
        return toTodoResponseDto(todoList);
    }

    // 모든 할 일 최신순 (내림차순) 조회
    @Override
    public List<TodoResponseDto> findAllDesc() {
        List<Todo> todoList = jpaTodoRepository.findAllByOrderByIdDesc();
        return toTodoResponseDto(todoList);
    }

    // 완료된 모든 할 일 조회
    @Override
    public List<TodoResponseDto> findAllByComplete() {
        List<Todo> todoList = jpaTodoRepository.findByIsCompleteTrue();
        return toTodoResponseDto(todoList);
    }

    // 미완료인 모든 할 일 조회
    @Override
    public List<TodoResponseDto> findAllByUnComplete() {
        List<Todo> todoList = jpaTodoRepository.findByIsCompleteFalse();
        return toTodoResponseDto(todoList);
    }

    // 특정 키워드가 포함된 할 일 조회
    @Override
    public List<TodoResponseDto> findByTaskContaining(String keyword) {
        List<Todo> todoList = jpaTodoRepository.findByTaskContaining(keyword);
        return toTodoResponseDto(todoList);
    }

    // 특정 유저의 할 일들 조회
    @Override
    public List<TodoResponseDto> findByUserId(Long userId) {
        User findedUser = getUserOrThrow(userId);
        List<Todo> todoList = jpaTodoRepository.findByUserId(findedUser.getId());
        return toTodoResponseDto(todoList);
    }

    // 할 일 우선순위 변경
    @Override
    @Transactional
    public TodoResponseDto changeTodoPriority(Long todoId, Priority priority) {
        Todo findTodo = getTodoOrThrow(todoId);
        findTodo.changePriority(priority);
        return TodoResponseDto.fromEntity(findTodo);
    }

    // 우선순위로 할 일 조회하기
    @Override
    public List<TodoResponseDto> findAllByPriority(Priority priority) {
        List<Todo> todoList = jpaTodoRepository.findAllByPriority(priority);
        return toTodoResponseDto(todoList);
    }

    //우선순위 높은 순서대로 조회
    @Override
    public List<TodoResponseDto> findAllByOrderByPriorityAsc() {
        List<Todo> todoList = jpaTodoRepository.findAllByOrderByPriority();
        return toTodoResponseDto(todoList);
    }

    // 할 일 상태 변경 (할 일 제목, 우선순위, 완료상태 변경 가능)
    @Override
    @Transactional
    public TodoResponseDto updateTodo(Long todoId, TodoUpdateDto dto) {
        Todo findTodo = getTodoOrThrow(todoId);

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
