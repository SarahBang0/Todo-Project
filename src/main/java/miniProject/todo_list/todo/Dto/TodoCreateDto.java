package miniProject.todo_list.todo.Dto;

import lombok.*;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TodoCreateDto {

    // todo 만들 때 id 필요없음
    private String task;
    private Long userId;

    // todo create은 저장만 하는거니까 조회할(Entity->Dto) 필요 없음

    // Dto -> Entity 변환 메서드 (DB에 저장)
    public Todo toEntity(User user) {
        return Todo.builder()
                .task(this.task)
                .user(user)
                .isComplete(false)
                .build();
    }

}
