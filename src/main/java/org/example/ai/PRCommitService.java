package org.example.ai;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PRCommitService {

    public static void addTestCasesToPR(String testClassContent, String branchName) throws Exception {
        try {
            // Save test cases to a file
            String packageName = null;
            for (String line : testClassContent.split("\n")) {
                if (line.startsWith("package ")) {
                    packageName = line.replace("package ", "").replace(";", "").trim();
                    break;
                }
            }

            // Determine the output directory based on the package name
            String outputDir = "src/test/java";
            if (packageName != null) {
                outputDir += "/" + packageName.replace(".", "/");
            }

            // Ensure the output directory exists
            File directory = new File(outputDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Extract class name from the test class content
            String className = testClassContent.split("class ")[1].split(" ")[0].trim();

            // Create the test class file
            File testFile = new File(directory, className + ".java");
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write(testClassContent);
            }
            // Commit and push changes
            ProcessBuilder builder = new ProcessBuilder(
                    "bash", "-c",
                    "git checkout " + branchName + " && " +
                            "git add " + outputDir + " && " +
                            "git commit -m 'Add generated test cases' && " +
                            "git push origin " + branchName
            );
            builder.directory(new File(System.getProperty("user.dir")));
            Process process = builder.start();
            process.waitFor();
        }catch (IOException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Failed to write test class: " + e.getMessage());
        }
    }


}