package miniProject.todo_list;


import miniProject.todo_list.todo.MemroyTodoRepository;
import miniProject.todo_list.todo.TodoServiceImpl;
import miniProject.todo_list.todo.TodoRepository;
import miniProject.todo_list.todo.TodoService;
import miniProject.todo_list.user.MemoryUserRepository;
import miniProject.todo_list.user.UserRepository;
import miniProject.todo_list.user.UserService;
import miniProject.todo_list.user.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public TodoService todoService(TodoRepository todoRepository, UserRepository userRepository) {
        System.out.println("Call TodoService");
        return new TodoServiceImpl(todoRepository, userRepository);
    }

    @Bean
    public TodoRepository todoRepository() {
        System.out.println("Call TodoRepository");
        return new MemroyTodoRepository();
    }

    @Bean
    public UserService userService(UserRepository userRepository, TodoRepository todoRepository) {
        System.out.println("Call UserService");
        return new UserServiceImpl(userRepository, todoRepository);
    }

    @Bean
    public UserRepository userRepository() {
        System.out.println("Call UserRepository");
        return new MemoryUserRepository();
    }

}
