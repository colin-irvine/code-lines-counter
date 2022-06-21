package codecounter;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *  What is this guy responsible for?
 *  Getting a file/line provider from the user,
 *  returning the code count output
 */
public class Client {
    private CodeCounter codeCounter;
    private LineReader lineReader;
    private String message;

    public Client(String filePath) throws FileNotFoundException {
        setDefaultLineProvider(filePath);
        setDefaultCodeCounter(this.lineReader);
    };

    public Client(LineReader lineReader) throws Exception {
        setLineProvider(lineReader);
        setDefaultCodeCounter(this.lineReader);
    }

    public Client(String filePath, CodeCounter codeCounter) throws Exception {
        setDefaultLineProvider(filePath);
        setCodeCounter(codeCounter);
    }

    public Client(LineReader lineReader, CodeCounter codeCounter) throws Exception {
        setLineProvider(lineReader);
        setCodeCounter(codeCounter);
    }

    private void setDefaultLineProvider(String filePath) throws FileNotFoundException {
        this.lineReader = new FileLineReader(filePath);
    }

    private void setDefaultCodeCounter(LineReader lineReader) {
        this.codeCounter = new IterativeCodeCounter(lineReader
                                                    , "*/"
                                                    , "/*"
                                                    ,"//");
    }

    private void setLineProvider(LineReader lineReader) throws Exception {
        if (lineReader == null)
            throw new Exception("[Error]: Line Reader is null.");

        this.lineReader = lineReader;
    }

    private void setCodeCounter(CodeCounter codeCounter) throws Exception {
        if (codeCounter == null)
            throw new Exception("[Error]: Code Counter is null.");

        this.codeCounter = codeCounter;
    }

    public void doWork() throws IOException {
        int lineCount = this.codeCounter.countLines();
        this.message = String.format("Lines of code counted: %d ", lineCount);
        System.out.println(this.message);
    }
}
