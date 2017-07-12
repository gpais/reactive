package com.example.shoppingservice.handler;

import org.axonframework.eventhandling.EventHandler;

import com.example.shoppingservice.event.MessageCreatedEvent;
import com.example.shoppingservice.event.MessageReadEvent;


public class MessagesEventHandler {

    @EventHandler
    public void handle(MessageCreatedEvent event) {
        System.out.println("Message received: " + event.getText() + " (" + event.getId() + ")");
    }

    @EventHandler
    public void handle(MessageReadEvent event) {
        System.out.println("Message read: " + event.getId());
    }
}