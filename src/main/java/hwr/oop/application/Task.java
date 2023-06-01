package hwr.oop.application;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Task {
    public Task(String title, String content, TaskState taskState, LocalDateTime deadline) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.content = content;
        this.taskState = taskState;
        this.deadline = deadline;
    }

    private final UUID id;
    private final String title;
    private final String content;
    private TaskState taskState;
    private final LocalDateTime deadline;

    public Optional<LocalDateTime> getDeadline() {
        return Optional.ofNullable(deadline);
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public TaskState getTaskState() {
        return taskState;
    }


    //change TaskState to BACKLOG
    public void resetTask() {
        if (taskState == TaskState.IN_PROGRESS) {
            taskState = TaskState.BACKLOG;
        } else {
            throw new TaskStateException("task can't be reset with taskState" + taskState.name());
        }
    }

    //change TaskState to IN_PROGRESS
    public void startTask() {
        if (taskState == TaskState.BACKLOG || taskState == TaskState.IN_REVIEW || taskState == TaskState.DONE) {
            taskState = TaskState.IN_PROGRESS;
        } else {
            throw new TaskStateException("task can't be started with taskState" + taskState.name());
        }
    }

    //change TaskState to IN_REVIEW
    public void reviewTask() {
        if (taskState == TaskState.IN_PROGRESS) {
            taskState = TaskState.IN_REVIEW;
        } else {
            throw new TaskStateException("task can't be finished with taskState" + taskState.name());
        }
    }

    //change TaskState to DONE
    public void completeTask() {
        if (taskState == TaskState.IN_REVIEW || taskState== TaskState.IN_PROGRESS) {
            taskState = TaskState.DONE;
        } else {
            throw new TaskStateException("task can't be completed with taskState" + taskState.name());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            return (Objects.equals(((Task) obj).id, this.id));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, content, taskState, deadline);
    }
}