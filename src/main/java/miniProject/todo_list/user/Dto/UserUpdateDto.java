package miniProject.todo_list.user.Dto;

import lombok.*;
import miniProject.todo_list.user.Entity.User;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserUpdateDto {

    private String email;
    private String userName;

    public User toEntity() {
        return User.builder()
                .email(this.email)
                .userName(this.userName)
                .build();
    }
}
