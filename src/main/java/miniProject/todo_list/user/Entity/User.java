package miniProject.todo_list.user.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String userName;

    protected User() {
    }

    public User(Long id, String email, String userName) {
        this.id = id;
        this.email = email;
        this.userName = userName;
    }

}
