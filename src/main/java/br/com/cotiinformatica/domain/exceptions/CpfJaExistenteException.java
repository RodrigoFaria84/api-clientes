package br.com.cotiinformatica.domain.exceptions;

public class CpfJaExistenteException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CpfJaExistenteException(String cpf) {
		super("O cpf já está cadastrado: " + cpf);
	}
}
