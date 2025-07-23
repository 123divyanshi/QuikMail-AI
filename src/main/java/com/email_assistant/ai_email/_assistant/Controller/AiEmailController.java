package com.email_assistant.ai_email._assistant.Controller;

import com.email_assistant.ai_email._assistant.Service.EmailGeneratorService;
import com.email_assistant.ai_email._assistant.Entity.EmailRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@AllArgsConstructor
@RestController
@RequestMapping("/api/email")
@CrossOrigin("*")
public class AiEmailController {
    private final EmailGeneratorService emailGeneratorService;
    @PostMapping("/reply")
    public ResponseEntity<String> getReply(@RequestBody EmailRequest emailRequest){
        String response=emailGeneratorService.generateEmailReply(emailRequest);
        return ResponseEntity.ok(response);
    }
//    public AiEmailController(EmailGeneratorService emailGeneratorService) {
//        this.emailGeneratorService = emailGeneratorService;
//    }


}
