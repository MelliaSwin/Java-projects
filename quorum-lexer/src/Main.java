import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // <Folder> is the only argument
        if (args.length != 1) {
            System.err.println("Usage: java -jar Lexer.jar <Folder>");
            System.exit(1);
        }
        // Grab name of folder from argument
        String folder = args[0];
        // convert into File object
        File dir = new File(folder);
        // If folder doesn't exist, terminate w/ error message
        if (!dir.exists()) {
            System.err.println("Could not locate folder: " + folder);
            System.exit(1);
        }
        // Grab all files
        File[] files = dir.listFiles(File::isFile);
        if (files == null) {
            System.err.println("Could not list files: " + dir);
            System.exit(1);
        }

        // Sort files by name
        Arrays.sort(files, (a, b) -> a.getName().compareTo(b.getName()));

        // Go through each file in folder
        for (File f : files) {
            // Store its text in string:
            StringBuilder text = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    text.append(line).append("\n");
                }
                // Error Handling:
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + f.getName());
                continue; // Read next file
            } catch (IOException e) {
                System.err.println("Something went wrong while reading file: " + f.getName());
                continue; // Read next file
            }
            // If file is empty, then skip over it
            if(text.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println("Input: " + f.getName());
            System.out.println("Output:");

            // Call our lexer
            Lexer lexer = new Lexer(text.toString());
            var tokens = lexer.tokenize();

            if(tokens.isEmpty()) {
                continue;
            }
            // Output the tokens
            for (Token i : tokens) {
                System.out.println(i);
            }
            // New line to separate each file
            System.out.println();
        }
    }
}