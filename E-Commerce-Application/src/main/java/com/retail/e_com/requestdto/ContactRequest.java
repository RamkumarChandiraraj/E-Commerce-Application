package com.retail.e_com.requestdto;

import com.retail.e_com.enums.ContactPriority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactRequest {
	private String name;
	private Long phoneNumber;
	private String email;
	private ContactPriority priority;
}
