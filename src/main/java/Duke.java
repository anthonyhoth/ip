import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.ArrayList;

public class Duke {
    // Initialise the task list
    private static ArrayList<Task> taskList = new ArrayList<>();

    public static void main(String[] args) {
        greetUser();
        loadFile();
        main();
    }

    /**
     * Prints out the greeting message.de
     */
    private static void greetUser() {
        System.out.println("Hello! I'm Jukebox :)\n" + "What can I do for you today?");
    }

    private static void loadFile() {
        try {
            SaveLoad.loadTaskList(taskList);
            System.out.println("Loading save file... Done!");
        } catch (DukeException e) {
            System.out.println("Hmm... looks like you don't have an existing save file, let's make one!");
        }
    }

    /**
     * Prints out the exit message.
     */
    private static void exitJukebox() {
        System.out.println("Aww... OK, Hope to see you again!");
    }

    /**
     * Main logic handler.
     */
    public static void main() {
        // Variables
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        // Logic handler
        while (true) {
            input = sc.nextLine();
            String[] inputWordsSplit = input.split(" ", 2);
            String commandWord = inputWordsSplit[0];
            CommandWord command = null;

            // Updating the command word, catching any invalid inputs
            try {
                command = CommandWord.getCommand(commandWord);
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }



            // Print out goodbye message and exits the program
            if (command == CommandWord.BYE) {
                exitJukebox();
                SaveLoad.saveTaskList(taskList);
                break;
            }

            // Prints out the task list
            if (command == CommandWord.LIST) {
                printList(taskList);
                continue;
            }

            // Marks or unmarks the given task
            if (command == CommandWord.MARK || command == CommandWord.UNMARK) {
                markUnmarkTask(command, inputWordsSplit);
                continue;
            }

            // Deletes a task from the task list
            if (command == CommandWord.DELETE) {
                deleteTask(inputWordsSplit);
            }

            // Resolving description of task to contents
            String contents = new String();
            // Checking for invalid description
            try {
                contents = inputWordsSplit[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Hmm... You forgot to indicate your task description!");
            }

            switch (command) {
            case TODO: {
                try {
                    contents = inputWordsSplit[1];
                    Task newTask = new Todo(contents);
                    taskList.add(newTask);
                    System.out.println("Okay!\n" + "Added: " + newTask + "\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                } finally {
                    break;
                }
            }

            case DEADLINE: {
                String[] split = contents.split(" /by ");
                try {
                    LocalDateTime dateTime = DateTime.parseDate(split[1]);
                    String taskDescription = split[0];
                    Task newTask = new Deadline(taskDescription, dateTime);
                    taskList.add(newTask);
                    System.out.println("Okay!\n" + "Added: " + newTask + "\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please indicate the date & time of this deadline using /by !");
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                } finally {
                    break;
                }
            }

            case EVENT: {
                String[] split = contents.split(" /at ");
                try {
                    LocalDateTime dateTime = DateTime.parseDate(split[1]);
                    String taskDescription = split[0];
                    Task newTask = new Event(taskDescription, dateTime);
                    taskList.add(newTask);
                    System.out.println("Okay!\n" + "Added: " + newTask + "\n");
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Please indicate the date & time of this event using /at !");
                } catch (DukeException e) {
                    System.out.println(e.getMessage());
                } finally {
                    break;
                }
            }
            }
        }
    }


        /**
         * Prints out the task list iteratively.
         * @param taskList Input task list
         */
        private static void printList (ArrayList < Task > taskList) {
            if (!taskList.isEmpty()) {
                System.out.println("These are your current tasks! :)");
                int i = 1;
                for (Task t : taskList) {
                    String s = String.format("%d.%s", i, t);
                    System.out.println(s);
                    i++;
                    if (t == null) {
                        break;
                    }
                }
                System.out.println(" ");
            } else {
                System.out.println("No tasks have been added yet!\n");
            }
        }

        /**
         * Marks or unmarks the task.
         * @param command Input command word
         * @param inputWordsSplit String array of the command word with the task number
         */
        private static void markUnmarkTask (CommandWord command, String[] inputWordsSplit){
            try {
                int taskNumber = Integer.parseInt(inputWordsSplit[1]);
                Task task = taskList.get(taskNumber - 1);
                if (command == CommandWord.MARK) {
                    task.mark();
                    System.out.println(String.format("Goodjob! This task is now completed :)\n" + "%s", task.toString() + "\n"));
                } else {
                    task.unmark();
                    System.out.println(String.format("Oh... OK, I'll mark this task as uncompleted!\n" + "%s", task.toString() + "\n"));
                }
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please indicate a valid task number! :)\n");
            }
        }

        /**
        * Handles the deletion of the task from the task list.
        * @param inputWordsSplit String array of the command word with the task number
        */
        private static void deleteTask(String[] inputWordsSplit) {
            try {
                int taskNumber = Integer.parseInt(inputWordsSplit[1]);
                Task task = taskList.get(taskNumber - 1);
                System.out.println("Ok! I've removed this task: \n" + task + "\n");
                taskList.remove(taskNumber - 1);
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Please indicate a valid task number! :)\n");
            }
        }

        /**
         * Enum for the command word
         */
        private enum CommandWord {
            LIST("list"),
            MARK("mark"),
            UNMARK("unmark"),
            TODO("todo"),
            DEADLINE("deadline"),
            EVENT("event"),
            DELETE("delete"),
            BYE("bye");

            // String field for comparing to the input to the enum
            private final String command;

            /**
             * Constructor for the CommandWord enum
             * @param command User input command
             */
            CommandWord(String command) {
                this.command = command;
            }

            /**
             * Compares the input command to the enum.
             * @param input Input command string
             * @return CommandWord from the enum that matches the input command
             * @throws IllegalArgumentException When an invalid command is inputted
             */
            private static CommandWord getCommand(String input) throws DukeException {
                for (int i = 0; i < CommandWord.values().length; i++) {
                    CommandWord temp = CommandWord.values()[i];
                    if (input.equals(temp.command)) {
                        return temp;
                    }
                }
                throw new DukeException("There is no such command called " + input + "!\n" + "Please try again :)\n");
            }
        }
    }

