package codecounter;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
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
    void testClientThrowsExceptionWhenLineReaderIsNull() throws Exception {
        LineReader lineReader = null;

        Exception exc = assertThrows(Exception.class, () -> new Client(lineReader));
        assertEquals("[Error]: Line Reader is null.", exc.getMessage());
    }

    @Test
    void testClientThrowsExceptionWhenCodeCounterIsNull() throws Exception {
        String userDirectory = System.getProperty("user.dir");
        String filePath = userDirectory + "/src/test/resources/TestCodeClass.java";
        CodeCounter codeCounter = null;

        Exception exc = assertThrows(Exception.class, () -> new Client(filePath, codeCounter));
        assertEquals("[Error]: Code Counter is null.", exc.getMessage());
    }

    @Test
    void testClientThrowsExceptionWhenLineProviderIsGoodAndCodeCounterIsNull() throws Exception {
        LineReader lineReader = new InMemoryLineReader();
        CodeCounter codeCounter = null;

        Exception exc = assertThrows(Exception.class, () -> new Client(lineReader, codeCounter));
        assertEquals("[Error]: Code Counter is null.", exc.getMessage());
    }

    @Test
    void testGetMessagePromptsUserOfMissingArguments() throws FileNotFoundException {

    }
}
