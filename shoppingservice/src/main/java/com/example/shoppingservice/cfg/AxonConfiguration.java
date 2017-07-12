package com.example.shoppingservice.cfg;

import java.util.UUID;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.eventhandling.AnnotationEventListenerAdapter;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.axonframework.eventsourcing.eventstore.jpa.SnapshotEventEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.shoppingservice.aggregate.MessagesAggregate;
import com.example.shoppingservice.handler.MessagesEventHandler;


@Configuration
public class AxonConfiguration {
	@Bean
    public CommandBus comandBus(){
    	return new AsynchronousCommandBus();
    }
	
	@Bean
    public CommandGateway commandGateway(CommandBus commandBus){
    	return new DefaultCommandGateway(commandBus);
    }
	
	@Bean
    public EventStore eventStore( ){
		EventStore eventStore= new EmbeddedEventStore(new InMemoryEventStorageEngine());
		AnnotationEventListenerAdapter annotationEventListenerAdapter=aggregateAnnotationCommandHandler();
        eventStore.subscribe(eventMessages -> eventMessages.forEach(e -> {
                  try {
                	  annotationEventListenerAdapter.handle(e);
                  } catch (Exception e1) {
                      throw new RuntimeException(e1);

                  }
              }

      ));
      
		return eventStore;
    }
	
	@Bean
    public EventSourcingRepository eventSourcingRepository(EventStore eventStore){
    	return new EventSourcingRepository<>(MessagesAggregate.class, eventStore);
    }
	
	@Bean
    public AggregateAnnotationCommandHandler aggregateAnnotationCommandHandler(
    		EventSourcingRepository repository,
    		CommandBus commandBus){
	      AggregateAnnotationCommandHandler<MessagesAggregate> messagesAggregateAggregateAnnotationCommandHandler =
               new AggregateAnnotationCommandHandler<MessagesAggregate>(MessagesAggregate.class, repository);
         messagesAggregateAggregateAnnotationCommandHandler.subscribe(commandBus); 
         return messagesAggregateAggregateAnnotationCommandHandler;
	
	}
	
	public AnnotationEventListenerAdapter aggregateAnnotationCommandHandler(){
		AnnotationEventListenerAdapter annotationEventListenerAdapter=  new AnnotationEventListenerAdapter(new MessagesEventHandler());
		return annotationEventListenerAdapter;
	    
	}
 

//
//       final String itemId = UUID.randomUUID().toString();
//       commandGateway.send(new CreateMessageCommand(itemId, "Hello, how is your day? :-)"));
//       commandGateway.send(new MarkReadMessageCommand(itemId));
}
