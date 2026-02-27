package com.mahmoud.book.authentication;
import com.mahmoud.book.user.*;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mahmoud.book.email.EmailService;
import com.mahmoud.book.email.EmailTemplateName;
import com.mahmoud.book.role.RoleRepository;
import com.mahmoud.book.security.JwtService;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final RoleRepository rolerepository;
	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;
	private final TokenRepository tokenRepository;
	private final EmailService emailService;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private String activationUrl;
	public void register(@Valid RegistrationRequest request) throws MessagingException {
		// TODO Auto-generated method stub
		var userRole =rolerepository.findByName("USER")
				.orElseThrow(()->new IllegalStateException("ROLE USER Wasn't Intialized"));
		var user=User.builder()
				.firstName(request.getFirstName())
				.lastName(request.getLastName())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.acountLocked(false)
				.enabled(false)
				.roles(List.of(userRole))
				.build();
		userRepository.save(user);
		sendValidationEmail(user);
		
	}
	private void sendValidationEmail(User user) throws MessagingException {
		// TODO Auto-generated method stub
		var newToken=generateAndSaveActivationToken(user);
		emailService.sendEmail(user.getEmail(), user.getFullName(), EmailTemplateName.ACTIVATE_ACCOUNT, activationUrl, newToken, "Account Activation");
		
	}
	private String generateAndSaveActivationToken(User user) {
		// TODO Auto-generated method stub
		String generatedToken=generateActivationCode(6);
		var token=Token.builder()
				.token(generatedToken)
				.createdAt(LocalDateTime.now())
				.expiredAt(LocalDateTime.now().plusMinutes(15))
				.user(user)
				.build();
		tokenRepository.save(token);
		return generatedToken;
	}
	private String generateActivationCode(int length) {
		// TODO Auto-generated method stub
		String character="0123456789";
		StringBuilder codeBuilder=new StringBuilder();
		SecureRandom secureRandom=new SecureRandom();
		for(int i=0;i<length;i++) {
			int randomIndex=secureRandom.nextInt(character.length());
			codeBuilder.append(character.charAt(randomIndex));
		}
		return codeBuilder.toString();
	}
	public AuthenticationResponse authenticate(@Valid AuthnticationRequest request) {
		// TODO Auto-generated method stub
		var authenticate=authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
				);
		var claims=new HashMap<String,Object> ();
		var user=((User)authenticate.getPrincipal());
		claims.put("fullname", user.getFullName());
		var jwtToken=jwtService.generateToken(claims,user);
		return AuthenticationResponse.builder().token(jwtToken).build();
	}
//	@Transactional
	public void activateAccount(String token) throws MessagingException {
		// TODO Auto-generated method stub
		Token savedToken=tokenRepository.findByToken(token)
				.orElseThrow(()->new RuntimeException("Invalid Token"));
		if(LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
			sendValidationEmail(savedToken.getUser());
			throw new RuntimeException("Activation code has expired");
		}
		var user=userRepository.findById(savedToken.getUser().getId()).orElseThrow(() -> new UsernameNotFoundException("UserNot Found"));
		user.setEnabled(true);
		savedToken.setValidatedAt(LocalDateTime.now());
		tokenRepository.save(savedToken);
		
	}

}
