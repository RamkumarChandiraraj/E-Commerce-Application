package com.retail.e_com.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.model.Contact;
import com.retail.e_com.requestdto.ContactRequest;
import com.retail.e_com.responsedto.ContactResponse;
import com.retail.e_com.utility.ResponseStructure;

public interface ContactService {

	ResponseEntity<ResponseStructure<ContactResponse>> addContact(ContactRequest contactRequest, int addressId);

	ResponseEntity<ResponseStructure<ContactResponse>> updateContact(ContactRequest contactRequest, int contactId);

}
