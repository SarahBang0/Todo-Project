package miniProject.todo_list.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {
    private Long id;
    private String email;
    private String userName;

    public User(Long id, String email, String userName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
    }

}
