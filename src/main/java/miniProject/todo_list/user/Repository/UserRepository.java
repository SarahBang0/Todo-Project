package miniProject.todo_list.user.Repository;

import miniProject.todo_list.user.Entity.User;

import java.util.List;

public interface UserRepository {

    User createUser(User user);

    User deleteUser(Long id);

    User findById(Long id);

    User findByEmail(String email);

    List<User> findByName(String name);

    List<User> findAll();

//    List<Todo> findTodo (String email);
}
