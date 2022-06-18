package codecounter;

public class IterativeCodeCounter implements CodeCounter {
    private LineProvider lineProvider;
    private boolean multilineCommentTagOpen;

    public IterativeCodeCounter(LineProvider lineProvider) {
        this.lineProvider = lineProvider;
        this.multilineCommentTagOpen = false;
    }
    @Override
    public int countLines() {
        int lineCount = 0;
        String currentLine = "";

        this.multilineCommentTagOpen = false;

        while ((currentLine = this.lineProvider.getLine()) != null) {
            if (isLineOfCode(currentLine)){
                lineCount++;
            }

            setMultilineCommentTagOpen(currentLine);

            // regardless of line of code or not, we need to check for opening and closing tags?
            // what are we checking then?
            // a) " operate() /* " - opens multiline and is code
            // b) " comment */ anotherOperation() " - closes multiline and is code
            // c) " /* comment " - opens multiline and is not code
            // d) " comment */ " - closes multiline and is not code
            // e) " */ "aDifferentOperation() /* " - is code and both closes multiline and opens multiline
            // what does this tell us?
            // we have at least 5 cases and order matters!
            // if open tag on line is followed by close tag on line then closed
            // if open tag on line is not followed by close tag on line then opened
            // if close tag on line is followed by open tag on line then opened
            // if close tag on line and is not followed by open then closed
            // simplification?:
            // if last tag on line is open then opened
            // if last tage on line closed then closed
            // we must be able to find tags and check their order
            //
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
