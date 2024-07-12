package com.example.final_project_java.userapi.api;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

//    final DefaultMessageService messageService;
//
//    @Value("${message.api-key}")
//    private String API_KEY;
//
//    @Value("${message.secret-key}")
//    private String SECRET_KEY;
//
//    public MessageController() {
//        this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, SECRET_KEY, "https://api.coolsms.co.kr");
//    }
//
//    @PostMapping("/send-one")
//    public SingleMessageSentResponse sendOne() {
//        Message message = new Message();
//        message.setFrom("01033139971");
//        message.setTo("01033139971");
//        message.setText("본인인증을 위한 메세지 API 확인 중!");
//
//        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//        System.out.println(response);
//
//        return response;
//    }

}