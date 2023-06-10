package hwr.oop.application;

import hwr.oop.persistence.AppData;
import hwr.oop.persistence.LoadPort;
import hwr.oop.persistence.SavePort;

public class RemoveUserAccessService implements RemoveUserAccessUseCase {
    private final LoadPort loadPort;
    private final SavePort savePort;

    public RemoveUserAccessService(LoadPort loadPort, SavePort savePort) {
        this.loadPort = loadPort;
        this.savePort = savePort;
    }
    @Override
    public void removePermissionUser(Project project, User user){
        int ind = loadPort.loadData().getProjectList().indexOf(project);
        if(ind>=0) {
            AppData appData = loadPort.loadData();
            appData.getProjectList().get(ind).removePermissionUser(user);
            savePort.saveData(appData);
        }
        else{
            throw new CanNotFindProjectForPermissionChange("project not found");
        }
    }

}
