package br.com.cotiinformatica.infrastructure.handlers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.cotiinformatica.domain.exceptions.CpfJaExistenteException;
import br.com.cotiinformatica.domain.exceptions.EmailJaExistenteException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/*
	 * Tratamento para erro 'EmailJaExistenteException'
	 */
	@ExceptionHandler(EmailJaExistenteException.class)
	public ResponseEntity<Map<String, Object>> handleEmailJaExistente(EmailJaExistenteException e) {		
		return buildResponse(HttpStatus.CONFLICT, e.getMessage()); //HTTP 409 (Conflict)
	}
	
	/*
	 * Tratamento para erro 'CpfJaExistenteException'
	 */
	@ExceptionHandler(CpfJaExistenteException.class)
	public ResponseEntity<Map<String, Object>> handleCpfJaExistente(CpfJaExistenteException e) {		
		return buildResponse(HttpStatus.CONFLICT, e.getMessage()); //HTTP 409 (Conflict)
	}
	
	/*
	 * Tratamento para erro 'RuntimeException'
	 */
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {		
		return buildResponse(HttpStatus.BAD_REQUEST, e.getMessage()); //HTTP 400 (BadRequest)
	}
	
	/*
	 * Tratamento para erros de validação (BeanValidation)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("validationErrors", errors);

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
	
	/*
	 * Método para tratar e retornar o JSON do erro
	 */
	private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("status", status);
		body.put("error", status.getReasonPhrase());
		body.put("message", message);
		
		return new ResponseEntity<>(body, status);
	}
}
