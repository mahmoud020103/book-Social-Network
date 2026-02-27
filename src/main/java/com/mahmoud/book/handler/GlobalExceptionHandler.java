package com.mahmoud.book.handler;
import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mahmoud.book.exception.OperationNotPermittedException;

import jakarta.mail.MessagingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ExceptionResponse> handleException(LockedException exp){
		return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				ExceptionResponse.builder()
				.businessErrorCode(BusinessErrorCode.ACCOUNT_LOCKED.getCode())
				.businessErrorDescrition(BusinessErrorCode.ACCOUNT_LOCKED.getDescription())
				.error(exp.getMessage())
				.build()
				);
				
		
	}
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<ExceptionResponse> handleException(DisabledException exp){
		return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				ExceptionResponse.builder()
				.businessErrorCode(BusinessErrorCode.ACCOUNT_DISABLED.getCode())
				.businessErrorDescrition(BusinessErrorCode.ACCOUNT_DISABLED.getDescription())
				.error(exp.getMessage())
				.build()
				);
				
		
	}
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionResponse> handleException(BadCredentialsException exp){
		return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
				ExceptionResponse.builder()
				.businessErrorCode(BusinessErrorCode.BAD_CREDENTIALS.getCode())
				.businessErrorDescrition(BusinessErrorCode.BAD_CREDENTIALS.getDescription())
				.error(BusinessErrorCode.BAD_CREDENTIALS.getDescription())
				.build()
				);
				
		
	}
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionResponse> handleException(MessagingException exp){
		return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				ExceptionResponse.builder()
				.error(exp.getMessage())
				.build()
				);
				
		
	}
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionResponse> handleException(MethodArgumentNotValidException exp){
		Set<String> errors=new HashSet<>();
		exp.getBindingResult().getAllErrors()
		.forEach(error->{
			var errorMessage=error.getDefaultMessage();
			errors.add(errorMessage);
		});
		return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				ExceptionResponse.builder()
				.validationErrors(errors)
				.build()
				);
				
		
	}
	@ExceptionHandler(OperationNotPermittedException.class)
	public ResponseEntity<ExceptionResponse> handleException(OperationNotPermittedException exp){
		return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
				ExceptionResponse.builder()
				.error(exp.getMessage())
				.build()
				);
	}
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionResponse> handleException(Exception exp){
		exp.printStackTrace();
		return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
				ExceptionResponse.builder()
				.businessErrorDescrition("Internal ERror ,please Contact the Admin")
				.error(exp.getMessage())
				.build()
				);
				
		
	}
}

