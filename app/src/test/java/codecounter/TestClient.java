package codecounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
/*
- Service is responsible for handling requests and returning appropriate responses/
- Client is responsible for creating requests and handling responses
- Test bad requests cause bad responses to be handled
- So, what are my tests? CLI outputs are correct?
*/
public class TestClient {

    @Test
    void testClientThrowsExceptionWhenFileDoesntExist() throws FileNotFoundException {
        assertThrows(FileNotFoundException.class, () -> new Client("no file"));
    }

    @Test
    void testClientThrowsExceptionWhenLineProviderIsNull() throws Exception {
        LineProvider lineProvider = null;

        Exception exc = assertThrows(Exception.class, () -> new Client(lineProvider));
        assertEquals("[Error]: Line Provider is null.", exc.getMessage());
    }

    @Test
    void testClientThrowsExceptionWhenCodeCounterIsNull() throws Exception {
        String userDirectory = System.getProperty("user.dir");
        String filePath = userDirectory + "/src/test/resources/TestCodeClass.java";
        CodeCounter codeCounter = null;
        // hmmmm... So what's it going to be then, eh?
        // if System.getProperty("user.dir"); returns where the JAR is invoked from then we might be okay?
        // Tests: When given a relative path, no exception is thrown
        // When given an absolute path, no exception is thrown
        Exception exc = assertThrows(Exception.class, () -> new Client(filePath, codeCounter));
        assertEquals("[Error]: Code Counter is null.", exc.getMessage());
    }

    @Test
    void testClientThrowsExceptionWhenLineProviderIsGoodAndCodeCounterIsNull() throws Exception {
        LineProvider lineProvider = new InMemoryLineProvider();
        CodeCounter codeCounter = null;

        Exception exc = assertThrows(Exception.class, () -> new Client(lineProvider, codeCounter));
        assertEquals("[Error]: Code Counter is null.", exc.getMessage());
    }

    @Test
    void testGetMessagePromptsUserOfMissingArguments() throws FileNotFoundException {

    }
}
