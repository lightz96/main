package thrift.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static thrift.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import thrift.commons.exceptions.DataConversionException;
import thrift.model.ReadOnlyThrift;
import thrift.model.Thrift;

public class JsonThriftStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data",
            "JsonThriftStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readAddressBook(null));
    }

    private java.util.Optional<ReadOnlyThrift> readAddressBook(String filePath) throws Exception {
        return new JsonThriftStorage(Paths.get(filePath)).readThrift(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readAddressBook("NonExistentFile.json").isPresent());
    }

    @Test
    public void read_notJsonFormat_exceptionThrown() {
        assertThrows(DataConversionException.class, () -> readAddressBook("notJsonFormatTransactionThrift.json"));
    }

    @Test
    public void readAddressBook_invalidTransactionThrift_throwDataConversionException() {
        assertThrows(DataConversionException.class, () -> readAddressBook("invalidTransactionThrift.json"));
    }

    @Test
    public void readAddressBook_invalidAndValidPersonAddressBook_throwDataConversionException() {
        assertThrows(DataConversionException.class, ()
            -> readAddressBook("invalidAndValidTransactionThrift.json"));
    }

    /* TODO: Fix the bug where saving the pre-built transaction list and reading the pre-built transaction list do
    *        not pass the assertEquals() on line 73.
    @Test
    public void readAndSaveAddressBook_allInOrder_success() throws Exception {
        Path filePath = testFolder.resolve("TempAddressBook.json");
        Thrift original = TypicalTransactions.getTypicalAddressBook();
        JsonThriftStorage jsonAddressBookStorage = new JsonThriftStorage(filePath);

        // Save in new file and read back
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        ReadOnlyThrift readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new Thrift(readBack));

        // Modify data, overwrite exiting file, and read back
        original.addTransaction(TypicalTransactions.LAKSA);
        original.removeTransaction(TypicalTransactions.PENANG_LAKSA);
        jsonAddressBookStorage.saveAddressBook(original, filePath);
        readBack = jsonAddressBookStorage.readAddressBook(filePath).get();
        assertEquals(original, new Thrift(readBack));

        // Save and read without specifying file path
        original.addTransaction(TypicalTransactions.PENANG_LAKSA);
        jsonAddressBookStorage.saveAddressBook(original); // file path not specified
        readBack = jsonAddressBookStorage.readAddressBook().get(); // file path not specified
        assertEquals(original, new Thrift(readBack));

    }
     */

    @Test
    public void saveAddressBook_nullAddressBook_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(null, "SomeFile.json"));
    }

    /**
     * Saves {@code addressBook} at the specified {@code filePath}.
     */
    private void saveAddressBook(ReadOnlyThrift addressBook, String filePath) {
        try {
            new JsonThriftStorage(Paths.get(filePath))
                    .saveThrift(addressBook, addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveAddressBook_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveAddressBook(new Thrift(), null));
    }
}
