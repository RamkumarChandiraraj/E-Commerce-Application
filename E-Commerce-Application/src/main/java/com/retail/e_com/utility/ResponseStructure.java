package com.retail.e_com.utility;

import org.springframework.stereotype.Component;
import lombok.Getter;

@Getter
@Component
public class ResponseStructure<T> {
	private int statusCode;
	private String message;
	private T data;
	public ResponseStructure<T> setStatusCode(int statusCode) {
		this.statusCode = statusCode;
		return this;
	}
	public ResponseStructure<T> setMessage(String message) {
		this.message = message;
		return this;
	}
	public ResponseStructure<T> setData(T uniqueUser) {
		this.data = uniqueUser;
		return this;
	}
}
