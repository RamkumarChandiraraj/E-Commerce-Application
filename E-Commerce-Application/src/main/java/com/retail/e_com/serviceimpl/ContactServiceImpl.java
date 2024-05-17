package com.retail.e_com.serviceimpl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.e_com.exception.AddressNotFoundByIdException;
import com.retail.e_com.exception.ContactLimitOverFlowException;
import com.retail.e_com.model.Contact;
import com.retail.e_com.repository.AddressRepository;
import com.retail.e_com.repository.ContactRepository;
import com.retail.e_com.requestdto.ContactRequest;
import com.retail.e_com.responsedto.ContactResponse;
import com.retail.e_com.service.ContactService;
import com.retail.e_com.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService{
	private ResponseStructure<ContactResponse> responseStructure;
	private AddressRepository addressRepository;
	private ContactRepository contactRepository;
	@Override
	public ResponseEntity<ResponseStructure<ContactResponse>> addContact(ContactRequest contactRequest,
			int addressId) {
		return addressRepository.findById(addressId).map(address->{
			Contact contact=maptoContact(contactRequest);
			if(address.getContacts().size()>2) throw new ContactLimitOverFlowException("Contact limit reached");

			address.getContacts().add(contact);
			contactRepository.save(contact);
			addressRepository.save(address);
			return ResponseEntity.ok().body(responseStructure.setMessage("Contact added to the address sucessful")
					.setStatusCode(HttpStatus.OK.value())
					.setData(maptoContactResponse(contact)));
		}).orElseThrow(() -> new AddressNotFoundByIdException("Addreess not found by the Id"));
	}
	private Contact maptoContact( ContactRequest contactRequest) {
		Contact contact=new Contact();
		System.out.println(contactRequest.getName());
		contact.setName(contactRequest.getName());
		contact.setEmail(contactRequest.getEmail());
		contact.setPhoneNumber(contactRequest.getPhoneNumber());
		contact.setPriority(contactRequest.getPriority());
		return contact;
	}
	private ContactResponse maptoContactResponse(Contact contact) {
		return ContactResponse.builder()
				.contactId(contact.getContactId())
				.name(contact.getName())
				.email(contact.getEmail())
				.phoneNumber(contact.getPhoneNumber()).build();
	}
	@Override
	public ResponseEntity<ResponseStructure<ContactResponse>> updateContact(ContactRequest contactRequest,int contactId) {
		return contactRepository.findById(contactId).map(contact->{
			//Contact updatedContact=maptoContact(new ContactRequest());
			contact.setEmail(contactRequest.getEmail());
			contact.setName(contactRequest.getName());
			contact.setPhoneNumber(contactRequest.getPhoneNumber());
			contact.setPriority(contactRequest.getPriority());
			Contact updatedContact=contactRepository.save(contact);
			return ResponseEntity.ok()
					.body(responseStructure.setMessage("Contact updated successfully")
							.setStatusCode(HttpStatus.OK.value())
							.setData(maptoContactResponse(updatedContact)));
		}).orElseThrow(() ->new AddressNotFoundByIdException("Contact not found by the Id"));
	}
}
