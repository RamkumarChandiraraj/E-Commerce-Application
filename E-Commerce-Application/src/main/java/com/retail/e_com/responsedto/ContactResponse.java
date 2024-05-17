package com.retail.e_com.responsedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ContactResponse {
	private int contactId;
	private String name;
	private String email;
	private Long phoneNumber;
}
