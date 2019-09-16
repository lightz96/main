package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class RemarkTest {
    @Test
    public void equals() {
        Remark remark = new Remark("testing");
        assertFalse(remark.equals(null));
        assertTrue(remark.equals(new Remark("testing")));
        assertTrue(remark.equals(remark));
        assertFalse(remark.equals(new Remark("test")));
        assertFalse(remark.equals("testing"));
    }
}
