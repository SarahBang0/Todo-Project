package miniProject.todo_list.todo.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import miniProject.todo_list.user.Entity.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "Todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;

    @Column(name = "is_complete")
    private boolean isComplete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Todo() {
    }

    public Todo(Long id, String task, User user) {
        this.id = id;
        this.task = task;
        this.user = user;
        this.isComplete = false;
    }

    public boolean isComplete() {
        return isComplete;
    }


}
