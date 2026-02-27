package com.mahmoud.book.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthnticationRequest {
	@NotEmpty(message = "Email is Mandatory")
	@NotBlank(message = "Email is Mandatory")
	@Email(message = "email is not formatted")
	private String email;
	@NotEmpty(message = "Password is Mandatory")
	@NotBlank(message = "Password is Mandatory")
	@Size(min = 8,message = "Password Should be at Least 8 Character")
	private String password;
}
