package br.com.cotiinformatica.domain.exceptions;

public class EmailJaExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailJaExistenteException(String email) {
		super("O email já está cadastrado: " + email);
	}
}
