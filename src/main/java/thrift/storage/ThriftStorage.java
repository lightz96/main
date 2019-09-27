package thrift.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import thrift.commons.exceptions.DataConversionException;
import thrift.model.ReadOnlyThrift;
import thrift.model.Thrift;

/**
 * Represents a storage for {@link Thrift}.
 */
public interface ThriftStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAddressBookFilePath();

    /**
     * Returns Thrift data as a {@link ReadOnlyThrift}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyThrift> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyThrift> readAddressBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyThrift} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyThrift addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyThrift)
     */
    void saveAddressBook(ReadOnlyThrift addressBook, Path filePath) throws IOException;

}
