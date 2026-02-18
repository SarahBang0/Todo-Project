package miniProject.todo_list.user.Dto;


import lombok.*;
import miniProject.todo_list.user.Entity.User;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserJoinDto {

    // 회원가입 시 id 필요없음
    private String email;
    private String userName;

    // 회원가입은 저장만 하는거니까 조회할(Entity->Dto) 필요 없음

    // Dto -> Entity 변환 메서드 (DB에 저장)
    public User toEntity() {
        return User.builder()
                .email(this.email)
                .userName(this.userName)
                .build();
    }
}
