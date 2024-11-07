package com.example.carpool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
// import org.springframework.messaging.handler.annotation.SendTo;
import com.example.carpool.model.ChatMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    ChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // ? Endpoint to receive messages from clients
    @MessageMapping("/chat.sendMessage")
    // @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        // Broadcast the message to the group
        // System.out.println(message.getGroupId());
        String destination = "/topic/group." + message.getGroupId();
        messagingTemplate.convertAndSend(destination, message);
        return message;
    }
}
