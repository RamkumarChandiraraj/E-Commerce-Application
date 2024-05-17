package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.requestdto.AddressRequest;
import com.retail.e_com.responsedto.AddressResponse;
import com.retail.e_com.service.AddressService;
import com.retail.e_com.utility.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fkv1")
@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173")
public class AddressController {

	private AddressService addressService;

	@PostMapping("/addAddress")
	public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(@RequestBody @Valid AddressRequest addressRequest 
			,@CookieValue(name = "at",required = false) String accessToken) {
		System.out.println("ramkumarr"+accessToken);
		return addressService.addAddress(addressRequest,accessToken);
	}

	@GetMapping("/findAddress")
	public ResponseEntity<?>findAddressByUser(@CookieValue(name = "at",required = false) String accessToken){
		return addressService.findAddressByUser(accessToken);
	}
	
	@PutMapping("/updateAddress")
	public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(@RequestBody AddressRequest addressRequst,@RequestParam int addressId)
	{
		return addressService.updateAddress(addressRequst,addressId);
	}
}
