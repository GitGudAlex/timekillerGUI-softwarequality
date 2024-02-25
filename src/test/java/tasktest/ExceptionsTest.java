package tasktest;

import de.hdm.bd.timekiller.customExceptions.DuplicatedNameException;
import de.hdm.bd.timekiller.customExceptions.IllegalNameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionsTest {

    @Test
    public void testDuplicatedNameException() {
        String expectedMessage = "Whoopsie that's a duplicate.";

        DuplicatedNameException exception = assertThrows(DuplicatedNameException.class, () -> {
            throw new DuplicatedNameException(
                    "Task with the same name already exists.");
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
