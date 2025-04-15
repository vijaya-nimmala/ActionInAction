package org.example.ai;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
public class PRTestCaseService {

    private static final String GITHUB_API_URL = "https://api.github.com/repos/{owner}/{repo}/pulls/{prNumber}/files";
    private static final String AI_API_URL = "https://api.openai.com/v1/completions";
    private static final String GITHUB_TOKEN = "your-github-token";
    private static final String AI_API_KEY = "your-ai-api-key";

    public String fetchPRChanges(String owner, String repo, int prNumber) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + GITHUB_TOKEN);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON)); // Fixed here

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            GITHUB_API_URL.replace("{owner}", owner)
                          .replace("{repo}", repo)
                          .replace("{prNumber}", String.valueOf(prNumber)),
            HttpMethod.GET,
            request,
            String.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch PR changes: " + response.getStatusCode());
        }
    }

    public String generateTestCases(String codeSnippet) {
        RestTemplate restTemplate = new RestTemplate();

        String payload = """
            {
                "model": "gpt-4",
                "prompt": "Generate JUnit test cases with 100% code coverage and 100% condition coverage for the following code:\n" + codeSnippet,
                "max_tokens": 8014
            }
        """;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + AI_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(AI_API_URL, request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to generate test cases: " + response.getStatusCode());
        }
    }
}

