package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.requestdto.ContactRequest;
import com.retail.e_com.responsedto.ContactResponse;
import com.retail.e_com.service.ContactService;
import com.retail.e_com.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/fkv2")
@CrossOrigin(allowCredentials = "true",origins = "http://localhost:5173")
public class ContactController {

	private ContactService contactService;

	@PostMapping("/addContact/{addressId}")
	public ResponseEntity<ResponseStructure<ContactResponse>>addContact(@RequestBody  ContactRequest contactRequest,@PathVariable int  addressId){
		return contactService.addContact(contactRequest,addressId);
	}
	@PutMapping("/updateContact")
    public ResponseEntity<ResponseStructure<ContactResponse>> updateContact(@RequestBody ContactRequest contactRequest, @RequestParam int contactId){
		System.out.println(contactId);
        return contactService.updateContact(contactRequest, contactId);
    }
}
