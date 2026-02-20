package miniProject.todo_list.todo.Entity;

public enum Priority {
    HIGH("높음", 1),
    MEDIUM("중간", 2),
    LOW("낮음", 3);

    private final String description;
    private final int level;

    Priority(String description, int level) {
        this.description = description;
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public int getLevel() {
        return level;
    }
}
