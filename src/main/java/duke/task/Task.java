package duke.task;

/**
 * Class for Duke Tasks.
 */
public class Task {
    protected String name;
    private boolean isCompleted;

    /**
     * Constructor for the Task class.
     * @param name Input description of the task
     */
    public Task(String name) {
        this.name = name;
        this.isCompleted = false;
    }

    /**
     * Gets the completion status of the task
     * @return A status icon if its completed/uncompleted
     */
    public String getStatus() {
        return isCompleted ? "X" : " ";
    }

    /**
     * Marks the task to show completed
     */
    public void mark() {
        this.isCompleted = true;
    }

    /**
     * Unmarks the task to show uncompleted
     */
    public void unmark() {
        this.isCompleted = false;
    }

    /**
     * Returns a string representation of the Task object.
     * @return String representation of the Task object.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s ", getStatus(), name);
    }

    /**
     * Returns the format of Task object in format to be saved. 
     * @return String of Task object to be saved.
     */
    public String changeFormat() {
        return String.format("[%s] | %s", getStatus(), name);
    };
}
