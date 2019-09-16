package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {

    private RemarkCommandParser parser = new RemarkCommandParser();

    @Test
    public void parse_indexProvided_Success() {

        String input = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_REMARK + "testing";
        assertParseSuccess(parser, input, new RemarkCommand(INDEX_FIRST_PERSON, new Remark("testing")));

    }

    @Test
    public void parse_argsMissing_Failed() {
        String input = RemarkCommand.COMMAND_WORD + " " + PREFIX_REMARK + "testing";
        assertParseFailure(parser, input, String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));

        String input1 = RemarkCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased();
        assertParseFailure(parser, input1, String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
    }
}
