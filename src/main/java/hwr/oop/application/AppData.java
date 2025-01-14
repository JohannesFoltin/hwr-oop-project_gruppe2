package hwr.oop.application;

import java.util.List;
import java.util.Objects;

public class AppData {
    private final List<Project> projectList;
    private final List<User> userList;
    public AppData(List<Project> projectList, List<User> userList) {
        this.projectList = projectList;
        this.userList = userList;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void addProject(Project project) {
        projectList.add(project);
    }

    public void addUser(User user) {
        userList.add(user);
    }

    public void deleteProject(Project project) {
        projectList.remove(project);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppData appData = (AppData) o;
        return Objects.equals(projectList, appData.projectList) && Objects.equals(userList, appData.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectList, userList);
    }
}
