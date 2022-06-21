package codecounter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLineReader implements LineReader {
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    public FileLineReader(String filePath) throws FileNotFoundException {
        this.fileReader = new FileReader(filePath);
        this.bufferedReader = new BufferedReader( this.fileReader);
    }

    @Override
    public String getLine() throws IOException {
        return this.bufferedReader.readLine();
    }
}
