package codecounter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir") + "/app/src/test/resources/TestCodeClass.java"; // parse file path from args
        try {
            Client codeCounterClient = new Client(filePath);
            codeCounterClient.doWork();
        }
        catch (FileNotFoundException exc) {
            System.out.println("[!] File not found" );
            System.out.println(exc.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
