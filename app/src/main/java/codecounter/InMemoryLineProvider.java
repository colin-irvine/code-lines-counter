package codecounter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InMemoryLineProvider implements LineProvider {
    private List<String> lines;
    private Iterator<String> lineIterator;

    InMemoryLineProvider() {
        this.lines = new ArrayList<>();
        this.lineIterator = this.lines.iterator();
    }

    @Override
    public String getLine() {
        if (lineIterator.hasNext() == false)
            return null;

        return lineIterator.next();
    }

    public void addLine(String codeLine) {
        this.lines.add(codeLine);
        this.lineIterator = this.lines.iterator();
    }

    public void clearLines() {
        this.lines.clear();
    }
}
