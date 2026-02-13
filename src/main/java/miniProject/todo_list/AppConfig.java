package miniProject.todo_list;


import miniProject.todo_list.todo.TodoListRepository;
import miniProject.todo_list.todo.TodoListServiceImpl;
import miniProject.todo_list.todo.TodoRepository;
import miniProject.todo_list.todo.TodoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TodoService todoService(TodoRepository todoRepository) {
        System.out.println("Call TodoService");
        return new TodoListServiceImpl(todoRepository);
    }

    @Bean
    public TodoRepository todoRepository() {
        System.out.println("Call TodoRepository");
        return new TodoListRepository();
    }

}
