package miniProject.todo_list.user.Dto;

import lombok.*;
import miniProject.todo_list.user.Entity.User;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    // 개인정보는 숨기기 위해 email은 제외
    private Long id;
    private String userName;


    // Entity -> Dto 변환 메서드
    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .build();
    }
}
