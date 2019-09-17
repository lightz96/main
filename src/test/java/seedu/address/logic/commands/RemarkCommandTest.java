package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

/**
 * @author j-lum-reused
 */
public class RemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Person oldPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(oldPerson).withRemark("testing").build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("testing"));
        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(oldPerson, editedPerson);
        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_deleteRemarkUnfilteredList_success() {
        Person oldPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(oldPerson).withRemark("").build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON,
                new Remark(""));
        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(oldPerson, editedPerson);
        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_addRemarkFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person oldPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withRemark("").build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""));
        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS, editedPerson);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(oldPerson, editedPerson);
        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);

    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index index = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        RemarkCommand remarkCommand = new RemarkCommand(index, new Remark(VALID_REMARK_BOB));
        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);

    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        assertTrue(model.getAddressBook().getPersonList().size() > INDEX_SECOND_PERSON.getZeroBased());
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark(VALID_REMARK_BOB));
        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }


    @Test
    public void equals() {
        final RemarkCommand defaultCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY));

        //should return true
        RemarkCommand commandWithSameValues = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY));
        assertTrue(defaultCommand.equals(commandWithSameValues));

        //should return true
        assertTrue(defaultCommand.equals(defaultCommand));

        //should return false
        assertFalse(defaultCommand.equals(null));

        final RemarkCommand bobRemarkCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark(VALID_REMARK_BOB));
        final RemarkCommand amyRemarkCommand = new RemarkCommand(INDEX_SECOND_PERSON, new Remark(VALID_REMARK_AMY));
        final RemarkCommand amyRemarkCommand1 = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_BOB));

        //should return false
        assertFalse(defaultCommand.equals(bobRemarkCommand));

        //should return false
        assertFalse(defaultCommand.equals(amyRemarkCommand));

        //should return false
        assertFalse(defaultCommand.equals(amyRemarkCommand1));

        assertFalse(defaultCommand.equals(new ClearCommand()));

    }
}
