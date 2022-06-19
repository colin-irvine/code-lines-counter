package codecounter;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileLineProvider implements LineProvider {
    private FileReader fileReader;
    private BufferedReader bufferedReader;
    public FileLineProvider(String filePath) throws FileNotFoundException {
        this.fileReader = new FileReader(filePath);
        this.bufferedReader = new BufferedReader( this.fileReader);
    }

    @Override
    public String getLine() throws IOException {
        return this.bufferedReader.readLine();
    }
}
