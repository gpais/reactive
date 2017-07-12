package com.example.shoppingservice.controller;

import java.time.Duration;
import java.util.UUID;
import java.util.stream.Stream;

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

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.shoppingservice.command.CreateMessageCommand;
import com.example.shoppingservice.command.MarkReadMessageCommand;
import com.example.shoppingservice.model.Item;


@RestController
public class ShoppingServiceController {
	@Autowired
    private CommandGateway commandGateway;
	
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
	@PostMapping("/addItem")
	Mono<Void> create(@RequestBody Publisher<Item> personStream) {
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
