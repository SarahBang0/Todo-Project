package miniProject.todo_list.user;

import java.util.List;

public interface UserService {

    void joinUser(User user);

    User quitUser(Long requestId, Long targetId);

    User findUserById(Long id);

    List<User> findAll();




}
