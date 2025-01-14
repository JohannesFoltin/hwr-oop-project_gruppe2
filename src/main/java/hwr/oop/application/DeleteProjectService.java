package hwr.oop.application;

import hwr.oop.inports.DeleteProjectUseCase;
import hwr.oop.outports.LoadPort;
import hwr.oop.outports.SavePort;

public class DeleteProjectService implements DeleteProjectUseCase {
    private LoadPort load;
    private SavePort save;

    public DeleteProjectService(LoadPort load, SavePort save) {
        this.load = load;
        this.save = save;
    }

    @Override
    public void deleteProject(Project project) {
        AppData appData = load.loadData();
        if (!appData.getProjectList().contains(project)) {
            throw new CantDeleteNonexistentProjectException("Project not in AppData");
        }
        appData.deleteProject(project);
        save.saveData(appData);
    }
}
