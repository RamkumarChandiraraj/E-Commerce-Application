package com.retail.e_com.requestdto;

import com.retail.e_com.enums.AddressType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {
	private String streetAddress;
	private String streetAddressAditional;
	private String city;
	private String state;
	private int pincode;
	private AddressType addressType;
}
