package codecounter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestCodeCounter {
    private InMemoryLineProvider lineProvider;
    private CodeCounter codeCounter;

    @BeforeAll
    void setUp() {
        this.lineProvider = new InMemoryLineProvider();
        this.codeCounter = new IterativeCodeCounter(this.lineProvider
                , "*/"
                , "/*"
                , "//");
    }

    @BeforeEach
    void clearLines() {
        this.lineProvider.clearLines();
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenOnlyCode() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("import org.junit.api.Test;");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLineOfCodeWhenLineIsBlank() throws IOException {
        int expected = 0, actual;
        this.lineProvider.addLine("");
        this.lineProvider.addLine(" ");
        this.lineProvider.addLine("     ");
        this.lineProvider.addLine("\n");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLineOfCodeWhenOnlySingleLineComment() throws IOException {
        int expected = 0, actual;
        this.lineProvider.addLine("//");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenBothCodeAndSingleLineComment() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("int x = 0; //");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeFollowedByMultilineComment() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("doSomething(); /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeSurroundedByMultilineComment() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("/* */ doSomething(); /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenCodeIsAfterMultilineCommentWithMultilineCommentAfter() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("*/ doSomething(); /*");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountNotLineOfCodeWhenWhiteSpaceSurroundedByMultilineComment() throws IOException {
        int expected = 0, actual;
        this.lineProvider.addLine("/* */ /* */ /* */ /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldCountLineOfCodeWhenMultilineCommentFollowedByCode() throws IOException {
        int expected = 1, actual;
        this.lineProvider.addLine("/* */ int x = 1;");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenOnlyWhiteSpaces() throws IOException {
        int expected = 0, actual;

        this.lineProvider.addLine("");
        this.lineProvider.addLine(" ");
        this.lineProvider.addLine("  ");
        this.lineProvider.addLine("\n");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenOnlySingleComments() throws IOException {
        int expected = 0, actual;

        this.lineProvider.addLine("//");
        this.lineProvider.addLine(" //");
        this.lineProvider.addLine(" /* */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountLinesShouldNotCountLinesOfCodeWhenLineAreMultilineComment() throws IOException {
        int expected = 0, actual;

        this.lineProvider.addLine("/******");
        this.lineProvider.addLine(" Multiline comment");
        this.lineProvider.addLine("*******/");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts2LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 2, actual;

        this.lineProvider.addLine("/* start of mulitline comment");
        this.lineProvider.addLine(" middle of multiline comment");
        this.lineProvider.addLine("end of multiline comment */");
        this.lineProvider.addLine("public class MyClass { ");
        this.lineProvider.addLine("}");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts3LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 3, actual;

        this.lineProvider.addLine("import something.from.somewhere");
        this.lineProvider.addLine("/* start of mulitline comment");
        this.lineProvider.addLine(" middle of multiline comment");
        this.lineProvider.addLine("end of multiline comment */");
        this.lineProvider.addLine("public class MyClass { ");
        this.lineProvider.addLine("}");
        this.lineProvider.addLine("/* start of mulitline comment");
        this.lineProvider.addLine(" middle of multiline comment");
        this.lineProvider.addLine("end of multiline comment */");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }

    @Test
    void testCountCodeLinesCounts9LinesWhenLinesAreCodeAndMultilineComments() throws IOException {
        int expected = 9, actual;

        this.lineProvider.addLine("    private boolean isLineOfCode(String line) {");
        this.lineProvider.addLine("");
        this.lineProvider.addLine("// if contains only whitespace, not code");
        this.lineProvider.addLine("        if (line.isBlank())");
        this.lineProvider.addLine("            return false;");
        this.lineProvider.addLine("");
        this.lineProvider.addLine("        // if line is only whitespace and comments, not code");
        this.lineProvider.addLine("        if (isLineCommentedOut(line))");
        this.lineProvider.addLine("            return false;");
        this.lineProvider.addLine("");
        this.lineProvider.addLine("/* if line is inside multiline comment, not code");
        this.lineProvider.addLine("previous line must set this to open or closed... */");
        this.lineProvider.addLine("         if (isLinePartOfMultilineComment())");
        this.lineProvider.addLine("            return false;");
        this.lineProvider.addLine("        return true;");
        this.lineProvider.addLine("");
        this.lineProvider.addLine("    }");

        actual = this.codeCounter.countLines();
        assertEquals(expected, actual);
    }
}