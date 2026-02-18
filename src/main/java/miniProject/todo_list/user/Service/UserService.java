package miniProject.todo_list.user.Service;

import miniProject.todo_list.user.Entity.User;

import java.util.List;

public interface UserService {

    Long joinUser(User user);

    void quitUser(Long requestId, Long targetId);

    User findUserById(Long id);

    List<User> findAll();




}
