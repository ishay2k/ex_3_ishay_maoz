package ascii_art;


/**
 * Represents a user command in the interactive ASCII art shell.
 * Each command should define how it is executed using the given Shell instance.
 */
public interface UserCommand {

    /**
     * Executes the command using the current shell context.
     *
     * @param shell the Shell instance containing the current state and logic
     */
    void execute();
}