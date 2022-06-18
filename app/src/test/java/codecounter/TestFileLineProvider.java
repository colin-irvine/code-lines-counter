package codecounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

public class TestFileLineProvider {

    @Test
    // is this test redundant?
    void testFileLineProviderThrowsErrorOnConstructionWhenFileDoesNotExist() {
        Exception exc = assertThrows(FileNotFoundException.class, () ->
        new FileLineProvider("file path")
        );
    }
}
