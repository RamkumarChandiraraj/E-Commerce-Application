package com.retail.e_com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.AddressNotFoundByIdException;
import com.retail.e_com.exception.AddressNotFoundByUserException;
import com.retail.e_com.exception.ContactNotFoundByUserException;
import com.retail.e_com.jwt.JwtService;
import com.retail.e_com.model.Address;
import com.retail.e_com.model.Contact;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.Seller;
import com.retail.e_com.repository.AddressRepository;
import com.retail.e_com.repository.ContactRepository;
import com.retail.e_com.repository.UserRepository;
import com.retail.e_com.requestdto.AddressRequest;
import com.retail.e_com.responsedto.AddressResponse;
import com.retail.e_com.responsedto.ContactResponse;
import com.retail.e_com.service.AddressService;
import com.retail.e_com.utility.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService{
	private JwtService jwtService;
	private UserRepository userRepostiory;
	private AddressRepository addressRepository;
	private ContactRepository contactRepository;
	private ResponseStructure<AddressResponse> responseStructure;

	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(@Valid AddressRequest addressRequest, String accesstoken) {
		System.out.println("Hii");
		String username = jwtService.getUsername(accesstoken);
		System.out.println(username);
		return userRepostiory.findByUsername(username).map(user->{
			Address address=mapToAddress(addressRequest,new Address());
			if(user.getUserRole()==UserRole.SELLER) {
				if(((Seller)user).getAddress()==null) {
					address=addressRepository.save(address);
					((Seller)user).setAddress(address);
					userRepostiory.save(user);
				}
			}
			else if(user.getUserRole()==UserRole.CUSTOMER) {
				if(((Customer)user).getAddress().size()<5) {
					address=addressRepository.save(address);
					((Customer)user).getAddress().add(address);
					userRepostiory.save(user);
				}
			}
			return ResponseEntity.ok().body(responseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("Address added Successfully")
					.setData(mapToAddressResponse(address)));
		}).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
	}
	private AddressResponse mapToAddressResponse(Address address) {
		List<ContactResponse> mapToContactResponses =null;
		if(address.getContacts()!=null) {
			mapToContactResponses= mapToContactResponses(address.getContacts());
		}

		return AddressResponse.builder().addressId(address.getAddressId()).streetAddress(address.getStreetAddress())
				.streetAddressAditional(address.getStreetAddressAdditional()).city(address.getCity())
				.state(address.getState()).pincode(address.getPincode()).addressType(address.getAddressType())
				. contacts( mapToContactResponses )
				.build();

	}
	private List<ContactResponse> mapToContactResponses(List<Contact> contacts) {
		List<ContactResponse> contactResponses=new ArrayList<>();

		for(Contact contact:contacts)
		{
			contactResponses.add(mapToContactResponse(contact));
		}
		return contactResponses;
	}
	private ContactResponse mapToContactResponse(Contact contact) {
		return ContactResponse.builder()
				.contactId(contact.getContactId())
				.name(contact.getName())
				.email(contact.getEmail())
				.phoneNumber(contact.getPhoneNumber()).build();
	}
	private Address mapToAddress(AddressRequest addressRequest, Address address) {
		address.setStreetAddress(addressRequest.getStreetAddress());
		address.setStreetAddressAdditional(addressRequest.getStreetAddressAditional());
		address.setCity(addressRequest.getCity());
		address.setState(addressRequest.getState());
		address.setPincode(addressRequest.getPincode());
		address.setAddressType(addressRequest.getAddressType());
		return address;
	}
	@Override
	public ResponseEntity<?> findAddressByUser(String accessToken) {
		String username = jwtService.getUsername(accessToken);
		return userRepostiory.findByUsername(username).map(user->{
			List<Address> addresses=null;
			Address address=null;
			List<Contact> contacts=null;
			//			if(user.getUserRole()==UserRole.SELLER) {
			//				address=((Seller)user).getAddress();
			//				if(address==null) throw new AddressNotFoundByUserException("User Does not have the Address");
			//				if(contacts==null)throw new ContactNotFoundByUserException("User does not have any contact");
			//				return ResponseEntity.ok().body(responseStructure.setData(mapToAddressResponse(address)));
			//			}
			if(user.getUserRole()==UserRole.SELLER) {
				address=((Seller)user).getAddress();
			}
			else if(user.getUserRole()==UserRole.CUSTOMER) {
				addresses=((Customer)user).getAddress();
			}
			if(address == null && (addresses==null||addresses.isEmpty())) {
				throw new AddressNotFoundByUserException("User Does not have the Address");
			}
			//			if(user.getUserRole()==UserRole.CUSTOMER) {
			//				addresses=((Customer)user).getAddress();
			//				if(addresses==null) throw new AddressNotFoundByUserException("The User does not have the address");
			//				return ResponseEntity.ok().body(responseStructure.setLists(mapToAddressResponseList(addresses)));
			//			}
			//			return null;
			if(address!=null) {
				return ResponseEntity.ok().body(responseStructure.setData(mapToAddressResponse(address)));
			}
			else {
				return ResponseEntity.ok().body(responseStructure.setLists(mapToAddressResponseList(addresses)));
			}
		}).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
	}

	private List<AddressResponse> mapToAddressResponseList(List<Address> addresses)
	{

		List<AddressResponse> addressResponses=new ArrayList<>();
		for(Address address: addresses)
		{
			addressResponses.add(mapToAddressResponse(address));
		}
		return addressResponses;
	}	

	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> updateAddress(AddressRequest addressRequst,int addressId) {
		Address address2=addressRepository.findById(addressId).map(address->{
			return addressRepository.save(mapToAddress(addressRequst,address));
		}).orElseThrow(() -> new AddressNotFoundByIdException("Address not found by Id"));
		return ResponseEntity.ok(new ResponseStructure<AddressResponse>()
				.setStatusCode(HttpStatus.OK.value())
				.setMessage("Address Updated")
				.setData(mapToAddressResponse(address2)));
	}
}
