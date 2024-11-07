package com.example.carpool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import com.example.carpool.model.ChatMessage;

@Controller
public class ChatController {

    @MessageMapping("/message")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
