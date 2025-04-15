package org.example.ai;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PRCommitService {

    public void addTestCasesToPR(String testCases, String branchName) throws Exception {
        // Save test cases to a file
        String filePath = "src/test/java/org/example/GeneratedTestCases.java";
        File file = new File(filePath);
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(testCases);
        }

        // Commit and push changes
        ProcessBuilder builder = new ProcessBuilder(
            "bash", "-c",
            "git checkout " + branchName + " && " +
            "git add " + filePath + " && " +
            "git commit -m 'Add generated test cases' && " +
            "git push origin " + branchName
        );
        builder.directory(new File(System.getProperty("user.dir")));
        Process process = builder.start();
        process.waitFor();
    }
}