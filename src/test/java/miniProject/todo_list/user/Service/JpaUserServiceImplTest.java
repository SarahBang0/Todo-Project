package miniProject.todo_list.user.Service;

import miniProject.todo_list.todo.Service.JpaTodoServiceImpl;
import miniProject.todo_list.user.Dto.UserJoinDto;
import miniProject.todo_list.user.Dto.UserResponseDto;
import miniProject.todo_list.user.Dto.UserUpdateDto;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class JpaUserServiceImplTest {

    @Autowired
    JpaTodoServiceImpl jpaTodoService;
    @Autowired
    JpaUserServiceImpl jpaUserService;
/*
    @Autowired
    JpaUserRepository jpaUserRepository;

    @BeforeEach
    void beforeEach() {
        jpaUserRepository.deleteAll();
    }
*/


    @Test
    @DisplayName("유저를 생성하고 조회할 수 있어야 한다")
    void joinUser() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();


        // dto 저장 확인
        assertThat(user1.getUserName()).isEqualTo("User1");
        // entity 저장 확인
        assertThat(savedUserId).isEqualTo(savedUser.getId());
        assertThat(savedUser.getId()).isEqualTo(savedUserId);
        assertThat(savedUser.getUserName()).isEqualTo("User1");
    }

    @Test
    @DisplayName("유저 생성 실패 / 이미 존재하는 이메일로 회원을 생성할 수 없다")
    void joinUserFail() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaUserService.joinUser(new UserJoinDto("spring@gmail.com", "User2")));
        assertThat(e.getMessage()).isEqualTo("유저 생성 실패! 이미 존재하는 이메일입니다.");
    }

    @Test
    @DisplayName("유저 탈퇴 성공 케이스")
    void quitUser() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        jpaUserService.quitUser(savedUserId, savedUserId);

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaUserService.findUserById(savedUserId));
        assertThat(e.getMessage()).isEqualTo("조회 실패! 해당 Id의 유저가 없습니다.");
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 요청아이디와 타켓아이디 불일치")
    void quitUserFail() {
        UserJoinDto user1 = new UserJoinDto("spring@gmail.com", "User1");
        UserResponseDto savedUser = jpaUserService.joinUser(user1);
        Long savedUserId = savedUser.getId();

        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaUserService.quitUser(1234L, savedUserId));
        assertThat(e.getMessage()).isEqualTo("유저 삭제 실패! 요청된 Id와 삭제할 Id가 같지 않습니다.");
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 존재하지 않는 유저 삭제")
    void quitUserFail2() {
        Long temp = 123456L;
        IllegalStateException e = assertThrows(IllegalStateException.class,
                () -> jpaUserService.quitUser(temp, temp));
        assertThat(e.getMessage()).isEqualTo("유저 삭제 실패! 해당 Id의 유저가 없습니다.");
    }

    @Test
    @DisplayName("모든 유저 목록을 조회할 수 있다")
    void findUserAll() {
        jpaUserService.joinUser(new UserJoinDto("spring@gmail.com", "User1"));
        jpaUserService.joinUser(new UserJoinDto("java@gmail.com", "User2"));

        List<UserResponseDto> userResponseDtos = jpaUserService.findAll();
        assertThat(userResponseDtos.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("유저 이메일 / 이름을 변경할 수 있다")
    void updateUser() {
        UserResponseDto dto = jpaUserService.joinUser(new UserJoinDto("spring@gmail.com", "User1"));
        UserResponseDto updated = jpaUserService.updateUser(dto.getId(), new UserUpdateDto("java@gmail.com", "Hello"));

        assertThat(updated.getUserName()).isEqualTo("Hello");
    }
}