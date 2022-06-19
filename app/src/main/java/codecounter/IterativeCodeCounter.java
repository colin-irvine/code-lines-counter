package codecounter;

import java.io.IOException;

public class IterativeCodeCounter implements CodeCounter {
    private LineProvider lineProvider;
    private boolean multilineCommentTagOpen;

    public IterativeCodeCounter(LineProvider lineProvider) {
        this.lineProvider = lineProvider;
        this.multilineCommentTagOpen = false;
    }
    @Override
    public int countLines() throws IOException {
        int lineCount = 0;
        String currentLine = "";

        this.multilineCommentTagOpen = false;

        while ((currentLine = this.lineProvider.getLine()) != null) {
            if (isLineOfCode(currentLine)){
                lineCount++;
            }

            setMultilineCommentTagOpen(currentLine);
        }

        return lineCount;
    }

    private boolean isLineOfCode(String line) {
        // if contains only whitespace, not code
        if (line.isBlank())
            return false;

        // if line is only whitespace and comments, not code
        if (isLineCommentedOut(line))
            return false;

        // if line is inside multiline comment, not code
        // previous line must set this to open or closed...
        if (isLinePartOfMultilineComment())
            return false;

        return true;

    }

    private boolean isLineCommentedOut(String line) {
        return isLineSingleComment(line);
    }

    private boolean isLineSingleComment(String line) {
        String cleanedLine = line.strip();
        if (cleanedLine.startsWith("//"))
            return true;

        if ((cleanedLine.startsWith("/*") && cleanedLine.endsWith("*/")))
            return true;

        return cleanedLine.startsWith("/*") && false == line.contains("*/");
    }

    private boolean isLinePartOfMultilineComment() {
        return this.multilineCommentTagOpen;
    }

    private void setMultilineCommentTagOpen(String line) {
        int closingTagIndex, openingTagIndex;
        closingTagIndex = line.indexOf("*/");
        openingTagIndex = line.indexOf("/*");

        if (closingTagIndex == -1 && openingTagIndex == -1)
            return;

        if (closingTagIndex > openingTagIndex)
            this.multilineCommentTagOpen = false;

        if (openingTagIndex > closingTagIndex)
            this.multilineCommentTagOpen = true;
    }
}
