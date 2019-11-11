package thrift.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import thrift.logic.commands.AddExpenseCommand;
import thrift.logic.commands.AddIncomeCommand;
import thrift.logic.commands.BudgetCommand;
import thrift.logic.commands.CloneCommand;
import thrift.logic.commands.DeleteCommand;
import thrift.logic.commands.RedoCommand;
import thrift.logic.commands.TagCommand;
import thrift.logic.commands.UndoCommand;
import thrift.logic.commands.Undoable;
import thrift.logic.commands.UntagCommand;
import thrift.logic.commands.UpdateCommand;
import thrift.logic.commands.exceptions.CommandException;
import thrift.model.transaction.Expense;
import thrift.model.transaction.Income;
import thrift.testutil.ExpenseBuilder;
import thrift.testutil.IncomeBuilder;
import thrift.testutil.TagSetBuilder;
import thrift.testutil.TypicalIndexes;
import thrift.testutil.TypicalOccurrences;
import thrift.testutil.TypicalTransactions;
import thrift.testutil.UpdateTransactionDescriptorBuilder;

public class PastUndoCommandsTest {

    private PastUndoableCommands pastUndoableCommands;
    @BeforeEach
    public void setUp() {
        pastUndoableCommands = new PastUndoableCommands();
    }

    @Test
    public void addPastCommand_inputParameters() throws CommandException {
        CommandStub commandStub = new CommandStub();
        //throws exceptions
        assertThrows(NullPointerException.class, () -> pastUndoableCommands.addPastCommand(null));

        //valid undoable command
        pastUndoableCommands.addPastCommand(commandStub);
        assertTrue(pastUndoableCommands.hasUndoCommand());
    }

    @Test
    public void addPastCommand_purgeRedoStack_success() throws CommandException {
        CommandStub commandStub = new CommandStub();
        pastUndoableCommands.addPastCommand(commandStub);
        assertTrue(pastUndoableCommands.hasUndoCommand());
        assertFalse(pastUndoableCommands.hasRedoCommand());

        pastUndoableCommands.getCommandToUndo();
        assertFalse(pastUndoableCommands.hasUndoCommand());
        assertTrue(pastUndoableCommands.hasRedoCommand());

        pastUndoableCommands.addPastCommand(commandStub);
        assertFalse(pastUndoableCommands.hasRedoCommand());
        assertTrue(pastUndoableCommands.hasUndoCommand());
    }

    @Test
    public void getCommandToUndo_emptyUndoStack_throwException() throws CommandException {
        //no command in both redoStack and undoStack
        assertThrows(CommandException.class, () -> pastUndoableCommands.getCommandToUndo());

        //command at redoStack only
        CommandStub commandStub = new CommandStub();
        pastUndoableCommands.addPastCommand(commandStub);
        pastUndoableCommands.getCommandToUndo();
        assertThrows(CommandException.class, () -> pastUndoableCommands.getCommandToUndo());
    }

    @Test
    public void getCommandToUndo_nonEmptyUndoStack_success() throws CommandException {
        CommandStub commandStub = new CommandStub();
        pastUndoableCommands.addPastCommand(commandStub);
        Undoable undoableCommand = pastUndoableCommands.getCommandToUndo();
        assertEquals(commandStub, undoableCommand);
        assertTrue(pastUndoableCommands.hasRedoCommand());
    }

    @Test
    public void getCommandToRedo_emptyRedoStack_throwsException() {
        //no command in both redoStack and undoStack
        assertThrows(CommandException.class, () -> pastUndoableCommands.getCommandToRedo());

        //command at undoStack only
        CommandStub commandStub = new CommandStub();
        pastUndoableCommands.addPastCommand(commandStub);
        assertThrows(CommandException.class, () -> pastUndoableCommands.getCommandToRedo());
    }

    @Test
    public void getCommandToRedo_nonEmptyRedoStack_success() throws CommandException {
        CommandStub commandStub = new CommandStub();
        pastUndoableCommands.addPastCommand(commandStub);
        pastUndoableCommands.getCommandToUndo();
        Undoable undoableCommand = pastUndoableCommands.getCommandToRedo();
        assertEquals(commandStub, undoableCommand);
        assertTrue(pastUndoableCommands.hasUndoCommand());
    }

    private class CommandStub implements Undoable {
        @Override
        public String undo(Model model) {
            return null;
        }

        @Override
        public String redo(Model model) {
            return null;
        }
    }
}
