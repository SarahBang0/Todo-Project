package miniProject.todo_list.todo;


import lombok.RequiredArgsConstructor;
import miniProject.todo_list.todo.Dto.TodoCreateDto;
import miniProject.todo_list.todo.Dto.TodoResponseDto;
import miniProject.todo_list.todo.Dto.TodoUpdateDto;
import miniProject.todo_list.todo.Entity.Priority;
import miniProject.todo_list.todo.Service.TodoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 데이터를 JSON으로 반환하는 컨트롤러
@RequiredArgsConstructor
public class TodoController {

    private final TodoService jpaTodoService;


    // 모든 할 일 목록 조회
    @GetMapping("/api/todos")
    public List<TodoResponseDto> findAllTodos() {
        return jpaTodoService.findAll();
    }

    // 모든 할 일 목록 최신순 조회
    @GetMapping("/api/todos/desc")
    public List<TodoResponseDto> findAllTodosDesc() {
        return jpaTodoService.findAllDesc();
    }

    // 특정 할 일 조회
    @GetMapping("/api/todos/{id}")
    public TodoResponseDto findTodo(@PathVariable Long id) {
        return jpaTodoService.findTodoById(id);
    }

    // 완료된 할 일 목록 조회
    @GetMapping("/api/todos/complete")
    public List<TodoResponseDto> findTodoComplete() {
        return jpaTodoService.findAllByComplete();
    }

    // 미완료인 할 일 목록 조회
    @GetMapping("/api/todos/uncomplete")
    public List<TodoResponseDto> findTodoUnComplete() {
        return jpaTodoService.findAllByUnComplete();
    }

    // 특정 키워드로 할 일 찾기 (/api/todos/search/java)
    @GetMapping("/api/todos/search")
    public List<TodoResponseDto> findTodoByKeyword(@RequestParam("keyword") String keyword) {
        return jpaTodoService.findByTaskContaining(keyword);
    }

    // 새로운 할 일 생성
    @PostMapping("/api/todos")
    public TodoResponseDto createTodo(@RequestBody TodoCreateDto dto) {
        return jpaTodoService.createTodo(dto);
    }

    // 할 일 삭제
    @DeleteMapping("/api/todos/{id}")
    public void deleteTodo(@PathVariable Long id) {
        jpaTodoService.deleteTodo(id);
    }


    // 특정 할 일 상태 변경 (미완료 -> 완료)
    @PatchMapping("/api/todos/{id}/complete")
    public TodoResponseDto todoComplete(@PathVariable Long id) {
        jpaTodoService.updateToComplete(id);
        return jpaTodoService.findTodoById(id);
    }

    // 특정 할 일 상태 변경 (완료 -> 미완료)
    @PatchMapping("/api/todos/{id}/uncomplete")
    public TodoResponseDto todoUnComplete(@PathVariable Long id) {
        jpaTodoService.updateToUncomplete(id);
        return jpaTodoService.findTodoById(id);
    }

    // 특정 유저로 할 일 목록 가져오기
    @GetMapping("/api/todos/user/{id}")
    public List<TodoResponseDto> findTodoByUser(@PathVariable Long id) {
        return jpaTodoService.findByUserId(id);
    }

    // 우선순위 별로 조회
    @GetMapping("/api/todos/priority/{priority}")
    public List<TodoResponseDto> findTodoByPriority(@PathVariable Priority priority) {
        return jpaTodoService.findAllByPriority(priority);
    }

    // 우선순위 변경
    @PatchMapping("/api/todos/{id}/priority/{priority}")
    public TodoResponseDto updateTodoPriority(@PathVariable Long id, @PathVariable Priority priority) {
        jpaTodoService.changeTodoPriority(id, priority);
        return jpaTodoService.findTodoById(id);
    }

    // 우선순위 순서대로 조회
    @GetMapping("/api/todos/sorted-by-priority")
    public List<TodoResponseDto> findTodoByOrderByPriorityAsc() {
        return jpaTodoService.findAllByOrderByPriorityAsc();
    }

    @PatchMapping("/api/todos/{id}")
    public TodoResponseDto updateTodo(@PathVariable Long id, @RequestBody TodoUpdateDto dto) {
        TodoResponseDto updated = jpaTodoService.updateTodo(id, dto);
        return updated;
    }
}
