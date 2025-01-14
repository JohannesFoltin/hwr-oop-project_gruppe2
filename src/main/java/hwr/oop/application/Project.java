package hwr.oop.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Project {
    private final UUID id;
    private List<Task> taskList;
    private String title;
    private Map<User, Boolean> permissions;

    public Project(UUID id, List<Task> taskList, String title, Map<User, Boolean> permissions) {
        this.id = id;
        this.taskList = taskList;
        this.title = title;
        this.permissions = permissions;
    }

    public UUID getId() {
        return id;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public String getTitle() {
        return title;
    }

    public Map<User, Boolean> getPermissions() {
        return permissions;
    }

    public void changeTitle(String newTitle){
        title = newTitle;
    }
    public void changePermission(User user, Boolean permission){
            permissions.put(user, permission);
    }
    public void removePermissionUser(User user){
        permissions.remove(user);
    }

    public void addTask(Task task) {
        taskList.add(task);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return Objects.equals(getId(), project.getId()) &&
                Objects.equals(getTaskList(), project.getTaskList()) &&
                Objects.equals(getTitle(), project.getTitle()) &&
                Objects.equals(getPermissions(), project.getPermissions());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTaskList(), getTitle(), getPermissions());
    }

    @Override
    public String toString() {
        return id.toString() + " - " + title;
    }
}
