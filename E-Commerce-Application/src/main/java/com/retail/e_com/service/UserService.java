package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;
import com.retail.e_com.requestdto.OTPRequest;
import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

public interface UserService {

	ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest);

}
