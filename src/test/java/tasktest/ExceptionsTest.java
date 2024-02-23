package tasktest;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ExceptionsTest {

    @Test
    public void testDuplicatedNameException() {
        String expectedMessage = "Whoopsie that's a duplicate.";

        DuplicatedNameException exception = assertThrows(DuplicatedNameException.class, () -> {
            throw new DuplicatedNameException();
        });
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testIllegalNameException() {

        assertThrows(IllegalNameException.class, () -> {
            throw new IllegalNameException();
        });
    }
}
