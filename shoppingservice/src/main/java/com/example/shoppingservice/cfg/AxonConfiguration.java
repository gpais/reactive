package com.example.shoppingservice.cfg;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.axonframework.commandhandling.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.AsynchronousCommandBus;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.AnnotationEventListenerAdapter;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.jpa.JpaSagaStore;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.eventsourcing.eventstore.jpa.JpaEventStorageEngine;
import org.axonframework.messaging.interceptors.BeanValidationInterceptor;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.example.shoppingservice.aggregate.MessagesAggregate;
import com.example.shoppingservice.handler.MessagesEventHandler;


@Configuration
@EnableTransactionManagement
public class AxonConfiguration {
	
     @Autowired
     private SagaStore<Object> sagaStore;
    
     @Autowired
     private PlatformTransactionManager platformTransactionManager;
     
     @PersistenceContext
     private EntityManager entityManager;
    
     @Bean
     public SagaStore<Object> sagaStore(EntityManagerProvider provider) {
     return new JpaSagaStore(provider);
     }
//    
//     @Bean
//     public SpringResourceInjector resourceInjector() {
//     return new SpringResourceInjector();
//     }
//    
//     @Bean
//     public SagaRepository<MoneyTransferSaga> moneyTransferSagaRepository() {
//     return new AnnotatedSagaRepository<>(MoneyTransferSaga.class, sagaStore, resourceInjector());
//     }
//    
//     @Bean
//     public AbstractSagaManager<MoneyTransferSaga> moneyTransferSagaManager() {
//     return new AnnotatedSagaManager<>(
//     MoneyTransferSaga.class,
//     moneyTransferSagaRepository());
//     }
    
     
     @Bean("axontransactionmanager")
     public TransactionManager transactionManager() {
    	 return new SpringTransactionManager(platformTransactionManager);
     }
     
     @Bean
 	 @DependsOn("axontransactionmanager")
     public JpaEventStorageEngine eventStorageEngine(EntityManagerProvider provider, TransactionManager transactionManager) {
    	return  new JpaEventStorageEngine(provider, transactionManager);
     }
    
//     @Bean
//     public Repository<Account> jpaAccountRepository(EventBus eventBus) {
//     return new GenericJpaRepository<>(entityManagerProvider(), Account.class, eventBus);
//     }
//    
     @Bean
     @Transactional
     public Repository<MessagesAggregate> jpaMoneyTransferRepository(EventBus eventBus,EntityManagerProvider provider) {
     return new GenericJpaRepository<>(provider, MessagesAggregate.class, eventBus);
     }
    
   
     
     @Bean
     public EntityManagerProvider entityManagerProvider() {
    	 ContainerManagedEntityManagerProvider entityManagerprovider= new ContainerManagedEntityManagerProvider();
//    	 entityManagerprovider.setEntityManager(entityManager);
    	 return entityManagerprovider;
     }
    
 
    
//     @Autowired
//     public void configure(EventHandlingConfiguration configuration) {
//     configuration.registerTrackingProcessor("TransactionHistory");
//     }	
	
	
	
	
	
	
	
	
	
	
//
//     @Bean
//     @Transactional
//     CommandBus commandBus(TransactionManager transactionManager) {
//         SimpleCommandBus commandBus = new AsynchronousCommandBus();
//         commandBus.registerDispatchInterceptor(new BeanValidationInterceptor());
//         return commandBus;
//     }
	
     @Bean
     CommandBus commandBus(TransactionManager transactionManager) {
         SimpleCommandBus commandBus = new SimpleCommandBus(transactionManager, NoOpMessageMonitor.INSTANCE);
         commandBus.registerDispatchInterceptor(new BeanValidationInterceptor());
         return commandBus;
     }
	
	@Bean
    public CommandGateway commandGateway(CommandBus commandBus){
    	return new DefaultCommandGateway(commandBus);
    }
//	
//	
//	
	@Bean
    public EventStore eventStore(EventStorageEngine engine ){
		
		EventStore eventStore= new EmbeddedEventStore(engine);
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
	
//	@Bean
//	public EventStorageEngine eventStrorageEngine(){
//		return new LegacyJpaEventStorageEngine(entityManagerProvider());
//	}
 

//
//       final String itemId = UUID.randomUUID().toString();
//       commandGateway.send(new CreateMessageCommand(itemId, "Hello, how is your day? :-)"));
//       commandGateway.send(new MarkReadMessageCommand(itemId));
}
