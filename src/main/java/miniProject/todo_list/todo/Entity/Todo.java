package miniProject.todo_list.todo.Entity;

import jakarta.persistence.*;
import lombok.*;
import miniProject.todo_list.user.Entity.User;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
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

    public void complete() {
        if(this.isComplete) {
            throw new IllegalStateException("이미 완료된 상태입니다.");
        } else {
            this.isComplete = true;
        }
    }

    public void unComplete() {
        if(!this.isComplete) {
            throw new IllegalStateException("미완료인 할 일은 미완료로 바꿀 수 없습니다.");
        } else {
            this.isComplete = false;
        }
    }


}
