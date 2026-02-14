package miniProject.todo_list.user.Repository;

import miniProject.todo_list.user.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findByUserName(String userName);
}
