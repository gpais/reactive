package com.example.shoppingservice.controller;

import java.time.Duration;
import java.util.stream.Stream;

import org.reactivestreams.Publisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import com.example.shoppingservice.model.Item;


@RestController
public class ShoppingServiceController {

	public ShoppingServiceController(){
	 System.out.print("initialise");	
	}
	@GetMapping("/check")
	String check() {
		return "Operational";
	}
	
	@PostMapping("/addItem")
	Mono<Void> create(@RequestBody Publisher<Item> personStream) {
		return null;
	}

	@GetMapping("/retrieveItems")
	Flux<Item> list() {
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
