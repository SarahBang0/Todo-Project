package miniProject.todo_list.todo.Dto;

import lombok.*;
import miniProject.todo_list.todo.Entity.Priority;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Entity.User;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TodoUpdateDto {

    private String task;
    private Boolean isComplete; // null 체크를 위해 Boolean
    private Priority priority;

    public Todo toEntity(User user) {
        return Todo.builder()
                .task(this.task)
                .user(user)
                .isComplete(this.isComplete)
                .priority(this.priority)
                .build();
    }

}
