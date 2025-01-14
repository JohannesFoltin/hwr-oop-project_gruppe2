package hwr.oop.applicationTest;

import hwr.oop.application.GetProjectsOfUserService;
import hwr.oop.application.Project;
import hwr.oop.application.User;
import hwr.oop.application.AppData;
import hwr.oop.outports.LoadPort;
import hwr.oop.persistence.PersistenceAdapter;
import hwr.oop.outports.SavePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.*;


class ListProjectsOfUserTest {
    LoadPort load;
    SavePort save;
    AppData appData;
    GetProjectsOfUserService service;
    List<Project> projects;
    User user;

    @BeforeEach
    void setUp() {
        load = new PersistenceAdapter("./OOPTest/");
        save = new PersistenceAdapter("./OOPTest/");
        appData = new AppData(new ArrayList<>(), new ArrayList<>());
        service = new GetProjectsOfUserService(load);

        projects = new ArrayList<>();
        user = RandomTestData.getRandomUser();
        for (int i=0; i<5; i++) {
            Project rando = RandomTestData.getRandomProject();
            projects.add(new Project(rando.getId(), null, null, Map.of(user, Boolean.TRUE)));
        }
    }

    @Test
    void listProjectsTest() {
        for (Project project : projects) {
            appData.addProject(project);
        }
        save.saveData(appData);

        assertThat(service.getProjects(user)).isEqualTo(load.loadAllUserProjects(user.getId()));
    }
}
