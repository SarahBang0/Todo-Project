package miniProject.todo_list.todo.Dto;

import lombok.*;
import miniProject.todo_list.todo.Entity.Todo;
import miniProject.todo_list.user.Entity.User;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TodoResponseDto {

    private Long id;
    private String task;
    private boolean isComplete;
    private String userName; // 조회 시에는 작성자 이름만 가져오기

    // Entity -> Dto 변환 메서드
    public static TodoResponseDto fromEntity(Todo todo) {
        return TodoResponseDto.builder()
                .id(todo.getId())
                .task(todo.getTask())
                .isComplete(todo.isComplete())
                .userName(todo.getUser().getUserName())
                .build();
    }

}
