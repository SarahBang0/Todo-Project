package miniProject.todo_list.user.Dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "이메일은 필수 입력 값 입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "이름은 필수 입력 값 입니다.")
    @Size(min = 2, max = 10, message = "이름은 2자 이상 10자 이하로 입력해주세요.")
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
