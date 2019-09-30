package thrift.model;

import java.util.Stack;

import thrift.logic.commands.Command;

/**
 * Stores all the past undoable commands executed by the user.
 */
public class PastUndoableCommands {

    private final Stack<Command> undoStack;

    public PastUndoableCommands() {
        this.undoStack = new Stack<>();
    }

    public void addPastCommand(Command command) {
        undoStack.push(command);
    }

    public Command getCommandToUndo() {
        return undoStack.pop();
    }
}
