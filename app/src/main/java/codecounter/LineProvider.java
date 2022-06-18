package codecounter;

import java.io.IOException;

public interface LineProvider {
    String getLine() throws IOException;
}
