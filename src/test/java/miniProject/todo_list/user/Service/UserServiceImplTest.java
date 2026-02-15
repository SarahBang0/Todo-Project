package miniProject.todo_list.user.Service;

import miniProject.todo_list.todo.Repository.MemoryTodoRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.MemoryUserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Autowired
    MemoryTodoRepository todoRepository;
    @Autowired MemoryUserRepository userRepository;

    @AfterEach
    void afterEach() {
        todoRepository.clearStore();
        userRepository.clearStore();
    }
//    UserService userService;
//    MemoryUserRepository userRepository;
//    ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
//
//    @BeforeEach
//    public void beforeEach() {
//        userService = ac.getBean(UserService.class);
//        userRepository = ac.getBean(MemoryUserRepository.class);
//    }


    @Test
    @DisplayName("유저를 생성하고 조회할 수 있어야 한다")
    void joinUser() {
        User user1 = new User(1L, "spring@gmail.com", "User1");

        userService.joinUser(user1);
        User userById = userService.findUserById(1L);

        assertThat(userById).isEqualTo(user1);
        assertThat(user1.getUserName()).isEqualTo("User1");
        assertThat(user1.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 탈퇴 성공 케이스")
    void quitUser() {
        User user1 = new User(1L, "spring@gmail.com", "User1");
        userService.joinUser(user1);

        userService.quitUser(1L, user1.getId());

        User userById = userService.findUserById(1L);
        assertThat(userById).isNull();
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 요청아이디와 타켓아이디 불일치")
    void quitUserFail() {
        User user1 = new User(1L, "spring@gmail.com", "User1");
        userService.joinUser(user1);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.quitUser(3L, 1L));
        assertThat(e.getMessage()).isEqualTo("유저 삭제 실패! 본인의 계정만 삭제할 수 있습니다.");
    }

    @Test
    @DisplayName("유저 탈퇴 실패 - 존재하지 않는 유저 삭제")
    void quitUserFail2() {
        Long temp = 123456L;
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> userService.quitUser(123456L, temp));
        assertThat(e.getMessage()).isEqualTo("유저 삭제 실패! 해당 유저가 없습니다.");
    }

    @Test
    @DisplayName("모든 유저 목록을 조회할 수 있다")
    void findUserAll() {
        userService.joinUser(new User(1L, "spring@gmail.com", "User1"));
        userService.joinUser(new User(2L, "java@gmail.com", "User2"));

        List<User> users = userService.findAll();
        assertThat(users.size()).isEqualTo(2);
    }
}