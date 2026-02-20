package miniProject.todo_list.common;

public record ErrorResponse(String message, int status) {
    // message : 에러 메세지
    // status : Http 상태 코드
}
