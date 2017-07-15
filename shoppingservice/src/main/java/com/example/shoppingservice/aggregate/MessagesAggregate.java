package com.example.shoppingservice.aggregate;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventhandling.EventHandler;

import com.example.shoppingservice.command.CreateMessageCommand;
import com.example.shoppingservice.command.MarkReadMessageCommand;
import com.example.shoppingservice.event.MessageCreatedEvent;
import com.example.shoppingservice.event.MessageReadEvent;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;


//@Entity
public class MessagesAggregate {

    @AggregateIdentifier
//    @Id
    private String id;
    
    private String status;

    public MessagesAggregate() {
    }

    @CommandHandler
    public MessagesAggregate(CreateMessageCommand command) {
        apply(new MessageCreatedEvent(command.getId(), command.getText()));
    }

    @EventHandler
    public void on(MessageCreatedEvent event) {
        this.id = event.getId();
    }

    @CommandHandler
    public void markRead(MarkReadMessageCommand command) {
        apply(new MessageReadEvent(id));
    }
    
    
    @EventHandler
    public void on(MessageReadEvent event) {
        this.status = "send";
    }
    
    
}