package thrift.logic.parser;

import static java.util.Objects.requireNonNull;

import thrift.commons.core.Messages;
import thrift.logic.commands.AddExpenseCommand;
import thrift.logic.commands.AddIncomeCommand;
import thrift.logic.commands.HelpCommand;
import thrift.logic.parser.exceptions.ParseException;

/**
 * Parses user input and creates a new HelpCommand object.
 */
public class HelpCommandParser implements Parser<HelpCommand> {

    public static final String EMPTY_STRING = "";

    @Override
    public HelpCommand parse(String userInput) throws ParseException {
        requireNonNull(userInput);
        switch (userInput.trim()) {
        case AddExpenseCommand.COMMAND_WORD:
            return new HelpCommand(AddExpenseCommand.MESSAGE_USAGE);
        case AddIncomeCommand.COMMAND_WORD:
            return new HelpCommand(AddIncomeCommand.MESSAGE_USAGE);
        case EMPTY_STRING:
            return new HelpCommand(EMPTY_STRING);
        default:
            throw new ParseException(String.format(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT,
                    HelpCommand.MESSAGE_USAGE)));
        }
    }
}
