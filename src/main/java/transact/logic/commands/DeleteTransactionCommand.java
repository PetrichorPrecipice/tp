package transact.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import transact.commons.core.index.Index;
import transact.commons.util.ToStringBuilder;
import transact.logic.Messages;
import transact.logic.commands.exceptions.CommandException;
import transact.model.Model;
import transact.model.transaction.Transaction;
import transact.ui.MainWindow.TabWindow;

/**
 * Deletes a transaction identified using it's displayed index from the
 * transaction book.
 */
public class DeleteTransactionCommand extends Command {

    public static final String COMMAND_WORD = "del";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes transaction identified by the index number used in the transaction list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_TRANSACTION_SUCCESS = "Deleted Transaction: %1$s";

    private final Index targetIndex;

    public DeleteTransactionCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Transaction> lastShownList = model.getFilteredTransactionList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TRANSACTION_DISPLAYED_INDEX);
        }

        Transaction transactionToDelete = lastShownList.get(targetIndex.getZeroBased());
        model.deleteTransaction(transactionToDelete);
        return new CommandResult(
                String.format(MESSAGE_DELETE_TRANSACTION_SUCCESS, Messages.format(transactionToDelete)),
                TabWindow.TRANSACTIONS);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteTransactionCommand)) {
            return false;
        }

        DeleteTransactionCommand otherDeleteTransactionCommand = (DeleteTransactionCommand) other;
        return targetIndex.equals(otherDeleteTransactionCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}