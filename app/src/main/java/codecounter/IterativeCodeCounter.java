package codecounter;

import java.io.IOException;

public class IterativeCodeCounter implements CodeCounter {
    private LineReader lineReader;
    private boolean multilineCommentTagOpen;
    private String closingCommentTag;
    private String openingCommentTag;
    private String singleCommentTag;

    public IterativeCodeCounter(LineReader lineReader
            , String closingCommentTag
            , String openingCommentTag
            , String singleCommentTag) {
        this.lineReader = lineReader;
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

        while ((currentLine = this.lineReader.getLine()) != null) {
            if (isLineOfCode(currentLine)){
                lineCount++;
            }

            setMultilineCommentTagOpen(currentLine);
        }
        // removing state after function finishes
        // TODO: Consider removing this state completely
        this.multilineCommentTagOpen = false;

        return lineCount;
    }

    private boolean isLineOfCode(String line) {
        String cleanedLine = removeMultilineComments(line);
        // if contains only whitespace, not code
        if (cleanedLine.isBlank())
            return false;

        // if line is only whitespace and comments, not code
        if (isLineCommentedOut(cleanedLine))
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
                && false == line.contains(this.closingCommentTag);
    }

    private boolean isLinePartOfMultilineComment() {
        return this.multilineCommentTagOpen;
    }

    private void setMultilineCommentTagOpen(String line) {
        int closingTagIndex, openingTagIndex;

        closingTagIndex = line.lastIndexOf(this.closingCommentTag);
        openingTagIndex = line.lastIndexOf(this.openingCommentTag);

        if (closingTagIndex == -1 && openingTagIndex == -1)
            return;

        if (closingTagIndex > openingTagIndex)
            this.multilineCommentTagOpen = false;

        if (openingTagIndex > closingTagIndex)
            this.multilineCommentTagOpen = true;
    }

    private String removeMultilineComments(String line) {
        String commentsRemoved = line;
        Integer openCommentIndex, closedCommentIndex;

        openCommentIndex = line.indexOf(this.openingCommentTag);
        closedCommentIndex = line.indexOf(this.closingCommentTag);

        while (hasCommentsToRemove(openCommentIndex, closedCommentIndex)) {
            commentsRemoved = extractBeforeComment(commentsRemoved, openCommentIndex)
                    + extractAfterComment(commentsRemoved, closedCommentIndex);
            openCommentIndex = commentsRemoved.indexOf(this.openingCommentTag);
            closedCommentIndex = commentsRemoved.indexOf(this.closingCommentTag);
        }

        return commentsRemoved;
    }

    private String extractBeforeComment(String line, int openCommentIndex) {
        return line.substring(0, openCommentIndex);
    }

    private String extractAfterComment(String line, int closedCommentIndex) {
        return line.substring(closedCommentIndex + 2);
    }

    private boolean hasCommentsToRemove(Integer openingCommentIndex, Integer closingCommentIndex) {
        return hasClosingTagForOpeningTag(openingCommentIndex, closingCommentIndex) &&
                hasBothOpeningAndClosingTags(openingCommentIndex, closingCommentIndex);
    }

    private boolean hasClosingTagForOpeningTag(Integer openingCommentIndex, Integer closingCommentIndex) {
        return closingCommentIndex > openingCommentIndex;
    }

    private boolean hasBothOpeningAndClosingTags(Integer openingCommentIndex, Integer closingCommentIndex) {
        return openingCommentIndex > -1 && closingCommentIndex > -1;
    }
}
