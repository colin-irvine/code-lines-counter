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
                && false == line.contains("*/");
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

        // very interesting, for "*/ doSomething(); /*"
        // this code produces "*/ doSomething(); doSomething(); doSomething(); /*"
        // is this due to the tags needed to follow each other?
        // order matters yo!
        // while
        openCommentIndex = line.indexOf("/*");
        closedCommentIndex = line.indexOf("*/");

        while ( (closedCommentIndex > openCommentIndex) &&(openCommentIndex > -1 && closedCommentIndex > -1)) {
            commentsRemoved = extractBeforeComment(commentsRemoved, openCommentIndex)
                    + extractAfterComment(commentsRemoved, closedCommentIndex);
            openCommentIndex = commentsRemoved.indexOf("/*");
            closedCommentIndex = commentsRemoved.indexOf("*/");
        }

        return commentsRemoved;
    }

    private String extractBeforeComment(String line, int openCommentIndex) {
        return line.substring(0, openCommentIndex);
    }

    private String extractAfterComment(String line, int closedCommentIndex) {
        return line.substring(closedCommentIndex + 2);
    }
}
