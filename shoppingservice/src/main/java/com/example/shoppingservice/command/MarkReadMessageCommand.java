package com.example.shoppingservice.command;


import org.axonframework.commandhandling.TargetAggregateIdentifier;

public class MarkReadMessageCommand {
 
    @TargetAggregateIdentifier
    private final String id;
 
    public MarkReadMessageCommand(String id) {
        this.id = id;
    }
 
    public String getId() {
        return id;
    }
}