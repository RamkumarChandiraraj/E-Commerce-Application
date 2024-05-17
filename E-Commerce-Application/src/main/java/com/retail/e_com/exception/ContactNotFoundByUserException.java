package com.retail.e_com.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactNotFoundByUserException extends RuntimeException{
	private String message;
}
