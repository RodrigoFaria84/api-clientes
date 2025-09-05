package br.com.cotiinformatica.integration;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ClienteControllerTest {

	@BeforeEach
	public void setUp() {
		
	}
	
	@Test
	@DisplayName("POST/api/v1/clientes - Deve retornar HTTP 201 ao cadastrar")
	public void postClientesReturnCreated() {
		fail("Não Implementado"); //TODO
	}
	
	@Test
	@DisplayName("PATCH/api/v1/clientes - Deve retornar HTTP 200 ao atualizar")
	public void pathClientesReturnReturnOK() {
		fail("Não Implementado"); //TODO
	}
	
	@Test
	@DisplayName("DELETE /api/v1/clientes - Deve retornar 200 ao excluir.")
	public void deleteClientesReturnsOk() {
		fail("Não implementado"); // TODO Implementar
	}
	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar todos.")
	public void getAllClientesReturnsOk() {
		fail("Não implementado"); // TODO Implementar
	}
	@Test
	@DisplayName("GET /api/v1/clientes - Deve retornar 200 ao consultar 1 cliente por ID.")
	public void getByIdClientesReturnsOk() {
		fail("Não implementado"); // TODO Implementar
	}

	
	
	
	
}
