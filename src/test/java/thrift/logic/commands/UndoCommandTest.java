package thrift.logic.commands;

import static thrift.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import thrift.model.Model;
import thrift.model.ModelManager;
import thrift.model.UserPrefs;
import thrift.testutil.TypicalTransactions;

public class UndoCommandTest {

    private Model model = new ModelManager(TypicalTransactions.getTypicalThrift(), new UserPrefs());
    private Model expectedModel = new ModelManager(TypicalTransactions.getTypicalThrift(), new UserPrefs());

    @Test
    public void execute() {
        assertCommandSuccess(new UndoCommand(), model, UndoCommand.MESSAGE_SUCCESS, expectedModel);
    }

}
