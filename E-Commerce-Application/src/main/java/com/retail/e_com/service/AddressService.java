package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.requestdto.AddressRequest;
import com.retail.e_com.responsedto.AddressResponse;
import com.retail.e_com.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface AddressService {

	ResponseEntity<ResponseStructure<AddressResponse>> addAddress(@Valid AddressRequest addressRequest,	String accessToken);

	ResponseEntity<?> findAddressByUser(String accessToken);

	ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequst, int addressId);


}
