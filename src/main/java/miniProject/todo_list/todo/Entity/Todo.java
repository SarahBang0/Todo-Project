package miniProject.todo_list.todo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;
    private boolean isComplete;
    private Long userId;

    protected Todo() {
    }

    public Todo(Long id, String task, Long userId) {
        this.id = id;
        this.task = task;
        this.userId = userId;
        this.isComplete = false;
    }

    public boolean getIsComplete() {
        return isComplete;
    }


}
