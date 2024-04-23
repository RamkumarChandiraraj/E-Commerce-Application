package com.retail.e_com.utility;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestControllerAdvice
public class ApplicationHandler extends ResponseEntityExceptionHandler{
	private ErrorStructure<Object> structure;
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		Map<String,String> messages=new HashMap<>();
		ex.getAllErrors().forEach(error->{
			messages.put(((FieldError)error).getField(),error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(
				structure.setStatus(HttpStatus.BAD_REQUEST.value())
				.setMessage("Invalid inputs")
				.setRootCause(messages));
	}

}

