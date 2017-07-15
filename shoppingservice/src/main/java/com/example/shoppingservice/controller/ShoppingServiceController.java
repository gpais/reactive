package com.example.shoppingservice.controller;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.shoppingservice.aggregate.MessagesAggregate;
import com.example.shoppingservice.command.CreateMessageCommand;
import com.example.shoppingservice.command.MarkReadMessageCommand;
import com.example.shoppingservice.model.Item;
import com.example.shoppingservice.repo.MessagesAggregateRepo;
import com.example.shoppingservice.request.CreateRequest;


@RestController
public class ShoppingServiceController {
	@Autowired
    private CommandGateway commandGateway;
	
	@Autowired
	private MessagesAggregateRepo repository;
	
	@Autowired
	private EntityManager entityManager;
	public ShoppingServiceController(){
	 System.out.print("initialise");	
	}
	
	@GetMapping("/check")
	String check() {
		return "Operational";
	}
	
	@GetMapping(value="/sendCommands", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	public void sendCommands(){
      final String itemId = UUID.randomUUID().toString();
      commandGateway.send(new CreateMessageCommand(itemId, "Hello, how is your day? :-)"));
      commandGateway.send(new MarkReadMessageCommand(itemId));
	}
	
	@GetMapping(value="/getSampleRequest", produces=MediaType.APPLICATION_JSON_VALUE)
	public CreateRequest createRequest(){
		CreateRequest request = new CreateRequest();
		request.setId(UUID.randomUUID().toString());
		request.setText("my text");
		return request;
   
	}
	
	

	@PostMapping("/create")
	public DeferredResult<Object> create(@RequestBody CreateRequest request) throws InterruptedException, ExecutionException {
		String referemce=(String) commandGateway.send(new CreateMessageCommand(UUID.randomUUID().toString(), "Hello, how is your day? :-)")).get();
		DeferredResult<Object> deferredResult = new DeferredResult<Object>();
//		Mono.fromFuture( commandGateway.send(new CreateMessageCommand(UUID.randomUUID().toString(), "Hello, how is your day? :-)")))
//		.map(b->repository.findById((String)b).get())
////		.and(uuid->Mono.fromFuture(commandGateway.send(new MarkReadMessageCommand((String) uuid))))
//		.subscribe(a->deferredResult.setResult(a), e->deferredResult.setErrorResult(e));
//		 
     
//		 personStream.subscribe(a->deferredResult.setResult(a));
		 Iterable<MessagesAggregate> value = repository.findAll();
		 value.forEach(a->{
			 System.out.print(a);
		 });
		deferredResult.setResult("sucess");
		 return deferredResult;
	}
	
	@PostMapping("/addItem")
	Mono<Void> createItem(@RequestBody Publisher<Item> personStream) {
		return null;
	}

	@GetMapping(value="/retrieveItems", produces=MediaType.TEXT_EVENT_STREAM_VALUE)
	Flux<Item> list() {
		
//      final String itemId = UUID.randomUUID().toString();
//      commandGateway.send(new CreateMessageCommand(itemId, "Hello, how is your day? :-)"));
//      commandGateway.send(new MarkReadMessageCommand(itemId));
		
		Flux<Long> interval = Flux.interval(Duration.ofSeconds(2l));
		
		Flux<String> itemNames=Flux.fromStream(Stream.of("couch","lamp","potrait","paint","cage", "dvd"));
		
		
		return Flux.zip(interval, itemNames)
		.map(x->{
			Item item = new Item();
			item.setName(x.getT2());
			return item;
		});
		
	}

	@GetMapping("/Item/{id}")
	Mono<Item> findById(@PathVariable String id) {
		return null;
	}
}
