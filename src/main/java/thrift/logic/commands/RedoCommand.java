package thrift.logic.commands;

import static java.util.Objects.requireNonNull;

import thrift.logic.commands.exceptions.CommandException;
import thrift.model.Model;

/**
 * Redo undone command in THRIFT.
 */
public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Redo undone command"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Redo successfully";

    public static final String NO_REDOABLE_COMMAND = "No valid command to redo";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        return new CommandResult(MESSAGE_SUCCESS);
    }
}
