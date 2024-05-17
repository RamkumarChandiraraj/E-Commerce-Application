package com.retail.e_com.responsedto;

import java.util.List;

import com.retail.e_com.enums.AddressType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AddressResponse {
	private int addressId;
	private String streetAddress;
	private String streetAddressAditional;
	private String city;
	private String state;
	private int pincode;
	private AddressType addressType;
	private List<ContactResponse> contacts;
}
