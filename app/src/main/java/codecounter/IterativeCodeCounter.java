package codecounter;

import java.io.IOException;

public class IterativeCodeCounter implements CodeCounter {
    private LineProvider lineProvider;
    private boolean multilineCommentTagOpen;
    // hmmmmm, maybe have a delegate for this?
    // responsibility would be to know/determine code language and assign comment tags
    // commenter = init("determined code language") then commenter.getClosingTag(), commenter.getOpeningTag()
    // be careful that you are not over engineering! this could simply be a map of language keys to a map of tags
    private String closingCommentTag;
    private String openingCommentTag;
    private String singleCommentTag;

    public IterativeCodeCounter(LineProvider lineProvider
            , String closingCommentTag
            , String openingCommentTag
            , String singleCommentTag) {
        this.lineProvider = lineProvider;
        this.multilineCommentTagOpen = false;
        this.closingCommentTag = closingCommentTag;
        this.openingCommentTag = openingCommentTag;
        this.singleCommentTag = singleCommentTag;
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
        // removing state after function finishes
        this.multilineCommentTagOpen = false;

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

        if (cleanedLine.startsWith(this.singleCommentTag))
            return true;

        if ((cleanedLine.startsWith(this.openingCommentTag)
                && cleanedLine.endsWith(this.closingCommentTag)))
            return true;

        return cleanedLine.startsWith(this.openingCommentTag)
                && false == line.contains("*/");
    }

    private boolean isLinePartOfMultilineComment() {
        return this.multilineCommentTagOpen;
    }

    private void setMultilineCommentTagOpen(String line) {
        int closingTagIndex, openingTagIndex;

        closingTagIndex = line.indexOf(this.closingCommentTag);
        openingTagIndex = line.indexOf(this.openingCommentTag);

        if (closingTagIndex == -1 && openingTagIndex == -1)
            return;

        if (closingTagIndex > openingTagIndex)
            this.multilineCommentTagOpen = false;

        if (openingTagIndex > closingTagIndex)
            this.multilineCommentTagOpen = true;
    }
}
