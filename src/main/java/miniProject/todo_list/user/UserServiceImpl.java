package miniProject.todo_list.user;

import miniProject.todo_list.todo.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private TodoRepository todoRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TodoRepository todoRepository) {
        this.userRepository = userRepository;
        this.todoRepository = todoRepository;
    }


    @Override
    public void joinUser(User user) {
        userRepository.createUser(user);
    }

    @Override
    public User quitUser(Long requestId, Long targetId) {
        if(!requestId.equals(targetId)) {
            throw new IllegalStateException("유저 삭제 실패! 본인의 계정만 삭제할 수 있습니다.");
        }

        User user = userRepository.findById(targetId);
        if(user != null) {
            return userRepository.deleteUser(targetId);
        } else {
            throw new IllegalStateException("유저 삭제 실패! 해당 유저가 없습니다.");
        }

    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}
