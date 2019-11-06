package thrift.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static thrift.logic.commands.CommandTestUtil.assertCommandSuccess;
import static thrift.logic.commands.CommandTestUtil.assertRedoCommandSuccess;
import static thrift.logic.commands.CommandTestUtil.assertUndoCommandSuccess;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import thrift.logic.commands.exceptions.CommandException;
import thrift.model.Model;
import thrift.model.ModelManager;
import thrift.model.UserPrefs;
import thrift.model.tag.Tag;
import thrift.model.transaction.Expense;
import thrift.model.transaction.Income;
import thrift.model.transaction.Transaction;
import thrift.testutil.ExpenseBuilder;
import thrift.testutil.TagSetBuilder;
import thrift.testutil.TypicalIndexes;
import thrift.testutil.TypicalTransactions;

class TagCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(TypicalTransactions.getTypicalThrift(), new UserPrefs());
    }

    @Test
    void execute_newTags_success() throws CommandException {
        Expense originalExpense = new ExpenseBuilder(model.getFilteredTransactionList().get(0))
                .build();

        Set<Tag> tagSet = new TagSetBuilder("Food", "Shopping").build();
        TagCommand tagCommand = new TagCommand(TypicalIndexes.INDEX_FIRST_TRANSACTION, tagSet);

        Set<Tag> expectedTag = new TagSetBuilder(originalExpense.getTags())
                .addTags("Food", "Shopping")
                .build();
        Expense updatedExpense = new Expense(
                originalExpense.getDescription(),
                originalExpense.getValue(),
                originalExpense.getRemark(),
                originalExpense.getDate(),
                expectedTag);

        assertNotEquals(updatedExpense.getTags(), model.getFilteredTransactionList().get(0).getTags()); //Before
        tagCommand.execute(model, null);
        assertEquals(updatedExpense.getTags(), model.getFilteredTransactionList().get(0).getTags()); //After
    }

    @Test
    void undoAndRedo_tagForExpense_success() {
        Model expectedModel = new ModelManager(model.getThrift(), new UserPrefs());
        Transaction lastTransaction = model.getFilteredTransactionList()
                .get(TypicalIndexes.INDEX_THIRD_TRANSACTION.getZeroBased());
        //checks if lastTransaction is an expense
        assertTrue(lastTransaction instanceof Expense);
        //assuming that it is tagging the transaction with non-duplicate tag
        Set<Tag> tagSet = new TagSetBuilder("Food", "Recommended").build();
        Set<Tag> updatedTags = new HashSet<Tag>(lastTransaction.getTags());
        for (Tag newTag : tagSet) {
            updatedTags.add(newTag);
        }
        Transaction updatedTransaction = new Expense(lastTransaction.getDescription(), lastTransaction.getValue(),
                lastTransaction.getRemark(), lastTransaction.getDate(), updatedTags);
        String expectedMessageOriginal = String.format(TagCommand.MESSAGE_ORIGINAL_TRANSACTION, lastTransaction);
        String expectedMessageUpdated = String.format(TagCommand.MESSAGE_TAG_TRANSACTION_SUCCESS,
                updatedTransaction);

        //tests tag command
        TagCommand tagCommand = new TagCommand(TypicalIndexes.INDEX_THIRD_TRANSACTION, tagSet);
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_THIRD_TRANSACTION, updatedTransaction);
        assertCommandSuccess(tagCommand, model, expectedMessageUpdated + expectedMessageOriginal,
                expectedModel);

        //tests undo command
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_THIRD_TRANSACTION, lastTransaction);
        assertUndoCommandSuccess(tagCommand, model, expectedModel);

        //tests redo command
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_THIRD_TRANSACTION, updatedTransaction);
        assertRedoCommandSuccess(tagCommand, model, expectedModel);
    }

    @Test
    void undoAndRedo_tagForIncome_success() {
        Model expectedModel = new ModelManager(model.getThrift(), new UserPrefs());
        Transaction lastTransaction = model.getFilteredTransactionList()
                .get(TypicalIndexes.INDEX_SECOND_TRANSACTION.getZeroBased());
        //checks if lastTransaction is an income
        assertTrue(lastTransaction instanceof Income);
        //assuming that it is tagging the transaction with non-duplicate tag
        Set<Tag> tagSet = new TagSetBuilder("School").build();
        Set<Tag> updatedTags = new HashSet<Tag>(lastTransaction.getTags());
        for (Tag newTag : tagSet) {
            updatedTags.add(newTag);
        }
        Transaction updatedTransaction = new Income(lastTransaction.getDescription(), lastTransaction.getValue(),
                lastTransaction.getRemark(), lastTransaction.getDate(), updatedTags);
        String expectedMessageOriginal = String.format(TagCommand.MESSAGE_ORIGINAL_TRANSACTION, lastTransaction);
        String expectedMessageUpdated = String.format(TagCommand.MESSAGE_TAG_TRANSACTION_SUCCESS,
                updatedTransaction);

        //tests tag command
        TagCommand tagCommand = new TagCommand(TypicalIndexes.INDEX_SECOND_TRANSACTION, tagSet);
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_SECOND_TRANSACTION, updatedTransaction);
        assertCommandSuccess(tagCommand, model, expectedMessageUpdated + expectedMessageOriginal,
                expectedModel);

        //tests undo command
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_SECOND_TRANSACTION, lastTransaction);
        assertUndoCommandSuccess(tagCommand, model, expectedModel);

        //tests redo command
        expectedModel.setTransactionWithIndex(TypicalIndexes.INDEX_SECOND_TRANSACTION, updatedTransaction);
        assertRedoCommandSuccess(tagCommand, model, expectedModel);
    }

    @Test
    void testEquals() {
        Set<Tag> setOne = new TagSetBuilder("Food", "Shopping").build();
        Set<Tag> setTwo = new TagSetBuilder("Debt", "Fees").build();
        TagCommand firstTagCommand = new TagCommand(TypicalIndexes.INDEX_FIRST_TRANSACTION, setOne);
        TagCommand secondTagCommand = new TagCommand(TypicalIndexes.INDEX_FIRST_TRANSACTION, setTwo);

        // same object -> returns true
        assertTrue(firstTagCommand.equals(firstTagCommand));

        // same values -> returns true
        TagCommand firstTagCommandCopy = new TagCommand(TypicalIndexes.INDEX_FIRST_TRANSACTION, setOne);
        assertTrue(firstTagCommand.equals(firstTagCommandCopy));

        // different types -> returns false
        assertFalse(firstTagCommand.equals(1));

        // null -> returns false
        assertFalse(firstTagCommand.equals(null));

        // different transaction -> returns false
        assertFalse(firstTagCommand.equals(secondTagCommand));
    }

}
