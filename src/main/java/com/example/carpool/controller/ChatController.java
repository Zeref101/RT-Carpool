package com.example.carpool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import com.example.carpool.model.ChatMessage;
import com.example.carpool.service.DatabaseConnectionService;
import com.example.carpool.service.ChatMessageService;
import java.util.UUID;

@Controller
public class ChatController {

    @Autowired
    private DatabaseConnectionService databaseConnectionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatMessageService chatMessageService) {
        this.messagingTemplate = messagingTemplate;
    }

    // Endpoint to receive messages from clients
    @MessageMapping("/chat.sendMessage")
    public ChatMessage sendMessage(ChatMessage message) {
        if (databaseConnectionService.isDatabaseConnected()) {
            UUID messageId = UUID.randomUUID();
            System.out.println(messageId);
            String sql = "INSERT INTO messages (messageid, content, senderid, groupid, timestamp) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, messageId, message.getContent(), message.getSender(),
                    message.getGroupId(),
                    message.getTimestamp());

            ChatMessage savedMessage = new ChatMessage();
            savedMessage.setContent(message.getContent());
            savedMessage.setSender(message.getSender());
            savedMessage.setGroupId(message.getGroupId());
            savedMessage.setTimestamp(message.getTimestamp());

            // Broadcast the message to the group (use the correct destination)
            String destination = "/topic/group." + message.getGroupId();
            messagingTemplate.convertAndSend(destination, savedMessage);

            // Return the saved message to the client
            return savedMessage;
        } else {
            throw new RuntimeException("Database connection is not established.");
        }
    }
}
