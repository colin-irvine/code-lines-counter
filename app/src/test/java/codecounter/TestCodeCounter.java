package codecounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCodeCounter {
    private InMemoryLineReader lineReader;
    private CodeCounter codeCounter;

    @BeforeAll
    void setUp() {
        this.lineReader = new InMemoryLineReader();
        this.codeCounter = new IterativeCodeCounter(this.lineReader
                , "*/"
                , "/*"
                , "//");
    }

    @BeforeEach
    void clearLines() {
        this.lineReader.clearLines();
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenOnlyCode() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("import org.junit.api.Test;");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLineOfCodeWhenLineIsBlank() throws IOException {
        int expected = 0, actual;
        this.lineReader.addLine("");
        this.lineReader.addLine(" ");
        this.lineReader.addLine("     ");
        this.lineReader.addLine("\n");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLineOfCodeWhenOnlySingleLineComment() throws IOException {
        int expected = 0, actual;
        this.lineReader.addLine("//");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenBothCodeAndSingleLineComment() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("int x = 0; //");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeFollowedByMultilineComment() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("doSomething(); /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeSurroundedByMultilineComment() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("/* */ doSomething(); /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeIsAfterMultilineCommentWithMultilineCommentAfter() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("*/ doSomething(); /*");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountNotLineOfCodeWhenWhiteSpaceSurroundedByMultilineComment() throws IOException {
        int expected = 0, actual;
        this.lineReader.addLine("/* */ /* */ /* */ /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenMultilineCommentFollowedByCode() throws IOException {
        int expected = 1, actual;
        this.lineReader.addLine("/* */ int x = 1;");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenOnlyWhiteSpaces() throws IOException {
        int expected = 0, actual;

        this.lineReader.addLine("");
        this.lineReader.addLine(" ");
        this.lineReader.addLine("  ");
        this.lineReader.addLine("\n");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenOnlySingleComments() throws IOException {
        int expected = 0, actual;

        this.lineReader.addLine("//");
        this.lineReader.addLine(" //");
        this.lineReader.addLine(" /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenLineAreMultilineComment() throws IOException {
        int expected = 0, actual;

        this.lineReader.addLine("/******");
        this.lineReader.addLine(" Multiline comment");
        this.lineReader.addLine("*******/");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts2LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 2, actual;

        this.lineReader.addLine("/* start of mulitline comment");
        this.lineReader.addLine(" middle of multiline comment");
        this.lineReader.addLine("end of multiline comment */");
        this.lineReader.addLine("public class MyClass { ");
        this.lineReader.addLine("}");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts3LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 3, actual;

        this.lineReader.addLine("import something.from.somewhere");
        this.lineReader.addLine("/* start of mulitline comment");
        this.lineReader.addLine(" middle of multiline comment");
        this.lineReader.addLine("end of multiline comment */");
        this.lineReader.addLine("public class MyClass { ");
        this.lineReader.addLine("}");
        this.lineReader.addLine("/* start of mulitline comment");
        this.lineReader.addLine(" middle of multiline comment");
        this.lineReader.addLine("end of multiline comment */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts9LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 9, actual;

        this.lineReader.addLine("    private boolean isLineOfCode(String line) {");
        this.lineReader.addLine("");
        this.lineReader.addLine("// if contains only whitespace, not code");
        this.lineReader.addLine("        if (line.isBlank())");
        this.lineReader.addLine("            return false;");
        this.lineReader.addLine("");
        this.lineReader.addLine("        // if line is only whitespace and comments, not code");
        this.lineReader.addLine("        if (isLineCommentedOut(line))");
        this.lineReader.addLine("            return false;");
        this.lineReader.addLine("");
        this.lineReader.addLine("/* if line is inside multiline comment, not code");
        this.lineReader.addLine("previous line must set this to open or closed... */");
        this.lineReader.addLine("         if (isLinePartOfMultilineComment())");
        this.lineReader.addLine("            return false;");
        this.lineReader.addLine("        return true;");
        this.lineReader.addLine("");
        this.lineReader.addLine("    }");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }
}