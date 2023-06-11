package hwr.oop.userinterface;

import hwr.oop.application.*;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Scanner;

public class AddTaskToProjectMenu {
    private final Scanner input;
    private final PrintStream output;
    private final EditProjectMenu editProjectMenu;
    private final CreateTaskUseCase createTaskUseCase;

    public AddTaskToProjectMenu(Scanner input, PrintStream output, EditProjectMenu editProjectMenu,
                                CreateTaskUseCase createTaskUseCase) {
        this.input = input;
        this.output = output;
        this.editProjectMenu = editProjectMenu;
        this.createTaskUseCase = createTaskUseCase;
    }

    public void start(Project project, User user) {
        checkPermission(project, user);
        output.println("What do you want to name your task? \n");
        String title = input.nextLine();
        output.println("Add content of your new task: \n");
        String content = input.nextLine();

        TaskState state = chooseTaskState(project);
        Optional<LocalDateTime> deadline = chooseDeadline(project);

        if (deadline.isEmpty()) {
            createTaskUseCase.createTaskInProject(title, content, state, null, project);
        } else {
            createTaskUseCase.createTaskInProject(title, content, state, deadline.get(), project);
        }
    }

    private void checkPermission(Project project, User user) {
        if (!project.getPermissions().containsKey(user) || project.getPermissions().get(user).equals(Boolean.FALSE)) {
            output.println("You do not have the necessary permissions to add a Task to this Project");
            editProjectMenu.start(user, project);
        }
    }

    private Optional<LocalDateTime> chooseDeadline(Project project) {
        output.println("Do you want to add a deadline to your task? (y/n) \n");
        String deadlineChoice = input.nextLine();
        if (deadlineChoice.equals("y")) {
            output.println("Please enter deadline: \n");
            try {
                return Optional.of(LocalDateTime.parse(input.nextLine()));
            } catch (Exception e) {
                output.println("Invalid input, please try again. ");
                return chooseDeadline(project);
            }
        } else if (deadlineChoice.equals("n")) {
            return Optional.empty();
        } else {
            output.println("Invalid input, please try again. \n");
            return chooseDeadline(project);
        }
    }

    private TaskState chooseTaskState(Project project) {
        output.println("What state should your task have?");
        output.println("Type 1 to set the taskState to IN_PROGRESS");
        output.println("Type 2 to set the taskState to BACKLOG \n");
        String choice = input.nextLine();
        if (choice.equals("1")) {
            return TaskState.IN_PROGRESS;
        } else if (choice.equals("2")) {
            return TaskState.BACKLOG;
        } else {
            output.println("Invalid input, please try again. \n");
            return chooseTaskState(project);
        }
    }
}
