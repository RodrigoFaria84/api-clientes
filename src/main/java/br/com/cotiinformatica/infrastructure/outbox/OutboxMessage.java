package br.com.cotiinformatica.infrastructure.outbox;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class OutboxMessage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String aggregateType;	//Ex: Cliente
	private String aggregateId;		//Ex: Id do cliente
	private String type;			//Ex: Novo cliente
	private String payload;			//Ex: Dados do Record do evento
	
	//Flag para indicar se o evento foi enviado para a fila do RabbitMQ
	private boolean published = false; 
	
	private LocalDateTime createdAt = LocalDateTime.now();
	private LocalDateTime transmittedAt;
}
