package org.example.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PRTestCaseService {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/lalith-chennupati/ActionInAction/pulls/1/files";
    private static final String AI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String GITHUB_TOKEN = System.getenv("GIT_TOKEN");
    private static final String AI_API_KEY = System.getenv("AI_API_KEY");

    public List<String> fetchPRChanges(String owner, String repo, int prNumber) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // Fixed here

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GITHUB_API_URL,
                HttpMethod.GET,
                request,
                String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode files = objectMapper.readTree(response.getBody());
                List<String> javaFileContents = new ArrayList<>();

                for (JsonNode file : files) {
                    String filename = file.get("filename").asText();
                    if (filename.contains("customerinfo/") && filename.endsWith(".java") && !filename.endsWith("Test.java")) {
                        String contentsUrl = file.get("contents_url").asText();
                        String decodedUrl = URLDecoder.decode(contentsUrl, StandardCharsets.UTF_8);
                        System.out.println(decodedUrl);
                        String fileContent = fetchFileContent(decodedUrl);
                        javaFileContents.add(fileContent);
                    }
                }
                return javaFileContents;
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse response: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("Failed to fetch PR files: " + response.getStatusCode());
        }
    }

    private String fetchFileContent(String contentsUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    contentsUrl,
                    HttpMethod.GET,
                    request,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode fileNode = objectMapper.readTree(response.getBody());
                    String base64Content = fileNode.get("content").asText();
                    // Decode Base64 content
                    return new String(java.util.Base64.getMimeDecoder().decode(base64Content), StandardCharsets.UTF_8);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode file content: " + e.getMessage());
                }
            } else {
                throw new RuntimeException("Failed to fetch file content: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }



    public String generateTestCases(String codeSnippet) {
        RestTemplate restTemplate = new RestTemplate();

        String userPrompt = "Generate JUnit test cases with 100% code coverage and 100% condition coverage for the following Java class. Do not include any explanations or additional information or any additional message in content filed:\n" + codeSnippet;
        try {
            // Use ObjectMapper to construct the JSON payload
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("model", "gpt-4");
            payload.put("max_tokens", 500);
            payload.put("temperature", 0.7);

            ObjectNode systemMessage = objectMapper.createObjectNode();
            systemMessage.put("role", "system");
            systemMessage.put("content", "You are a helpful assistant.");

            ObjectNode userMessage = objectMapper.createObjectNode();
            userMessage.put("role", "user");
            userMessage.put("content", userPrompt);

            payload.putArray("messages").add(systemMessage).add(userMessage);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + AI_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers);


            ResponseEntity<String> response = restTemplate.postForEntity(AI_API_URL, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                try {
                    ObjectMapper objectMapper1 = new ObjectMapper();
                    JsonNode fileNode = objectMapper1.readTree(response.getBody());
                    String testResponse = fileNode.toPrettyString();
                    System.out.println("Response JSON: " + fileNode.toPrettyString());
                    System.out.println("Response JSON With nodes: " + fileNode.get("choices").get(0).get("message").get("content").asText());
                    return fileNode.get("choices").get(0).get("message").get("content").asText();
                    //return new String(java.util.Base64.getDecoder().decode(fileNode.get("content").asText()));
                } catch (Exception e) {
                    throw new RuntimeException("Failed to decode file content: " + e.getMessage());
                }
            } else {
                throw new RuntimeException("Failed to generate test cases: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
    public void generateTestClassesForEachFile(String owner, String repo, int prNumber) {
        List<String> javaFileContents = fetchPRChanges(owner, repo, prNumber);

        javaFileContents.forEach(fileContent -> {
            try {
                // Generate test cases for each class
                String testCases = generateTestCases(fileContent);
                PRCommitService.addTestCasesToPR(testCases, "main");
                System.out.println("Generated Test Cases for Class:\n" + testCases);
            } catch (Exception e) {
                System.err.println("Failed to generate test cases for a class: " + e.getMessage());
            }
        });
    }
    /*public static void main(String[] args) {
        try {
            PRTestCaseService service = new PRTestCaseService();
            service.generateTestClassesForEachFile("owner", "repo", 1);
        }catch(Exception e){
            System.err.println("Error: " + e);
        }
         System.out.println("COMPLETED");
    }*/
}

