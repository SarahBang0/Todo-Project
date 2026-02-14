package miniProject.todo_list.todo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Todo {

    private Long id;
    private String task;
    private boolean isComplete;
    private Long userId;

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
