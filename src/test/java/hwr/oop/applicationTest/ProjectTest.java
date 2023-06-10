package hwr.oop.applicationTest;

import hwr.oop.application.*;
import hwr.oop.persistence.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

class ProjectTest {
    String directory = "./OOPTest/";
    AppData appData;
    CreateProjectUseCase createProject;
    LoadPort load;
    SavePort save;

    @BeforeEach
    void setUp() {
        load = new PersistenceAdapter(directory);
        save = new PersistenceAdapter(directory);
        createProject = new CreateProjectService(load, save);

        final File parent = new File(directory);
        parent.mkdirs();
        File file = new File(directory + "AppData.json");
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        appData = load.loadData();
    }

    static Stream<Arguments> randomProjects() {
        return Stream.of(
                Arguments.of(
                        RandomTestData.getRandomProject(),
                        RandomTestData.getRandomProject(),
                        RandomTestData.getRandomProject(),
                        RandomTestData.getRandomProject(),
                        RandomTestData.getRandomProject()
                )
        );
    }

    static Stream<Arguments> randomProjectsWithSingleUser() {
        List<Arguments> randomProjects = new ArrayList<>();
        for (int i=0; i<5; i++) {
            Project rando = RandomTestData.getRandomProject();
            User user = RandomTestData.getRandomUser();
            randomProjects.add(
                    Arguments.of(
                            new Project(rando.getId(), rando.getTaskList(), rando.getTitle(),
                                    Map.of(user, Boolean.TRUE)
                            ), user
                    )
            );
        }
        return  randomProjects.stream();
    }


    @Test
    void getIdTest() {
        UUID id = UUID.randomUUID();
        Project project = new Project(id, null, null, null);
        appData.addProject(project);
        save.saveData(appData);

        Project result = load.loadProjectById(project.getId());
        assertThat(result.getId()).isEqualTo(id);
    }

    @Test
    void getTitleTest() {
        String title = "testTitle";
        Project project = new Project(UUID.randomUUID(), null, title, null);
        appData.addProject(project);
        save.saveData(appData);

        Project result = load.loadProjectById(project.getId());
        assertThat(result.getTitle()).isEqualTo(title);
    }

    @Test
    void getTaskListTest() {
        List<Task> taskList = RandomTestData.getRandomtaskList();
        Project project = new Project(UUID.randomUUID(), taskList, null, null);
        appData.addProject(project);
        save.saveData(appData);

        Project result = load.loadProjectById(project.getId());
        assertThat(result.getTaskList()).isEqualTo(taskList);
    }

    @Test
    void getPermissionsTest() {
        Map<User, Boolean> permissions = RandomTestData.getRandomPermissions();
        Project project = new Project(UUID.randomUUID(), null, null, permissions);
        appData.addProject(project);
        save.saveData(appData);

        Project result = load.loadProjectById(project.getId());
        assertThat(result.getPermissions()).isEqualTo(permissions);
    }

    @Test
    void loadProjectWithWrongId_throwsException(){
        appData = new AppData(RandomTestData.getRandomProjects(), RandomTestData.getRandomUsers());
        save.saveData(appData);

        UUID uuid = UUID.randomUUID();
        try {
            load.loadProjectById(uuid);
            fail("should throw exception");
        } catch (ProjectNotInAppDataException e) {
            e.printStackTrace();
        }
    }

    @ParameterizedTest
    @MethodSource("randomProjects")
    void equalsTest(Project project) {
        Project copy = new Project(project.getId(), project.getTaskList(), project.getTitle(),
                project.getPermissions());
        assertThat(project).isEqualTo(copy);
        assertThat(copy).isEqualTo(project);
        assertThat(project.hashCode()).isEqualTo(copy.hashCode());
    }

    @ParameterizedTest
    @MethodSource("randomProjects")
    void equals_sameProjectDifferentID_returnsFalseTest(Project project) {
        Project copy = new Project(UUID.randomUUID(), project.getTaskList(), project.getTitle(),
                project.getPermissions());
        assertThat(project).isNotEqualTo(copy);
        assertThat(copy).isNotEqualTo(project);
        assertThat(project.hashCode()).isNotEqualTo(copy.hashCode());
    }

    @ParameterizedTest
    @MethodSource("randomProjects")
    void equals_differentClasses_returnsFalseTest(Project project) {
        Object o = new Object();
        assertThat(project).isNotEqualTo(o);
    }

    @ParameterizedTest
    @MethodSource("randomProjects")
    void equalsSameObjectTest(Project project) {
        assertThat(project).isEqualTo(project);
    }

    @ParameterizedTest
    @MethodSource("randomProjectsWithSingleUser")
    void canCreateProject(Project expected, User user) {
        Project result = createProject.createProject(expected.getTitle(), expected.getTaskList(), user);

        assertThat(result.getTitle()).hasToString(expected.getTitle());
        assertThat(result.getTaskList()).isEqualTo(expected.getTaskList());
        assertThat(result.getPermissions()).isEqualTo(expected.getPermissions());
    }

    @ParameterizedTest
    @MethodSource("randomProjectsWithSingleUser")
    void createProject_AddsOneToProjectListTest(Project project, User user) {
        AppData appData = new AppData(RandomTestData.getRandomProjects(), RandomTestData.getRandomUsers());
        save.saveData(appData);

        int originalSize = appData.getProjectList().size();
        createProject.createProject(project.getTitle(), project.getTaskList(), user);

        assertThat(load.loadData().getProjectList().size()).isEqualTo(originalSize+1);
    }

    @Test
    void wrongId_throwsException() {
        Project project = RandomTestData.getRandomProject();
        appData.addProject(project);
        UUID uuid = UUID.randomUUID();

        try {
            Project result = load.loadProjectById(uuid);
            fail("should throw exception");
        } catch (ProjectNotInAppDataException e) {
            e.printStackTrace();
        }
    }
    @Test
    void removeAccessUserSuccesful(){
        User user = new User(UUID.randomUUID(),"name", null,null);
        Map <User, Boolean> permissions= new HashMap<>();
        Project project = new Project(UUID.randomUUID(),null,"title",permissions);
        project.getPermissions().put(user,true);
        project.removePermissionUser(user);
        assertThat(project.getPermissions().containsKey(user)).isEqualTo(false);
    }
    @Test
    void removeAccessUserUnsuccesful(){
        User user = new User(UUID.randomUUID(),"name", null,null);
        Map <User, Boolean> permissions= new HashMap<>();
        Project project = new Project(UUID.randomUUID(),null,"title",permissions);
        try {
            project.removePermissionUser(user);
            fail("user not found");
        }catch (CanNotFindProjectForPermissionChange e){
            e.printStackTrace();
        }
    }
    @Test
    void addAccessUser(){
        User user = new User(UUID.randomUUID(),"name", null,null);
        Map <User, Boolean> permissions= new HashMap<>();
        Project project = new Project(UUID.randomUUID(),null,"title",permissions);
        project.changePermission(user,true);
        assertThat(project.getPermissions().get(user)).isEqualTo(true);
        project.changePermission(user,false);
        assertThat(project.getPermissions().get(user)).isEqualTo(false);
    }
}
