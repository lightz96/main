package thrift.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import thrift.logic.commands.AddExpenseCommand;
import thrift.logic.commands.Command;
import thrift.model.transaction.Expense;
import thrift.testutil.ExpenseBuilder;

public class PastUndoCommandsTest {

    private PastUndoableCommands pastUndoableCommands = new PastUndoableCommands();

    @Test
    public void addPassCommand_addCommandToUndoStack() {
        Expense validExpense = new ExpenseBuilder().build();
        Command addExpenseCommand = new AddExpenseCommand(validExpense);

        pastUndoableCommands.addPastCommand(addExpenseCommand);
        assertEquals(addExpenseCommand, pastUndoableCommands.getCommandToUndo());
    }
}
