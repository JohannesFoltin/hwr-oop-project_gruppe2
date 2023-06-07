package hwr.oop.application;

import hwr.oop.persistence.LoadPort;
import hwr.oop.persistence.SavePort;

import java.util.UUID;

public class DeleteProjectService implements DeleteProjectUseCase {
    private LoadPort load;
    private SavePort save;

    public DeleteProjectService(LoadPort load, SavePort save) {
        this.load = load;
        this.save = save;
    }

    @Override
    public void deleteProject(UUID id) {

    }
}
