package br.com.cotiinformatica.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

//Registro
public record NovoClienteEvent(
		UUID id,		//Id do cliente
		String nome,	//Nome do cliente
		String email,	//Email do cliente
		String cpf,		//Cpf do cliente
		LocalDateTime dataHoraCadastro	//Data e hora do cadastro
		) {
}
