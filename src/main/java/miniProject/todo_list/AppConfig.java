package miniProject.todo_list;


import miniProject.todo_list.todo.Repository.MemroyTodoRepository;
import miniProject.todo_list.todo.Repository.TodoRepository;
import miniProject.todo_list.todo.Service.TodoServiceImpl;
import miniProject.todo_list.todo.Repository.JpaTodoRepository;
import miniProject.todo_list.todo.Service.TodoService;
import miniProject.todo_list.user.Repository.MemoryUserRepository;
import miniProject.todo_list.user.Repository.UserRepository;
import miniProject.todo_list.user.Service.UserService;
import miniProject.todo_list.user.Service.UserServiceImpl;
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
    public UserService userService(UserRepository userRepository, JpaTodoRepository todoRepository) {
        System.out.println("Call UserService");
        return new UserServiceImpl(userRepository, todoRepository);
    }

    @Bean
    public UserRepository userRepository() {
        System.out.println("Call UserRepository");
        return new MemoryUserRepository();
    }

}
