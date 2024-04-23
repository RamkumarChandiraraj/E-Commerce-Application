package com.retail.e_com.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.e_com.exception.EmailNotCorrectException;
import com.retail.e_com.exception.InvalidOTPException;
import com.retail.e_com.exception.InvalidUserRoleSpecifiedException;
import com.retail.e_com.exception.OTPExpiredException;
import com.retail.e_com.exception.RegistrationSessionExpiredException;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler {
	private ErrorStructure<String> structure;

	private ResponseEntity<ErrorStructure<String>> errorResponse(HttpStatus status,String message,String rootCause)
	{
		return new ResponseEntity<ErrorStructure<String>>(structure.setStatus(status.value())
				.setMessage(message)
				.setRootCause(rootCause),status);
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerUserAlreadyExistByEmail(UserAlreadyExistByEmailException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User Already Exists with the given email Id");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerInvalidUserRoleSpecified(InvalidUserRoleSpecifiedException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User Entered is invalid");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerInvalidOTP(InvalidOTPException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Invalid otp");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerOTPExpired(OTPExpiredException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"OTP expired");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerRegistrationSession(RegistrationSessionExpiredException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User registration got expired");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerEmailNotCorrect(EmailNotCorrectException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Email is not correct");
	}
}
