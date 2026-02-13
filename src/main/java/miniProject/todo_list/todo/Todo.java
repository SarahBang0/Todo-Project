package miniProject.todo_list.todo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Todo {

    private Long id;
    private String task;
    private boolean isComplete;

    public Todo(Long id, String title) {
        this.id = id;
        this.task = title;
        this.isComplete = false;
    }

    public boolean getIsComplete() {
        return isComplete;
    }


}
