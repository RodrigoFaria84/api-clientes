package br.com.cotiinformatica.infrastructure.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.cotiinformatica.infrastructure.outbox.OutboxMessage;

@Repository
public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, Long> {

	/*
	 * Consulta paginada que retorne todos os registros de mensagens
	 * que não foram transmitidos para a fila do RabbitMQ
	 */
	
	@Query("""
			SELECT outbox FROM OutboxMessage outbox
			WHERE outbox.published = false
			AND outbox.type = :param_type
			ORDER BY outbox.createdAt ASC
			""")
	List<OutboxMessage> find(
			@Param("param_type") String type,
			Pageable pageable
			);
}
