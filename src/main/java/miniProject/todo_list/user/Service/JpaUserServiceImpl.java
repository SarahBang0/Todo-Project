package miniProject.todo_list.user.Service;

import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.user.Entity.User;
import miniProject.todo_list.user.Repository.JpaUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Primary
public class JpaUserServiceImpl implements UserService{

    private final JpaUserRepository jpaUserRepository;
    private final JpaTodoRepository jpaTodoRepository;

    public JpaUserServiceImpl(JpaUserRepository jpaUserRepository, JpaTodoRepository jpaTodoRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaTodoRepository = jpaTodoRepository;
    }

    @Override
    public void joinUser(User user) {
        System.out.println("전달받은 이메일 = [" + user.getEmail()+"]");

        if(user.getEmail()==null) {
            throw new IllegalStateException("이메일을 입력하세요");
        }
        User byEmail = jpaUserRepository.findByEmail(user.getEmail());
        if(byEmail!=null) {
            throw new IllegalStateException("유저 생성 실패! 이미 존재하는 이메일입니다.");
        }
        jpaUserRepository.save(user);
    }

    @Override
    public void quitUser(Long requestId, Long targetId) {
        if(!requestId.equals(targetId)) {
            throw new IllegalStateException("유저 삭제 실패! 요청된 Id와 삭제할 Id가 같지 않습니다.");
        }

        User user = jpaUserRepository.findById(targetId)
                .orElseThrow(()-> new IllegalStateException("유저 삭제 실패! 해당 Id의 유저가 없습니다."));
        jpaUserRepository.delete(user);
    }

    @Override
    public User findUserById(Long id) {
        return jpaUserRepository.findById(id).orElseThrow(() ->
                new IllegalStateException("조회 실패! 해당 Id의 유저가 없습니다."));
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll();
    }
}
