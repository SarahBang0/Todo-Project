/*
package miniProject.todo_list.todo.Service;


import miniProject.todo_list.todo.TodoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TodoController.class)
public class TodoControllerTest {

    @Autowired
    JpaTodoServiceImpl jpaTodoService;
    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("할 일 생성 시 제목을 입력하지 않으면 400 Bad Request 반환")
    void taskNotBlankValidation() {
        String json = "{\"task\": \"\", \"userId\": 1}";

        mockMvc.perform(post("/api/todos") // 실제 컨트롤러 매핑 주소
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); // 400 에러가 나는지 확인
    }
}
*/
