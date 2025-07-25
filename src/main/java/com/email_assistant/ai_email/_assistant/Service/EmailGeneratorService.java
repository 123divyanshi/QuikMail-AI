package com.email_assistant.ai_email._assistant.Service;

import com.email_assistant.ai_email._assistant.Entity.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {
    @Value("${gemini.api.url}")
    private  String geminiApiUrl;
    @Value("${gemini.api.key}")
    private  String geminiApiKey;

    private final  WebClient webClient;

    public EmailGeneratorService(WebClient.Builder webClientBuilder){

        this.webClient = webClientBuilder.build();
    }


    public  String generateEmailReply(EmailRequest emailRequest) {
        String prompt=generatePrompt(emailRequest);

        Map<String,Object> requestBody=Map.of("contents",new Object[]{
                Map.of("parts",new Object[]{
                        Map.of("text", prompt)})
                }

                );

        String response = webClient.post()
                .uri(geminiApiUrl + "?key=" + geminiApiKey)

                .header("Content-Type","application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        // Extract Response and Return
        return extractResponseContent(response);





        }

    private String extractResponseContent(String response) {
        try{
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();

        }
        catch(Exception e){
            System.out.println("some error has encountered"+e.getMessage());
            return "Error: Could not extract response.";
        }

    }

    private String generatePrompt(EmailRequest emailRequest) {

        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a professional email reply for the following email content. Please don't generate a subject line ");
        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
            prompt.append(" use a ").append(emailRequest.getTone()).append(" tone.");


        }
        prompt.append("\n Original email: \n").append(emailRequest.getEmailContent());
        return prompt.toString();

}
}
