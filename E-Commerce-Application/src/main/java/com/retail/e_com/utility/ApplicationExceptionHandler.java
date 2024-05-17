package com.retail.e_com.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.retail.e_com.exception.AddressNotFoundByIdException;
import com.retail.e_com.exception.AddressNotFoundByUserException;
import com.retail.e_com.exception.ContactLimitOverFlowException;
import com.retail.e_com.exception.ContactNotFoundByUserException;
import com.retail.e_com.exception.EmailNotCorrectException;
import com.retail.e_com.exception.ImageNotFormatedExcption;
import com.retail.e_com.exception.ImageNotFoundByIdException;
import com.retail.e_com.exception.ImageTypeInvalidException;
import com.retail.e_com.exception.InvalidCreaditionalException;
import com.retail.e_com.exception.InvalidOTPException;
import com.retail.e_com.exception.InvalidUserRoleSpecifiedException;
import com.retail.e_com.exception.OTPExpiredException;
import com.retail.e_com.exception.ProductNotFoundByIdException;
import com.retail.e_com.exception.RegistrationSessionExpiredException;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;
import com.retail.e_com.exception.UserNotLoggedInException;
import com.retail.e_com.exception.UserTokenBlockedStateException;

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
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerUserTokenBlocked(UserTokenBlockedStateException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"User in blocked state");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerInvalidCreaditional(InvalidCreaditionalException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Invalid Creaditional");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerUserNotLoggedIn(UserNotLoggedInException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Failed to Refresh login");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerAddressNotFoundByUser(AddressNotFoundByUserException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Address not found by User");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerContactNotFoundByUser(ContactNotFoundByUserException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Contact not found by User");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerContactLimitOverFlow(ContactLimitOverFlowException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Contact limit over");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerAddressNotFoundById(AddressNotFoundByIdException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Address not found by Id");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerProductNotFoundById(ProductNotFoundByIdException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Product not found by Id");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerImageNotFormated(ImageNotFormatedExcption ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Image not formated");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerImageTypeInvalid(ImageTypeInvalidException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Image Type invalid");
	}
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlerImageNotFoundById(ImageNotFoundByIdException ex)
	{
		return errorResponse(HttpStatus.BAD_REQUEST,ex.getMessage(),"Image not found by id");
	}
}
