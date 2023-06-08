package hwr.oop.application;

import hwr.oop.persistence.AppData;
import hwr.oop.persistence.LoadPort;
import hwr.oop.persistence.UserNotInAppDataException;

import java.util.List;

public class ValidateUserService implements ValidateUserUseCase{
    private final LoadPort loadPort;

    public ValidateUserService(LoadPort loadPort) {
        this.loadPort = loadPort;
    }

    @Override
    public User validateUser(String name) {
        List<User> users = loadPort.loadData().getUserList();
        for (User user : users) {
            if (user.getName().equals(name)){
                return user;
            }

        }
        throw new UserNotInAppDataException("User not in appdata no shit");
    }
}
