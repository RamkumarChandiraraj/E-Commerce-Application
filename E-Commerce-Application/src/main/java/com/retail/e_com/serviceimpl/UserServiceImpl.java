package com.retail.e_com.serviceimpl;

import java.util.Random;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.EmailNotCorrectException;
import com.retail.e_com.exception.InvalidOTPException;
import com.retail.e_com.exception.InvalidUserRoleSpecifiedException;
import com.retail.e_com.exception.OTPExpiredException;
import com.retail.e_com.exception.RegistrationSessionExpiredException;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;
import com.retail.e_com.mail_service.MailService;
import com.retail.e_com.mail_service.MessageModel;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.repository.UserRepository;
import com.retail.e_com.requestdto.OTPRequest;
import com.retail.e_com.requestdto.UserRequest;
import com.retail.e_com.responsedto.UserResponse;
import com.retail.e_com.service.UserService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
	private UserRepository userRepository;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private ResponseStructure<UserResponse> structure;
	private SimpleResponseStructure simpleResponse;
	private MailService mailService;

	@Override
	public ResponseEntity<SimpleResponseStructure> userRegistration(UserRequest userRequest) {
		if(userRepository.existsByEmail(userRequest.getEmail()))
			throw new UserAlreadyExistByEmailException("Faild to register User");
		User user=mapToChildEntity(userRequest);
		String otp=generatedOTP();

		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);

		//System.err.println(otp);
		try {
			sendOTP(user,otp);
		} catch (MessagingException e) {
			throw new EmailNotCorrectException("Email  is not correct");
		}
		System.out.println(otp);
		return ResponseEntity.ok(simpleResponse.setStatus(HttpStatus.ACCEPTED.value()).setMessage("Verify OTP sent through the email to complete the registration ! otp expires in 5 minutes"));
	}

	private void sendOTP(User user, String otp) throws MessagingException {
		MessageModel model=MessageModel.builder()
				.to(user.getEmail())
				.subject("Verify your OTP")
				.text(
						"<p>Hii, <br>"
								+"Thanks for your interest in E-Com, "
								+"Please verify your mail Id using the OTP given below. </p>"
								+"<br>"
								+"<h1>"+otp+"</h1>"
								+"<br>"
								+"<p>Please ignore if its not you</p>"
								+"<br>"
								+"With best regards"
								+"<h3>E-Com</h3>"
						)
				.build();

		mailService.sendMailMessage(model);
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponse>> verifyOTP(OTPRequest otpRequest) {
		if(otpCache.getData(otpRequest.getEmail())==null) throw new OTPExpiredException("otp had expired");
		if(!otpCache.getData(otpRequest.getEmail()).equals(otpRequest.getOtp())) throw new InvalidOTPException("OTP entered is wrong");
		User user=userCache.getData(otpRequest.getEmail());
		if(user==null) throw new RegistrationSessionExpiredException("User Registration session expired");
		user.setEmailVerified(true);
		//user=userRepository.save(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(structure.setData(mapToUserResponse(user))
						.setMessage("OTP verification successful")
						.setStatusCode(HttpStatus.CREATED.value()));
	}

	private String generatedOTP() {
		return String.valueOf(new Random().nextInt(100000,999999));
	}

	private <T extends User> T mapToChildEntity(UserRequest userRequest) {
		UserRole userRole=userRequest.getUserRole();
		User user=null;
		switch(userRole)
		{
		case SELLER ->user= new Seller();
		case CUSTOMER ->user=new Customer();
		default -> throw new InvalidUserRoleSpecifiedException("User entered is not specified");
		}
		user.setDisplayName(userRequest.getName());
		user.setUsername(userRequest.getEmail().split("@gmail.com")[0]);
		user.setEmail(userRequest.getEmail());
		user.setPassword(userRequest.getPassword());
		user.setUserRole(userRole);
		user.setEmailVerified(false);
		user.setDeleted(false);
		return (T)user;
	}

	private UserResponse mapToUserResponse(User user) {
		return UserResponse.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.displayName(user.getDisplayName()) 
				.email(user.getEmail())
				.isEmailVerified(user.isEmailVerified())
				.isDeleted(user.isDeleted())
				.userRole(user.getUserRole())
				.build();
	}
}
