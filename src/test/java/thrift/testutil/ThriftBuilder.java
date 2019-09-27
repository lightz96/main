package thrift.testutil;

import thrift.model.AddressBook;
import thrift.model.transaction.Transaction;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new ThriftBuilder().withPerson("John", "Doe").build();}
 */
public class ThriftBuilder {

    private AddressBook addressBook;

    public ThriftBuilder() {
        addressBook = new AddressBook();
    }

    public ThriftBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Transaction} to the {@code AddressBook} that we are building.
     */
    public ThriftBuilder withDescription(Transaction transaction) {
        addressBook.addTransaction(transaction);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
