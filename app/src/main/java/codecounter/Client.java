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
    private LineProvider lineProvider;
    private String message;

    public Client(String filePath) throws FileNotFoundException {
        setDefaultLineProvider(filePath);
        setDefaultCodeCounter(this.lineProvider);
    };

    public Client(LineProvider lineProvider) throws Exception {
        setLineProvider(lineProvider);
        setDefaultCodeCounter(this.lineProvider);
    }

    public Client(String filePath, CodeCounter codeCounter) throws Exception {
        setDefaultLineProvider(filePath);
        setCodeCounter(codeCounter);
    }

    public Client(LineProvider lineProvider, CodeCounter codeCounter) throws Exception {
        setLineProvider(lineProvider);
        setCodeCounter(codeCounter);
    }

    private void setDefaultLineProvider(String filePath) throws FileNotFoundException {
        this.lineProvider = new FileLineProvider(filePath);
    }

    private void setDefaultCodeCounter(LineProvider lineProvider) {
        this.codeCounter = new IterativeCodeCounter(lineProvider
                                                    , "*/"
                                                    , "/*"
                                                    ,"//");
    }

    private void setLineProvider(LineProvider lineProvider) throws Exception {
        if (lineProvider == null)
            throw new Exception("[Error]: Line Provicer is null.");

        this.lineProvider = lineProvider;
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
